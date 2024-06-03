package password;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
  public static void storeUserPassword(String username, String password, String email) throws DataAccessException {
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

    try (var conn = DatabaseManager.getConnection()) {
      var statement="INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, hashedPassword);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error storing password: " + e.getMessage());
    }
  }
  public static boolean verifyUser(String username, String providedClearTextPassword) throws DataAccessException {
    String hashedPassword = null;
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT password FROM user WHERE username=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        try (var rs = preparedStatement.executeQuery()) {
          if (rs.next()) {
            hashedPassword = rs.getString("password");
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
    return hashedPassword != null && BCrypt.checkpw(providedClearTextPassword, hashedPassword);
  }
}
