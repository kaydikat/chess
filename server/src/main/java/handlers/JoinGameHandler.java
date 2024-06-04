package handlers;

import com.google.gson.Gson;
import dataaccess.gamedaos.GameDao;
import dataaccess.userdaos.UserDao;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
  private final Gson gson;
  private final GameDao gameDao;
  private final UserDao userDao;
  private final JoinGameService joinGameService;

  public JoinGameHandler(GameDao gameDao, UserDao userDao) {
    this.gson=new Gson();
    this.gameDao=gameDao;
    this.userDao=userDao;
    this.joinGameService=new JoinGameService(gameDao, userDao);
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
