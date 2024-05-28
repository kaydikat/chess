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
    public CreateGameHandler() {
      this.gson = new Gson();
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
      CreateGameService service = new CreateGameService();
      CreateGameResult result = service.createGame(request);
      return gson.toJson(result);
    }
}
