package server;

import dataaccess.DataAccessException;
import handlers.ClearHandler;
import handlers.LoginHandler;
import handlers.RegisterHandler;
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


        Spark.post("/session", (req, res) -> {
            String reqData = req.body();

            LoginHandler loginHandler = new LoginHandler();
            String result = loginHandler.handleLogin(reqData);
            res.type("application/json");

            return result;
        });
    }
}
