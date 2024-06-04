package handlers;

import com.google.gson.Gson;
import dataaccess.authDaos.AuthDao;
import dataaccess.gameDaos.GameDao;
import dataaccess.userDaos.UserDao;
import service.ClearService;
import request.ClearRequest;
import result.ClearResult;

public class ClearHandler {
  private Gson gson;
  private final AuthDao authDao;
  private final UserDao userDao;
  private final GameDao gameDao;

  public ClearHandler(AuthDao authDao, UserDao userDao, GameDao gameDao) {
    this.gson = new Gson();
    this.authDao = authDao;
    this.userDao = userDao;
    this.gameDao = gameDao;
  }

  public String handleRequest(String reqData) {
    ClearRequest request = gson.fromJson(reqData, ClearRequest.class);
    ClearService service = new ClearService(authDao, userDao, gameDao);
    ClearResult result = service.clear(request);
    return gson.toJson(result);
  }
}
