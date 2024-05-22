package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameDaoInMemory {
  private final Map<Integer, GameData> games = new HashMap<>();
  private final Random random = new Random();

  public void createGame(String gameName) throws DataAccessException {
    int newGameID = 1000 + random.nextInt(9000);

    GameData game = new GameData(newGameID, null, null, gameName, new ChessGame());
    games.put(newGameID, game);
  }
  public GameData getGame(Integer gameID) throws DataAccessException {
    GameData game = games.get(gameID);
    if (game == null) {
      throw new DataAccessException("Game not found");
    }
    return game;
  }

  public Collection<GameData> listGames() {
    return games.values();
  }

  public void updateGame(GameData game) throws DataAccessException {
    if (!games.containsKey(game.gameID())) {
      throw new DataAccessException("Game not found");
    }
    games.put(game.gameID(), game);
  }

  public void addColor(GameData game, String playerColor) throws DataAccessException {
    String whiteUsername = game.whiteUsername();
    String blackUsername = game.blackUsername();

    if ("WHITE".equals(playerColor)) {
      if (whiteUsername != null) {
        throw new DataAccessException("White player already exists");
      }
      whiteUsername = playerColor;
    } else if ("BLACK".equals(playerColor)) {
      if (blackUsername != null) {
        throw new DataAccessException("Black player already exists");
      }
      blackUsername = playerColor;
    } else {
      throw new DataAccessException("Invalid player color specified");
    }

    GameData updatedGame = new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game());
    games.put(game.gameID(), updatedGame);
  }

  public void clear() {
    games.clear();
  }
}
