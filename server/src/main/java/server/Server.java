package server;

import dataaccess.DataAccessException;
import handlers.*;
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
        Spark.delete("/db", (req, res) -> (new ClearHandler().handleRequest(req.body())));
        Spark.post("/user", (req, res) ->
                (new RegisterHandler()).handle(req, res));

        Spark.post("/session", (req, res) ->
                (new LoginHandler()).handle(req, res));
        Spark.delete("/session", (req, res) -> (new LogoutHandler()).handle(req, res));
        //Spark.get("/games", (req, res) -> new ListGamesHandler().handle(req, res));
    }

}
