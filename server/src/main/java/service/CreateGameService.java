package service;

import dataaccess.DataAccessException;
import dataaccess.GameDaoInMemory;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;

import static authentication.CheckAuth.checkAuth;

public class CreateGameService {
  private final GameDaoInMemory gameDao;


  public CreateGameService() {
    this.gameDao = GameDaoInMemory.getInstance();
  }
  public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
    if (request == null) {
      return new CreateGameResult(null);
    }
    if (checkAuth(request.authToken())) {
      GameData game = gameDao.createGame(request.gameName());
      Integer gameID = gameDao.createGameID(game);
      return new CreateGameResult(gameID);
    } else {
      return new CreateGameResult(null);
    }
  }
}
