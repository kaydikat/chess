package server;

import dataaccess.*;
import handlers.*;
import service.ClearService;
import spark.*;

public class Server {

    static AuthDao authDao = AuthDaoSQL.getInstance();
    static UserDao userDao = UserDaoSQL.getInstance();
    static GameDao gameDao = GameDaoSQL.getInstance();


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

        Spark.delete("/db", (req, res) -> (new ClearHandler(authDao, userDao, gameDao).handleRequest(req.body())));
        Spark.post("/user", (req, res) ->
                (new RegisterHandler(authDao, userDao)).handle(req, res));

        Spark.post("/session", (req, res) ->
                (new LoginHandler(authDao, userDao)).handle(req, res));
        Spark.delete("/session", new LogoutHandler(authDao));
        Spark.post("/game", (req, res) ->
                (new CreateGameHandler(gameDao)).handle(req, res));
        Spark.get("/game", (req, res) ->
                (new ListGamesHandler(gameDao)).handle(req, res));
        Spark.put("/game", (req, res) ->
                (new JoinGameHandler(gameDao, userDao)).handle(req, res));
    }
}
