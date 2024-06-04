package service;

import dataaccess.DataAccessException;
import dataaccess.gameDaos.GameDao;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

import static authentication.CheckAuth.checkAuth;

public class CreateGameService {
  private final GameDao gameDao;


  public CreateGameService(GameDao gameDao) {
    this.gameDao = gameDao;
  }
  public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
    if (checkAuth(request.authToken())) {
      GameData game = gameDao.createGame(request.gameName());
      return new CreateGameResult(game.gameID(), null);
    } else {
      return new CreateGameResult(null, "error: invalid authorization");
    }
  }
}
