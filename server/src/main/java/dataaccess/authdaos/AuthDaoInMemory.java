package dataaccess.authdaos;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDaoInMemory implements AuthDao {
  private static AuthDaoInMemory instance;
  private final Map<String, AuthData> auths = new HashMap<>();

  private AuthDaoInMemory() {}

  public static AuthDaoInMemory getInstance() {
    if (instance == null) {
      instance = new AuthDaoInMemory();
    }
    return instance;
  }

  public void createAuth(String username) throws DataAccessException {
    if (auths.containsKey(username)) {
      throw new DataAccessException("Auth already exists");
    }
    String authToken = UUID.randomUUID().toString();
    AuthData auth = new AuthData(authToken, username);

    auths.put(authToken, auth);
    auths.put(username, auth);
  }
  public AuthData getAuth(String username) throws DataAccessException {
    AuthData auth = auths.get(username);
    if (auth == null) {
      throw new DataAccessException("Auth not found");
    }
    return auth;
  }
    public AuthData getAuthWithAuthToken(String authToken) throws DataAccessException {
        AuthData auth = auths.get(authToken);
        if (auth == null) {
        throw new DataAccessException("Auth not found");
        }
        return auth;
    }
  public void deleteAuth(String authToken) throws DataAccessException {
    AuthData auth = auths.get(authToken);
    if (auth == null) {
      throw new DataAccessException("Auth not found");
    }
    auths.remove(authToken);
    auths.remove(auth.username());
  }

  public void clear() {
    auths.clear();
  }
}
