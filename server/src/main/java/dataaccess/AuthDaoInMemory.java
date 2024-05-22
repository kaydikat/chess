package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;

public class AuthDaoInMemory {
  private final Map<String, AuthData> auths = new HashMap<>();

  public void createAuth(String username) throws DataAccessException {
    String authToken = String.valueOf(new SecureRandom());
    AuthData auth = new AuthData(authToken, username);

    auths.put(authToken, auth);
    auths.put(username, auth);
  }
  public AuthData getAuth(String authToken) throws DataAccessException {
    AuthData auth = auths.get(authToken);
    if (auth == null) {
      throw new DataAccessException("Auth not found");
    }
    return auth;
  }
  void deleteAuth(String authToken) throws DataAccessException {
    AuthData auth = auths.get(authToken);
    if (auth == null) {
      throw new DataAccessException("Auth not found");
    }
    auths.remove(authToken);
    auths.remove(auth.username());
  }
  void clear() {
    auths.clear();
  }
}
