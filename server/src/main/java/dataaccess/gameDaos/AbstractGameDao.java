package dataaccess.gameDaos;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.gameDaos.GameDao;
import model.GameData;

import java.util.Collection;
import java.util.Random;

public abstract class AbstractGameDao implements GameDao {
  protected final Gson gson = new Gson();
  protected final Random random = new Random();

  @Override
  public GameData createGame(String gameName) throws DataAccessException {
    ChessGame newChessGame = new ChessGame();
    GameData game = new GameData(null, null, null, gameName, newChessGame);
    Integer gameID = createGameID(game);
    game = new GameData(gameID, null, null, gameName, newChessGame);
    return game;
  }

  @Override
  public void addColor(Integer gameID, String playerColor, String username) throws DataAccessException {
    GameData game = getGame(gameID);
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

    updateGameColor(gameID, whiteUsername, blackUsername);
  }

  protected abstract void updateGameColor(Integer gameID, String whiteUsername, String blackUsername) throws DataAccessException;

  protected String serializeGame(GameData game) {
    return gson.toJson(game);
  }

  protected GameData deserializeGame(String gameText) {
    return gson.fromJson(gameText, GameData.class);
  }
}
