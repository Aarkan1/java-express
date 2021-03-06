package express.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import express.Express;
import express.JavalinUtil;
import io.javalin.http.UploadedFile;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import nosqlite.annotations.Document;
import nosqlite.annotations.Id;
import org.reflections8.Reflections;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static nosqlite.Database.collection;

/**
 * @author Johan Wirén
 *
 */
public class Database {
    private static List<WsContext> clients = new CopyOnWriteArrayList<>();
    private static Map<String, List<WsContext>> clientsMap = new ConcurrentHashMap<>();
    private static Express app;
    private static Express express;

    public Database(Express app) {
        Database.app = app;
        init();
    }

    private static void init() {
        Reflections reflections = new Reflections();
        Set<Class<?>> klasses = reflections.getTypesAnnotatedWith(Document.class);
        Map<String, Class<?>> collNames = new HashMap<>();
        Map<String, String> idFields = new HashMap<>();

        klasses.forEach(klass -> {
            String klassName = klass.getSimpleName();
            if(!klass.getPackage().getName().contains("test_entities")) {
                collNames.putIfAbsent(klassName, klass);
            }
    
            for(Field field : klass.getDeclaredFields()) {
                if(field.isAnnotationPresent(Id.class)) {
                    idFields.putIfAbsent(klassName, field.getName());
                    break;
                }
            }
        });

        if (nosqlite.Database.useWatchers) watchCollections(collNames);
        if (nosqlite.Database.useBrowser) initBrowser(collNames, idFields);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(express != null) express.stop();
        }));
    }

    /**
     * Embedded server to browse collections locally
     */
    private static void initBrowser(Map<String, Class<?>> collNames, Map<String, String> idFields) {
        express = new Express();

        express.get("/rest/docs", (req, res) -> res.send(BrowserDocumentation.docs));
        
        express.get("/rest/collNames", (req, res) -> res.json(collNames.keySet()));

        express.get("/rest/:coll", (req, res) -> {
            String coll = req.params("coll");
            Map data = new HashMap();
            data.put(idFields.get(coll), collection(coll).findAsJson());
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
                Express.log.info("Could not convert JSON.", e);
                res.status(500).end(e.getMessage());
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
    
        JavalinUtil.startingServer = false;
        express.raw().start(9595);
        JavalinUtil.startingServer = true;
        Express.log.info("Browse collections at http://localhost:" + 9595);
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
        
        app.ws("/watch-collections/:coll", ws -> {
            ws.onConnect(ctx -> {
                String coll = ctx.pathParam("coll");
                clientsMap.putIfAbsent(coll, new CopyOnWriteArrayList<>());
                clientsMap.get(coll).add(ctx);
    
                collection(coll).watch(watchData ->
                    clientsMap.get(coll).stream().filter(client -> client.session.isOpen())
                        .forEach(client -> client.send(watchData)));
            });
            
            ws.onClose(ctx -> clientsMap.get(ctx.pathParam("coll")).remove(ctx));
        });
    }

}
