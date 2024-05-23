package handlers;

import com.google.gson.Gson;
import service.ClearService;
import request.ClearRequest;
import result.ClearResult;

public class ClearHandler {
  private Gson gson;

  public ClearHandler() {
    this.gson = new Gson();
  }

  public String handleRequest(String reqData) {
    ClearRequest request = gson.fromJson(reqData, ClearRequest.class);
    ClearService service = new ClearService();
    ClearResult result = service.clear(request);
    return gson.toJson(result);
  }
}
