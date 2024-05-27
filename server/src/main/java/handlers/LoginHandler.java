package handlers;

import com.google.gson.Gson;
import service.LoginService;
import request.LoginRequest;
import result.LoginResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
  private final Gson gson;
  public LoginHandler() {
    this.gson = new Gson();
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
    LoginService service = new LoginService();
    LoginResult result = service.login(request);
    return gson.toJson(result);
  }
}