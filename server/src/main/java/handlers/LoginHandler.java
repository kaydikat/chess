package handlers;

import com.google.gson.Gson;
import dataaccess.authdaos.AuthDao;
import dataaccess.userdaos.UserDao;
import service.LoginService;
import request.LoginRequest;
import result.LoginResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
  private final Gson gson;
  private final AuthDao authDao;
  private final UserDao userDao;
  public LoginHandler(AuthDao authDao, UserDao userDao) {
    this.gson = new Gson();
    this.authDao = authDao;
    this.userDao = userDao;
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
    LoginService service = new LoginService(authDao, userDao);
    LoginResult result = service.login(request);
    if (result.username() == null || result.authToken() == null) {
      res.status(401);
    } else {
      res.status(200);
    }
    return gson.toJson(result);
  }
}