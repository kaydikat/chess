package service;

import dataaccess.DataAccessException;
import dataaccess.GameDaoInMemory;
import request.ListGamesRequest;
import result.ListGamesResult;

import static authentication.CheckAuth.checkAuth;

public class ListGamesService {
  private final GameDaoInMemory gameDao;


  public ListGamesService() {
    this.gameDao = GameDaoInMemory.getInstance();
  }
  public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
    if (request == null) {
      return new ListGamesResult(null);
    }
    if (checkAuth(request.authToken())) {
        return new ListGamesResult(gameDao.listGames());
        } else {
        return new ListGamesResult(null);
    }
  }
}
