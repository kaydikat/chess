package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;
import java.util.Map;

public class UserDaoInMemory {
  private final Map<String, UserData> users = new HashMap<>();

    public void createUser(UserData user) throws DataAccessException {
      if (users.containsKey(user.username())) {
        throw new DataAccessException("User already exists");
      }
        users.put(user.username(), user);
    }
    public UserData getUser(String username) throws DataAccessException {
      UserData user = users.get(username);
      if (user == null) {
        throw new DataAccessException("User not found");
      }
      return user;
    }
    public void clear() throws DataAccessException {
      users.clear();
    }
}
