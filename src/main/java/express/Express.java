package express;

import express.database.CollectionOptions;
import express.database.Database;
import express.http.HttpContextHandler;
import express.http.Request;
import express.http.Response;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.sse.SseClient;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class Express {
    private final Javalin app;
    private final Map<String, Object> locals = new ConcurrentHashMap<>();
    private Database db;

    public Express() {
        app = Javalin.create();
        app.config.showJavalinBanner = false;
    }

    public Express(Consumer<JavalinConfig> config) {
        app = Javalin.create(config);
        app.config.showJavalinBanner = false;

        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    /**
     * @return The underlying Javalin instance
     */
    public Javalin raw() {
        return app;
    }

    /**
     * Enable the embedded document database
     *
     * @return
     */
    public Express enableCollections() {
        db = new Database(this);
        return this;
    }

    /**
     * Enable the embedded document database
     *
     * @return
     */
    public Express enableCollections(String dbPath) {
        db = new Database(dbPath, this);
        return this;
    }

    /**
     * Enable the embedded document database
     *
     * @return
     */
    public Express enableCollections(CollectionOptions... options) {
        db = new Database(this, options);
        return this;
    }

    /**
     * Enable the embedded document database
     *
     * @return
     */
    public Express enableCollections(String dbPath, CollectionOptions... options) {
        db = new Database(dbPath, this, options);
        return this;
    }

    public Express watchCollections() {
        Database.watchCollections();
        return this;
    }

    public Express useStatic(Path path) {
        useStatic(path.toString(), Location.EXTERNAL);
        return this;
    }

    public Express useStatic(String path, Location location) {
        app.config.addStaticFiles(path, location);
        return this;
    }

    /**
     * route 404 to file
     *
     * @param url
     * @param filePath
     * @return
     */
    public Express useSinglePageApp(String url, Path filePath) {
        app.config.addSinglePageRoot(url, filePath.toString(), Location.EXTERNAL);
        return this;
    }

    /**
     * route 404 to file with Location.CLASSPATH or Location.EXTERNAL
     *
     * @param url
     * @param filePath
     * @return
     */
    public Express useSinglePageApp(String url, String filePath, Location location) {
        app.config.addSinglePageRoot(url, filePath, location);
        return this;
    }

    /**
     * Enable CORS for all origins
     *
     * @return The Express instance
     */
    public Express cors() {
        app.config.enableCorsForAllOrigins();
        return this;
    }

    /**
     * Enable CORS for origin
     *
     * @param origin
     * @return The Express instance
     */
    public Express cors(String origin) {
        app.config.enableCorsForOrigin(origin);
        return this;
    }

    /**
     * Enable extensive Javalin dev logging
     *
     * @return The Express instance
     */
    public Express devLogging() {
        app.config.enableDevLogging();
        return this;
    }

    public Express put(String path, HttpContextHandler handler) {
        app.put(path, ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express get(String path, HttpContextHandler handler) {
        app.get(path, ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express post(String path, HttpContextHandler handler) {
        app.post(path, ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express patch(String path, HttpContextHandler handler) {
        app.patch(path, ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express delete(String path, HttpContextHandler handler) {
        app.delete(path, ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express use(HttpContextHandler handler) {
        app.before(ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express use(String path, HttpContextHandler handler) {
        app.before(path, ctx -> handler.handle(new Request(ctx), new Response(ctx)));
        return this;
    }

    public Express all(String path, HttpContextHandler handler) {
        get(path, handler);
        post(path, handler);
        put(path, handler);
        patch(path, handler);
        delete(path, handler);
        return this;
    }

    public Express sse(String path, Consumer<SseClient> client) {
        app.sse(path, client);
        return this;
    }

    public Express ws(String path, Consumer<WsHandler> ws) {
        app.ws(path, ws);
        return this;
    }

    public List<Object> locals() {
        return (List<Object>) locals.values();
    }

    public Object locals(String key) {
        return locals.get(key);
    }

    public Express locals(String key, Object obj) {
        locals.put(key, obj);
        return this;
    }

    public Express enable(String name) {
        locals.put(name, true);
        return this;
    }
    public Express disable(String name) {
        locals.put(name, false);
        return this;
    }
    public boolean enabled(String name) { return ((boolean) locals.get(name) == true); }
    public boolean disabled(String name) { return ((boolean) locals.get(name) == false); }

    public Express param() { return this; }
    public Express route() { return this; }
    public Express set(String name, Object obj) {
        locals.put(name, obj);
        return this;
    }

    public Object get(String name) { return locals.get(name); }

    public void listen() {
        listen(8080);
    }

    public void listen(int port) {
        app.start(port);
    }

    public void listen(String hostname, int port) {
        app.start(hostname, port);
    }

    public void stop() {
        app.stop();
    }


//    public Express mountpath() { return this; }
//    public Express router() { return this; }
//    public Express mount() { return this; }
//    public Express engine() { return this; }
//    public Express path() { return this; }
//    public Express render() { return this; }

}
