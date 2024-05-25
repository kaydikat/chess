package server;

import handlers.ClearHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createEndpoints();


        Spark.awaitInitialization();
        System.out.println("Server started on port: " + Spark.port());
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private static void createEndpoints() {
        Spark.get("/hello", (req, res) -> "Hello!");
        Spark.delete("/clear", (req, res) -> (new ClearHandler().handleRequest(req.body())));
    }

}
