package dataaccess.authdaos;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class AuthDaoSQL implements AuthDao {

  private static AuthDaoSQL instance;
  private AuthDaoSQL() {}

  public static AuthDaoSQL getInstance() {
    if (instance == null) {
      instance = new AuthDaoSQL();
    }
    return instance;
  }


  public void createAuth(String username) throws DataAccessException {
    String authToken = UUID.randomUUID().toString();
    AuthData auth = new AuthData(authToken, username);

    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, auth.authToken());
        preparedStatement.setString(2, auth.username());
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error creating auth: " + e.getMessage());
    }
  }

  public AuthData getAuth(String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM auth WHERE username=? ORDER BY id DESC LIMIT 1";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
            return new AuthData(rs.getString("authToken"), rs.getString("username"));
          } else {
            throw new DataAccessException("Auth not found");
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
  }
  public AuthData getAuthWithAuthToken(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
            return new AuthData(rs.getString("authToken"), rs.getString("username"));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
    return null;
  }

  public String getUsername(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username FROM auth WHERE authToken=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
            return rs.getString("username");
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
    return null;
  }

  public void deleteAuth(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM auth WHERE authToken=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting auth: " + e.getMessage());
    }
  }

  public void clear() {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM auth";
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
