package express.http;

import java.io.IOException;

/**
 * @author Johan Wirén
 */
@FunctionalInterface
public interface HttpContextHandler {
    void handle(Request req, Response res) throws IOException;
}
