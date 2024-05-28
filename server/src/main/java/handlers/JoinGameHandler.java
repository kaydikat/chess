package handlers;

import com.google.gson.Gson;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
  private final Gson gson;
  private final JoinGameService joinGameService;

  public JoinGameHandler() {
    this.gson=new Gson();
    this.joinGameService=new JoinGameService();
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    String authToken = req.headers("authorization");
    JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
    request = new JoinGameRequest(authToken, request.playerColor(), request.gameID());
    JoinGameResult result = joinGameService.joinGame(request);
    if (result.message() == null) {
      res.status(200);
    } else if (result.message().equals("error: invalid auth token")) {
      res.status(401);
    } else if (result.message().equals("error: color already exists")) {
      res.status(403);
    }
    else if (result.message().equals("error: bad game ID") || result.message().equals("error: invalid player color") || result.message().equals("error: invalid player color specified")){
      res.status(400);
    }

    return gson.toJson(result);

  }
}
