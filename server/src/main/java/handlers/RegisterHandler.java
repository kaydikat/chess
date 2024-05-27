package handlers;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;
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
  public Object handle(Request req, Response res) throws Exception {
    RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
    RegisterService service = new RegisterService();
    RegisterResult result = service.register(request);
    return gson.toJson(result);
  }
}
