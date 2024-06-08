package service;

import dataaccess.DataAccessException;
import dataaccess.gamedaos.GameDao;
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
    GameData game = gameDao.createGame(request.gameName());
    if (checkAuth(request.authToken())) {
      return new CreateGameResult(game.gameID(), game.game(), null);
    } else {
      return new CreateGameResult(null, null,"error: invalid authorization");
    }
  }
}
