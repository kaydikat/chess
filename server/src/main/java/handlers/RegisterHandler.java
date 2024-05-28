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
    if (result.message() != null && result.message().equals("error: user already exists")) {
      res.status(403);
    }
    else if (result.message() != null && result.message().equals("error: missing required field")) {
        res.status(400);
    } else {
      res.status(200);
    }
    return gson.toJson(result);
  }
}
