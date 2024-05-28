package handlers;

import com.google.gson.Gson;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    private final Gson gson;
    private final CreateGameService createGameService;
    public CreateGameHandler() {
      this.gson = new Gson();
      this.createGameService = new CreateGameService();
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      String authToken = req.headers("authorization");
      CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
      request = new CreateGameRequest(authToken, request.gameName());
      CreateGameResult result = createGameService.createGame(request);
      return gson.toJson(result);
    }
}
