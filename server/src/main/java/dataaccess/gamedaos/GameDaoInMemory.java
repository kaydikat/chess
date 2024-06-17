package dataaccess.gamedaos;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDaoInMemory extends AbstractGameDao {
  private static GameDaoInMemory instance;
  private final Map<Integer, GameData> games = new HashMap<>();

  private GameDaoInMemory() {}

  public static GameDaoInMemory getInstance() {
    if (instance == null) {
      instance = new GameDaoInMemory();
    }
    return instance;
  }

  @Override
  public Integer createGameID(GameData game) {
    int newGameID = 1000 + random.nextInt(9000);
    games.put(newGameID, game);
    return newGameID;
  }

  @Override
  public GameData getGame(Integer gameID) throws DataAccessException {
    if (!games.containsKey(gameID)) {
      throw new DataAccessException("Game not found");
    }
    return games.get(gameID);
  }

  @Override
  public String getPlayerColor(Integer gameID, String username) {
    GameData game = games.get(gameID);
    if (game.whiteUsername().equals(username)) {
      return "white";
    } else if (game.blackUsername().equals(username)) {
      return "black";
    } else {
      return "observer";
    }
  }

  @Override
  public Collection<GameData> listGames() {
    return games.values();
  }

  @Override
  protected void updateGameColor(Integer gameID, String whiteUsername, String blackUsername) {
    GameData game = games.get(gameID);
    game = new GameData(gameID, whiteUsername, blackUsername, game.gameName(), game.game());
    games.put(gameID, game);
  }

  @Override
  public void removeColor(Integer gameID, String playerColor) {
    GameData game = games.get(gameID);
    if (playerColor.equals("white")) {
      game = new GameData(gameID, null, game.blackUsername(), game.gameName(), game.game());
    } else if (playerColor.equals("black")) {
      game = new GameData(gameID, game.whiteUsername(), null, game.gameName(), game.game());
    }
    games.put(gameID, game);
  }

  @Override
  public void updateGame(Integer gameID, ChessGame game) {
    GameData gameData = games.get(gameID);
    gameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
    games.put(gameID, gameData);
  }

  @Override
  public void clear() {
    games.clear();
  }
}