package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDaoInMemory {
  private static UserDaoInMemory instance;

  private final Map<String, UserData> users = new HashMap<>();

  private UserDaoInMemory() {}

  public static UserDaoInMemory getInstance() {
    if (instance == null) {
      instance = new UserDaoInMemory();
    }
    return instance;
  }

  public static void resetInstance() {
    instance = new UserDaoInMemory();
  }

  public void createUser(UserData user) throws DataAccessException {
      if (users.containsKey(user.username())) {
        throw new DataAccessException("User already exists");
      }
        users.put(user.username(), user);
    }
    public UserData getUser(String username) throws DataAccessException {
      return users.get(username);
    }

    public void clear() {
      users.clear();
    }
}
