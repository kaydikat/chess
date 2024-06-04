package handlers;

import com.google.gson.Gson;
import dataaccess.authDaos.AuthDao;
import service.LogoutService;
import request.LogoutRequest;
import result.LogoutResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
  private final Gson gson;
  private final AuthDao authDao;
  private final LogoutService logoutService;
  public LogoutHandler(AuthDao authDao) {
    this.gson = new Gson();
    this.authDao = authDao;
    this.logoutService = new LogoutService(authDao);
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    String authToken = req.headers("authorization");
    LogoutRequest request = new LogoutRequest(authToken);
    LogoutResult result = logoutService.logout(request);
    if (result.message() == null) {
      res.status(200);
    } else {
      res.status(401);
    }

    return gson.toJson(result);
  }
}
