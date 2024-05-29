package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameDaoInMemory {
  private static GameDaoInMemory instance;
  private final Map<Integer, GameData> games = new HashMap<>();

  private GameDaoInMemory() {}

  public static GameDaoInMemory getInstance() {
    if (instance == null) {
      instance = new GameDaoInMemory();
    }
    return instance;
  }
  private final Random random = new Random();

  public Integer createGameID(GameData game) {
    int newGameID = 1000 + random.nextInt(9000);
    games.put(newGameID, game);
    return newGameID;
  }

  public GameData createGame(String gameName) throws DataAccessException {
    ChessGame newChessGame = new ChessGame();
    GameData game = new GameData(null, null, null, gameName, newChessGame);
    Integer gameID = createGameID(game);
    game = new GameData(gameID, null, null, gameName, newChessGame);
    return game;
  }
  public GameData getGame(Integer gameID) throws DataAccessException {
    return games.get(gameID);
  }

  public Collection<GameData> listGames() {
    System.out.println("game values " + games.values());
    return games.values();
  }

  public void updateGame(GameData game) throws DataAccessException {
    if (!games.containsKey(game.gameID())) {
      throw new DataAccessException("Game not found");
    }
    games.put(game.gameID(), game);
  }

  public void addColor(Integer gameID, String playerColor, String username) throws DataAccessException {
    GameData game = games.get(gameID);
    if (game == null) {
      throw new DataAccessException("Game not found");
    }

    String whiteUsername = game.whiteUsername();
    String blackUsername = game.blackUsername();

    if ("WHITE".equalsIgnoreCase(playerColor)) {
      if (whiteUsername != null) {
        throw new DataAccessException("Color already exists");
      }
      whiteUsername = username;
    } else if ("BLACK".equalsIgnoreCase(playerColor)) {
      if (blackUsername != null) {
        throw new DataAccessException("Color already exists");
      }
      blackUsername = username;
    } else if (playerColor == null || playerColor.isEmpty()) {
      System.out.println("Observer joined the game");
    } else {
      throw new DataAccessException("Invalid player color specified");
    }

    game = new GameData(gameID, whiteUsername, blackUsername, game.gameName(), game.game());
    games.put(gameID, game);
  }

  public void clear() {
    games.clear();
  }
}
