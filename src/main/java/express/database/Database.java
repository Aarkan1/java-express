package express.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import express.Express;
import express.database.exceptions.DatabaseNotEnabledException;
import express.database.exceptions.ModelsNotFoundException;
import io.javalin.http.UploadedFile;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Id;
import org.reflections8.Reflections;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Johan Wirén
 *
 * The embedded database based on Nitrite DB
 *
 * Documentation: https://www.dizitart.org/nitrite-database
 */
public class Database {
    private static Map<String, Collection> collections = new HashMap<>();
    private static List<WsContext> clients = new CopyOnWriteArrayList<>();
    private static boolean enabledDatabase = false;
    private static Express app;
    private static Express express;
    private static Logger logger;
    private static boolean enableWatcher = false;
    private static boolean disableBrowser = false;

    public Database(Express app) {
        Database.app = app;
        try {
            init("db/embedded.db");
        } catch (ModelsNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Database(Express app, CollectionOptions... options) {
        Database.app = app;
        setOptions(options);

        try {
            init("db/embedded.db");
        } catch (ModelsNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Database(String dbPath, Express app) {
        Database.app = app;
        try {
            init(dbPath);
        } catch (ModelsNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Database(String dbPath, Express app, CollectionOptions... options) {
        Database.app = app;
        setOptions(options);

        try {
            init(dbPath);
        } catch (ModelsNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setOptions(CollectionOptions... options) {
        for (CollectionOptions option : options) {
            if(option == CollectionOptions.ENABLE_WATCHER) enableWatcher = true;
            else if(option == CollectionOptions.DISABLE_BROWSER) disableBrowser = true;
        }
    }

    private static void init(String dbPath) throws ModelsNotFoundException {
        logger = Logger.getLogger(Database.class.getSimpleName());

        File directory = new File(Paths.get(dbPath).toString());
        directory.getParentFile().mkdirs(); // create parent dirs if necessary

        Nitrite db = Nitrite.builder()
                .compressed()
                .filePath(Paths.get(dbPath).toString())
                .openOrCreate("fwEWfwGhjyuYThtgSD", "dWTRgvVBfeeetgFR");

        Reflections reflections = new Reflections();
        Set<Class<?>> klasses = reflections.getTypesAnnotatedWith(Model.class);
        Map<String, Class<?>> collNames = new HashMap<>();
        Map<String, String> idFields = new HashMap<>();

        if(klasses.isEmpty()) throw new ModelsNotFoundException("Must have a class with @Model to use embedded database.");

        klasses.forEach(klass -> {
            String klassName = klass.getSimpleName();

            for(Field field : klass.getDeclaredFields()) {
                if(field.isAnnotationPresent(Id.class)) {
                    idFields.putIfAbsent(klassName, field.getName());
                    break;
                }
            }

            collNames.putIfAbsent(klassName, klass);
            collections.putIfAbsent(klassName, new Collection(db.getRepository(klass), klass, idFields.get(klassName)));
        });

        enabledDatabase = true;
        if (enableWatcher) watchCollections(collNames);
        if(!disableBrowser) initBrowser(collNames, idFields);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            db.close();
            if(express != null) express.stop();
        }));
    }

    /**
     * Embedded server to browse collections locally
     */
    private static void initBrowser(Map<String, Class<?>> collNames, Map<String, String> idFields) {
        express = new Express();

        express.get("/rest/collNames", (req, res) -> res.json(collNames.keySet()));

        express.get("/rest/:coll", (req, res) -> {
            String coll = req.params("coll");
            Map data = new HashMap();
            data.put(idFields.get(coll), collection(coll).find());
            res.json(data);
        });

        express.delete("/rest/:coll/:id", (req, res) -> {
            String coll = req.params("coll");
            String id = req.params("id");
            collection(coll).deleteById(id);
            res.send("OK");
        });

        // route to handle json mockdata imports
        express.post("/rest/:coll", (req, res) -> {
            UploadedFile file = req.formDataFile("files");
            ObjectMapper mapper = new ObjectMapper();
            Object[] objects = mapper.readValue(file.getContent(), Object[].class);
            Class klass = collNames.get(req.params("coll"));
            List models = new ArrayList();
            try {
                for(Object obj : objects) models.add(mapper.readValue(mapper.writeValueAsBytes(obj), klass));
            } catch (UnrecognizedPropertyException e) {
                logger.log(Level.WARNING, "Could not convert JSON.", e);
                res.status(500).stop(e.getMessage());
            }
            res.json(collection(klass).save(models));
        });

        express.delete("/api/drop-collection/:coll", (req, res) -> {
            res.send(collection(req.params("coll")).delete());
        });

        express.get("/api/export-collection/:coll", (req, res) -> {
            String coll = req.params("coll");
            List models = collection(coll).find();
            String path = Paths.get("db/" + coll + ".json").toString();
            File file = new File(path);
            file.getParentFile().mkdirs();   // create parent dirs if necessary
            if(file.exists()) file.delete(); // replace existing file
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new File(path), models);
            res.sendFile(Paths.get("db/" + coll + ".json"));
        });

        express.useStatic("/public", Location.CLASSPATH);

        express.listen(9595);
    }

    public static Collection collection(Object model) {
        return collection(model.getClass().getSimpleName());
    }

    public static Collection collection(Class klass) { return collection(klass.getSimpleName()); }

    public static Collection collection(String klass) {
        try {
            return getColl(klass);
        } catch (DatabaseNotEnabledException | NullPointerException e) {
            logger.log(Level.WARNING, "Database not enabled.", e);
        }
        return null;
    }

    private static Collection getColl(String klass) throws DatabaseNotEnabledException {
        if(enabledDatabase) {
            return collections.get(klass);
        } else {
            throw new DatabaseNotEnabledException("Database is not enabled");
        }
    }

    private static void watchCollections(Map<String, Class<?>> collNames) {
        app.ws("/watch-collections", ws -> {
            ws.onConnect(ctx -> clients.add(ctx));
            ws.onClose(ctx -> clients.remove(ctx));
            collNames.keySet().forEach(coll ->
                collection(coll).watch(watchData ->
                    clients.stream().filter(client -> client.session.isOpen())
                    .forEach(client -> client.send(watchData))
            ));
        });
    }

}
