package express.http;

import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJson;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author Johan Wirén
 */
public class Response {
    private final Context ctx;

    public Response(Context ctx) {
        this.ctx = ctx;
    }

    public Context ctx() { return ctx; }
    public Response send(String text) { ctx.result(text); return this; }
    public Response send(Object obj) { ctx.result(JavalinJson.toJson(obj)); return this; }
    public Response send(InputStream stream) { ctx.result(stream); return this; }
    public Response send(byte[] bytes) { ctx.result(bytes); return this; }
    public Response json(Object json) { ctx.json(json); return this; }

    public Response append(String name, String value) { ctx.header(name, value); return this; }
    public Response attachment() {
        ctx.header("Content-Disposition", "attachment");
        return this;
    }
    public Response attachment(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        String extension = path.substring(path.lastIndexOf(".") + 1);
        ctx.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        type(extension);
        return this;
    }
    public Response cookie(String name, String value) { ctx.cookie(name, value); return this; }
    public Response cookie(Cookie cookie) { ctx.cookie(cookie); return this; }
    public Response clearCookie(String name, String path) { ctx.removeCookie(name, path); return this; }
    public Response end() { ctx.result(""); return this; }
    public String get() { return ctx.contentType(); }
    public String links() { return ctx.header("Link"); }
    public Response links(String next, String last) { ctx.header("Link", String.format("<%s>; rel=\"next\",\n<%s>; rel=\"last\"", next, last)); return this; }
    public String location() { return ctx.header("Location"); }
    public Response location(String location) { ctx.header("Location", location); return this; }
    public Response redirect(String location) { ctx.redirect(location); return this; }
    public Response redirect(String location, int httpStatusCode) { ctx.redirect(location, httpStatusCode); return this; }
    public Response render(String filePath, Map<String, Object> model) { ctx.render(filePath, model); return this; }
    public Response sendStatus(int statusCode) { status(statusCode).send(Status.valueOf(statusCode).getDescription()); return this; }
    public Response set(String name, String value) { ctx.header(name, value); return this; }
    public Response status(int statusCode) { ctx.status(statusCode); return this; }
    public int status() { return ctx.status(); }
    public Response type(String contentType) {
        if(contentType.startsWith(".")) contentType = contentType.replaceFirst("\\.", "");
        MediaType fromExtension = MediaType.getByExtension(contentType);
        ctx.contentType(fromExtension != null ? fromExtension.getMIME() : contentType);
        return this;
    }
    public Response download(Path path) { attachment(path.toString()); sendFile(path); return this; }

    public Response sendFile(Path path) {
        MediaType fromExtension = MediaType.getByExtension(
                path.toString().substring(path.toString().lastIndexOf(".") + 1)
        );
        ctx.contentType(fromExtension != null ? fromExtension.getMIME() : "text/plain");
        try {
            ctx.result(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

//    public Response headersSent() {  }
//    public Response format() {  }
//    public Response locals() {  }
//    public Response vary() {  }
}
