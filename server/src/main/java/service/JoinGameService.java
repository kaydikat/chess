package service;

import dataaccess.DataAccessException;
import dataaccess.GameDaoInMemory;
import dataaccess.UserDaoInMemory;
import model.GameData;
import request.JoinGameRequest;
import request.LoginRequest;
import result.JoinGameResult;

import static authentication.CheckAuth.checkAuth;
import static authentication.ReturnAuth.returnAuth;

public class JoinGameService {
  private final GameDaoInMemory gameDao;
  private final UserDaoInMemory userDao;


  public JoinGameService() {
    this.gameDao = GameDaoInMemory.getInstance();
    this.userDao = UserDaoInMemory.getInstance();
  }
  public JoinGameResult joinGame(JoinGameRequest request) {
    String username = returnAuth(request.authToken());
    if (username == null) {
      return new JoinGameResult("error: invalid auth token");
    }

    if (!"black".equalsIgnoreCase(request.playerColor()) && !"white".equalsIgnoreCase(request.playerColor())) {
      return new JoinGameResult("error: invalid player color");
    }

    try {
      GameData game = gameDao.getGame(request.gameID());
      if (request.playerColor() != null) {
        gameDao.addColor(request.gameID(), request.playerColor(), username);
        System.out.println("Player " + request.playerColor() + " has joined game " + request.gameID());
      } else {
        // Handle joining as an observer
        System.out.println("Player " + username + " has joined game " + request.gameID() + " as an observer");
      }
      return new JoinGameResult(null);
    } catch (DataAccessException e) {
      if (e.getMessage().equals("Color already exists")) {
        return new JoinGameResult("error: color already exists");
      }
      return new JoinGameResult("error: bad game ID");
    }
  }
}