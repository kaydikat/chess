package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import service.LoginService;
import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
  private final Gson gson;
  public RegisterHandler() {
    this.gson = new Gson();
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    RegisterRequest request1 = gson.fromJson(request.body(), RegisterRequest.class);
    RegisterService service = new RegisterService();
    RegisterResult result = service.register(request1);
    return gson.toJson(result);
  }
}
