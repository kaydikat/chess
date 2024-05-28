package handlers;

import com.google.gson.Gson;
import service.LogoutService;
import request.LogoutRequest;
import result.LogoutResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
  private final Gson gson;
  private final LogoutService logoutService;
  public LogoutHandler() {
    this.gson = new Gson();
    this.logoutService = new LogoutService();
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    String authToken = req.headers("authorization");
    LogoutRequest request = new LogoutRequest(authToken);
    LogoutResult result = logoutService.logout(request);

    return gson.toJson(result);
  }
}
