package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
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
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setInt(1, gameID);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
           int id = rs.getInt("gameID");
           String whiteUsername = rs.getString("whiteUsername");
           String blackUsername = rs.getString("blackUsername");
           String gameName = rs.getString("gameName");
           String gameText = rs.getString("game");
           ChessGame game = deserializeGame(gameText).game();
           return new GameData(id, whiteUsername, blackUsername, gameName, game);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
    return null;
  }

  public Collection<GameData> listGames() throws DataAccessException {
    var games=new ArrayList<GameData>();
    try (var conn=DatabaseManager.getConnection()) {
      var statement="SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
      try (var ps=conn.prepareStatement(statement)) {
        try (var rs=ps.executeQuery()) {
          while (rs.next()) {
            int id=rs.getInt("gameID");
            String whiteUsername=rs.getString("whiteUsername");
            String blackUsername=rs.getString("blackUsername");
            String gameName=rs.getString("gameName");
            String gameText=rs.getString("game");
            ChessGame game=deserializeGame(gameText).game();
            games.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to list games: " + e.getMessage());
    }
    return games;
  }

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

    try (var conn = DatabaseManager.getConnection()) {
      var statement = "UPDATE game SET whiteUsername=?, blackUsername=? WHERE gameID=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, whiteUsername);
        preparedStatement.setString(2, blackUsername);
        preparedStatement.setInt(3, gameID);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error adding color: " + e.getMessage());
    }
  }

  public void clear() {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM game";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println("Error clearing auth table: " + e.getMessage());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private String serializeGame(GameData game) {
    return gson.toJson(game);
  }

  private GameData deserializeGame(String gameText) {
    return gson.fromJson(gameText, GameData.class);
  }
}
