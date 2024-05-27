package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.LoginService;
import request.LoginRequest;
import result.LoginResult;
import spark.Route;

public class LoginHandler implements Route {
  private Gson gson = new Gson();
  public String handleLogin(String reqData) throws DataAccessException {
    LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
    LoginService service = new LoginService();
    LoginResult result = service.login(request);
    return gson.toJson(result);
  }
}