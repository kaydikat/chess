package handlers;

import com.google.gson.Gson;
import dataaccess.gameDaos.GameDao;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    private final Gson gson;
    private final GameDao gameDao;
    private final CreateGameService createGameService;
    public CreateGameHandler(GameDao gameDao) {
      this.gson = new Gson();
      this.gameDao = gameDao;
      this.createGameService = new CreateGameService(gameDao);
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      String authToken = req.headers("authorization");
      CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
      request = new CreateGameRequest(authToken, request.gameName());
      CreateGameResult result = createGameService.createGame(request);
      if (result.gameID() == null) {
        res.status(401);
      }
      return gson.toJson(result);
    }
}
