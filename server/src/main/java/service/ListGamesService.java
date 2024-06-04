package service;

import dataaccess.DataAccessException;
import dataaccess.gamedaos.GameDao;
import model.GameData;
import request.ListGamesRequest;
import result.ListGamesResult;

import java.util.Collection;

import static authentication.CheckAuth.checkAuth;

public class ListGamesService {
  private final GameDao gameDao;


  public ListGamesService(GameDao gameDao) {
    this.gameDao = gameDao;
  }
  public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
    if (checkAuth(request.authToken())) {
      Collection<GameData> games = gameDao.listGames();
      return new ListGamesResult(games,null);
    } else {
      return new ListGamesResult(null,"error: invalid auth token");
    }
  }
}
