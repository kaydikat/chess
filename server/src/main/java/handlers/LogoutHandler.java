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
  public LogoutHandler() {
    this.gson = new Gson();
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    LogoutRequest request = gson.fromJson(req.body(), LogoutRequest.class);
    LogoutService service = new LogoutService();
    LogoutResult result = service.logout(request);
    return gson.toJson(result);
  }
}
