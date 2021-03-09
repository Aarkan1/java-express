package express.http;

import io.javalin.http.*;
import io.javalin.plugin.json.JavalinJson;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johan Wirén
 *
 * The Response object wrapping Javalins Context
 */
public class Response {
    private final Context ctx;

    public Response(Context ctx) {
        this.ctx = ctx;
    }

    public Context ctx() { return ctx; }
    public Response send(String text) {
        type("text/html");
        ctx.result(text);
        return this; }
    public Response send(Object obj) { ctx.result(JavalinJson.toJson(obj)); return this; }
    public Response send(InputStream stream) { ctx.result(stream); return this; }
    public Response send(byte[] bytes) { ctx.result(bytes); return this; }
    public Response json(Object json) {
        if(json == null) send("null");
        else ctx.json(json);
        return this;
    }

    public Response append(String name, String value) { ctx.header(name, value); return this; }
    public Response attachment() {
        ctx.header("Content-Disposition", "attachment");
        return this;
    }
    public Response attachment(String path) {
        String filename = path.substring(path.lastIndexOf(path.contains("/") ? '/' : '\\') + 1);
        String extension = path.substring((path.lastIndexOf(".") + 1));
        ctx.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        type(extension);
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
    public Response cookie(String name, String value) { ctx.cookie(name, value); return this; }
    public Response cookie(Cookie cookie) { ctx.cookie(cookie); return this; }
    public Response clearCookie(String name, String path) { ctx.removeCookie(name, path); return this; }
    public String get(String key) { return ctx.header(key); }
    public String links() { return ctx.header("Link"); }
    public Response links(String next, String last) { ctx.header("Link", String.format("<%s>; rel=\"next\",\n<%s>; rel=\"last\"", next, last)); return this; }
    public String location() { return ctx.header("Location"); }
    public Response location(String location) { ctx.header("Location", location); return this; }
    public Response redirect(String location) { ctx.redirect(location); return this; }
    public Response redirect(int httpStatusCode, String location) { ctx.redirect(location, httpStatusCode); return this; }
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

    public void end() { end(null); }

    public void end(String message) {
        switch (status()) {
            case 302:
                throw new RedirectResponse();
            case 400:
                throw new BadRequestResponse(message == null ? "BadRequest" : message);
            case 401:
                throw new UnauthorizedResponse(message == null ? "Unauthorized" : message);
            case 403:
                throw new ForbiddenResponse(message == null ? "Forbidden" : message);
            case 404:
                throw new NotFoundResponse(message == null ? "NotFound" : message);
            case 405:
                Map<String, String> details = new HashMap<>();
                details.put("method", ctx.method());
                throw new MethodNotAllowedResponse(message == null ? "MethodNotAllowed" : message, details);
            case 409:
                throw new ConflictResponse(message == null ? "Conflict" : message);
            case 410:
                throw new GoneResponse(message == null ? "Gone" : message);
            case 500:
                throw new InternalServerErrorResponse(message == null ? "InternalServerError" : message);
            case 502:
                throw new BadGatewayResponse(message == null ? "BadGateway" : message);
            case 503:
                throw new ServiceUnavailableResponse(message == null ? "ServiceUnavailable" : message);
            case 504:
                throw new GatewayTimeoutResponse(message == null ? "GatewayTimeout" : message);
            default:
                Map<String, String> det = new HashMap<>();
                throw new HttpResponseException(status(), message == null ? "NoResponse" : message, det);
        }
    }

//    public Response headersSent() {  }
//    public Response format() {  }
//    public Response vary() {  }
}
