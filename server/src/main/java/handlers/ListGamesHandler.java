package handlers;

import com.google.gson.Gson;
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
  private final ListGamesService listGamesService;

  public ListGamesHandler() {
    this.gson=new Gson();
    this.listGamesService=new ListGamesService();
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    String authToken = req.queryParams("authToken");
    ListGamesRequest request = new ListGamesRequest(authToken);
    ListGamesResult result = listGamesService.listGames(request);

    res.type("application/json");
    return gson.toJson(result);

  }
}
