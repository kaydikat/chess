package dataaccess.gamedaos;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GameDaoSQL extends AbstractGameDao {
  private static GameDaoSQL instance;

  private GameDaoSQL() {}

  public static GameDaoSQL getInstance() {
    if (instance == null) {
      instance = new GameDaoSQL();
    }
    return instance;
  }

  @Override
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

  @Override
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

  @Override
  public Collection<GameData> listGames() throws DataAccessException {
    var games = new ArrayList<GameData>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            int id = rs.getInt("gameID");
            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            String gameText = rs.getString("game");
            ChessGame game = deserializeGame(gameText).game();
            games.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to list games: " + e.getMessage());
    }
    return games;
  }

  @Override
  protected void updateGameColor(Integer gameID, String whiteUsername, String blackUsername) throws DataAccessException {
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

  @Override
  public String getPlayerColor(Integer gameID, String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT whiteUsername, blackUsername FROM game WHERE gameID=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setInt(1, gameID);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
            String whiteUsername=rs.getString("whiteUsername");
            String blackUsername=rs.getString("blackUsername");
            if (whiteUsername == null && blackUsername == null) {
              return "observer";
            } else if (whiteUsername.equals(username)) {
              return "white";
            } else if (blackUsername.equals(username)) {
              return "black";
            } else {
              return "observer";
            }
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error getting player color: " + e.getMessage());
    }
    return null;
  }
  @Override
  public void removeColor(Integer gameID, String playerColor) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "UPDATE game SET whiteUsername=NULL WHERE gameID=?";
      if (playerColor.equals("black")) {
        statement = "UPDATE game SET blackUsername=NULL WHERE gameID=?";
      }
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setInt(1, gameID);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error removing color: " + e.getMessage());
    }
  }

  @Override
  public void updateGame(Integer gameID, ChessGame game) throws DataAccessException {
    String gameText = serializeGame(new GameData(gameID, null, null, null, game));
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "UPDATE game SET game=? WHERE gameID=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, gameText);
        preparedStatement.setInt(2, gameID);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error updating game: " + e.getMessage());
    }
  }

  @Override
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
}

