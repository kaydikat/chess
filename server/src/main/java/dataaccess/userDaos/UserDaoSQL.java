package dataaccess.userDaos;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import password.PasswordUtil;

import java.sql.SQLException;

public class UserDaoSQL implements UserDao {
  private static UserDaoSQL instance;
    private UserDaoSQL() {}

    public static UserDaoSQL getInstance() {
      if (instance == null) {
        instance = new UserDaoSQL();
      }
      return instance;
    }
  public void createUser(UserData user) throws DataAccessException {
      if (getUser(user.username()) != null) {
        throw new DataAccessException("User already exists");
      }
      PasswordUtil.storeUserPassword(user.username(), user.password(), user.email());
  }

  public UserData getUser(String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM user WHERE username=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
            return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
    return null;
  }

  public void clear() {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM user";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println("Error clearing user table: " + e.getMessage());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
