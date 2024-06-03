package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.Collection;
import java.util.Random;

public class GameDaoSQL implements GameDao {
    private final Gson gson = new Gson();
    private final Random random = new Random();
    private static GameDaoSQL instance;
    private GameDaoSQL() {}
    public static GameDaoSQL getInstance() {
        if (instance == null) {
            instance = new GameDaoSQL();
        }
        return instance;
    }
  public Integer createGameID(GameData game) throws DataAccessException {
    int newGameID = 1000 + random.nextInt(9000);
    String gameText = serializeGame(game);
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO game (gameID, gameName, game) VALUES (?, ?, ?)";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setInt(1, newGameID);
        preparedStatement.setString(2, game.gameName());
        preparedStatement.setString(3, gameText);
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error creating gameID: " + e.getMessage());
    }
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
    return null;
  }

  public Collection<GameData> listGames() {
    return null;
  }

  public void addColor(Integer gameID, String playerColor, String username) throws DataAccessException {

  }

  public void clear() {

  }

  private String serializeGame(GameData game) {
    return gson.toJson(game);
  }

  private GameData deserializeGame(String gameText) {
    return gson.fromJson(gameText, GameData.class);
  }
}
