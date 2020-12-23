package express.http;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.util.List;
import java.util.Map;

/**
 * @author Johan Wirén
 *
 * The Request object wrapping Javalins Context
 */
public class Request {
    private final Context ctx;

    public Request(Context ctx) {
        this.ctx = ctx;
    }

    public Context ctx() { return ctx; }
    public String baseUrl() { return ctx.endpointHandlerPath(); }
    public Map body() { return ctx.bodyAsClass(Map.class); }
    public <T> T body(Class<T> klass) { return ctx.bodyAsClass(klass); }
    public byte[] bodyAsBytes() { return ctx.bodyAsBytes(); }
    public String cookie(String name) { return ctx.cookie(name); }
    public Map<String, String> cookies() { return ctx.cookieMap(); }
    public String host() { return ctx.host(); }
    public String hostname() { return ctx.host() + ":" + ctx.port(); }
    public String ip() { return ctx.ip(); }
    public String method() { return ctx.method(); }
    public String originalUrl() { return ctx.fullUrl(); }
    public Map<String, String> params() { return ctx.pathParamMap(); }
    public String params(String key) { return ctx.pathParam(key); }
    public <T> T params(String key, Class<T> klass) { return (T) ctx.pathParam(key, klass); }
    public String path() { return ctx.path(); }
    public String protocol() { return ctx.protocol(); }
    public Map<String, List<String>> query() { return ctx.queryParamMap(); }
    public String query(String key) { return ctx.queryParam(key); }
    public <T> T query(String key, Class<T> klass) { return (T) ctx.queryParam(key, klass); }
    public Map<String, List<String>> formData() { return ctx.formParamMap(); }
    public String formData(String key) { return ctx.formParam(key); }
    public <T> T formData(String key, Class<T> klass) { return (T) ctx.formParam(key, klass); }
    public UploadedFile formDataFile(String key) { return ctx.uploadedFile(key); }
    public List<UploadedFile> formDataFiles(String key) { return ctx.uploadedFiles(key); }
    public boolean secure() { return protocol().equals("https"); }
    public String[] subdomains() {
        String[] domains = ctx.url().split("\\.");
        return new String[]{ domains[0], domains[1] };
    }
    public boolean xhr() { return ctx.header("X-Requested-With").equals("XMLHttpRequest"); }
    public boolean accepts(String accept) { return ctx.header("Accept").equals(accept); }
    @Deprecated public String acceptsCharsets() { return ctx.header("Accept-Charset"); }
    @Deprecated public String acceptsEncodings() { return ctx.header("Accept-Encoding"); }
    @Deprecated public String acceptsLanguages() { return ctx.header("Accept-Language"); }
    public String get(String header) { return ctx.header(header); }
    public String header(String field) { return ctx.header(field); }
    public boolean is(String contentType) { return ctx.contentType().equals(contentType); }
    public <T> Map<String, T> session() { return ctx.sessionAttributeMap(); }
    public <T> T session(String key) { return ctx.sessionAttribute(key); }
    public void session(String key, Object value) { ctx.sessionAttribute(key, value); }

//    public void route() {  }
//    public void stale() {  }
//    public void signedCookies() {  }
//    public void ips() {  }
//    public void fresh() {  }
//    public void range() {  }
}

