package service;

import dataaccess.*;
import dataaccess.gamedaos.GameDao;
import dataaccess.userdaos.UserDao;
import model.GameData;
import request.JoinGameRequest;
import result.JoinGameResult;

import static authentication.ReturnAuth.returnAuth;

public class JoinGameService {
  private final GameDao gameDao;
  private final UserDao userDao;


  public JoinGameService(GameDao gameDao, UserDao userDao) {
    this.gameDao = gameDao;
    this.userDao = userDao;
  }
  public JoinGameResult joinGame(JoinGameRequest request) {
    String username = returnAuth(request.authToken());
    if (username == null) {
      return new JoinGameResult(null, null, null, null,"error: invalid auth token");
    }

    if (!"black".equalsIgnoreCase(request.playerColor()) &&
            !"white".equalsIgnoreCase(request.playerColor()) &&
            !"observer".equalsIgnoreCase(request.playerColor())) {
      return new JoinGameResult(null, null, null, null,"error: invalid player color");
    }

    try {
      GameData game = gameDao.getGame(request.gameID());
      if (!request.playerColor().equalsIgnoreCase("observer")) {
        gameDao.addColor(request.gameID(), request.playerColor(), username);
        System.out.println("Player " + request.playerColor() + " has joined game " + request.gameID());
      } else {
        // Handle joining as an observer
        System.out.println("Player " + username + " has joined game " + request.gameID() + " as an observer");
      }
      game = gameDao.getGame(request.gameID());
      return new JoinGameResult(game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), null);
    } catch (DataAccessException e) {
      if (e.getMessage().equals("Color already exists")) {
        return new JoinGameResult(null, null, null,null,"error: color already exists");
      }
      return new JoinGameResult(null,null,null,null,"error: bad game ID");
    }
  }
}
