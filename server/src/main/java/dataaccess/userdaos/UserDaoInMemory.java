package dataaccess.userdaos;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDaoInMemory implements UserDao {
  private static UserDaoInMemory instance;

  private final Map<String, UserData> users = new HashMap<>();

  private UserDaoInMemory() {}

  public static UserDaoInMemory getInstance() {
    if (instance == null) {
      instance = new UserDaoInMemory();
    }
    return instance;
  }
  public Map<String, UserData> getUsers() {
    return users;
  }

  public void createUser(UserData user) throws DataAccessException {
      if (users.containsKey(user.username())) {
        throw new DataAccessException("User already exists");
      }
        users.put(user.username(), user);
    }
    public UserData getUser(String username) throws DataAccessException {
        if (!users.containsKey(username)) {
            throw new DataAccessException("User not found");
        }
      return users.get(username);
    }

    public void clear() {
      users.clear();
    }
}
