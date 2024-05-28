package service;

import dataaccess.DataAccessException;
import dataaccess.GameDaoInMemory;
import model.GameData;
import request.JoinGameRequest;
import result.JoinGameResult;

import static authentication.CheckAuth.checkAuth;

public class JoinGameService {
  private final GameDaoInMemory gameDao;


  public JoinGameService() {
    this.gameDao = GameDaoInMemory.getInstance();
  }
  public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException {
    if (checkAuth(request.authToken())) {
      GameData game = gameDao.getGame(request.gameID());
      gameDao.addColor(game, request.playerColor());
      System.out.println("Player " + request.playerColor() + " has joined game " + request.gameID());
      return new JoinGameResult();
    }
    return null;
  }
}
