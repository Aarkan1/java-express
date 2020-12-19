package express;

import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Express app = new Express();

        app.get("/json", (req, res) -> {
           res.json(Map.of("status", "ok"));
        });

        app.post("/", (req, res) -> {
           res.send("Hello dude");
        });

        app.useStatic(Paths.get("src/main/java/www"));

        app.get("*", (req, res) -> {
            res.sendFile(Paths.get("src/main/java/www/index.html"));
        });

        app.listen(3000);
    }
}
