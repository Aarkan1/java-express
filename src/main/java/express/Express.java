package express;

import express.database.Database;
import express.http.HttpContextHandler;
import express.http.Request;
import express.http.Response;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.sse.SseClient;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


/**
 * The Express layer on top of Javalin
 *
 * @author Johan Wirén
 */
public class Express {
    private final Javalin app;
    private final Map<String, Object> locals = new ConcurrentHashMap<>();
    
    public static Logger log = LoggerFactory.getLogger(Express.class);
 
    public Express() {
        this(null);
    }

    public Express(Consumer<JavalinConfig> config) {
        JavalinUtil.disableJavalinLogger();
        if(config != null) app = Javalin.create(config);
        else app = Javalin.create();
        app.config.showJavalinBanner = false;
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
        
        app.events(event -> event.serverStartFailed(() ->
            Express.log.error("Failed to start Express")));
    }

    /**
     * @return The underlying Javalin instance
     */
    public Javalin raw() {
        return app;
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
    public Express useStaticFallback(String url, Path filePath) {
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
    public Express useStaticFallback(String url, String filePath, Location location) {
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

    public <T> T locals(String name) { return (T) locals.get(name); }

    public Express locals(String name, Object obj) {
        locals.put(name, obj);
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

    public Express set(String name, Object obj) {
        locals.put(name, obj);
        return this;
    }

    public <T> T get(String name) { return (T) locals.get(name); }

    public void listen() {
        listen(80);
    }

    public void listen(int port) {
        if (nosqlite.Database.useWatchers || nosqlite.Database.useBrowser) {
            new Database(this);
        }
        app.start(port);
        JavalinUtil.reEnableJavalinLogger();
        Javalin.log.info("Server listening on http://localhost:" + port);
    }

    public void listen(String hostname, int port) {
        if (nosqlite.Database.useWatchers || nosqlite.Database.useBrowser) {
            new Database(this);
        }
        app.start(hostname, port);
        JavalinUtil.reEnableJavalinLogger();
        Javalin.log.info("Server listening on http://" + hostname + ":" + port);
    }
    
    public void collectionBrowserListen(int port) {
        app.start(port);
    }

    public void stop() {
        JavalinUtil.disableJavalinLogger();
        app.stop();
        JavalinUtil.reEnableJavalinLogger();
        Express.log.info("Express has stopped");
    }

//    public Express route(String path, EndpointGroup endpointGroup) {
//        app.routes(() -> path(path, endpointGroup));
//        return this;
//    }

//    public Express param() { return this; }
//    public Express path() { return this; }
//    public Express render() { return this; }

}
