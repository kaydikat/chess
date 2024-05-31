package handlers;

import com.google.gson.Gson;
import dataaccess.GameDao;
import model.GameData;
import request.ListGamesRequest;
import result.ListGamesResult;
import service.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;

public class ListGamesHandler implements Route {
  private final Gson gson;
  private final GameDao gameDao;
  private final ListGamesService listGamesService;

  public ListGamesHandler(GameDao gameDao) {
    this.gson=new Gson();
    this.gameDao=gameDao;
    this.listGamesService=new ListGamesService(gameDao);
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    String authToken = req.headers("authorization");
    ListGamesRequest request = new ListGamesRequest(authToken);
    ListGamesResult result = listGamesService.listGames(request);
     if (result.message() != null) {
        res.status(401);
     } else {
        res.status(200);
     }
    return gson.toJson(result);

  }
}
