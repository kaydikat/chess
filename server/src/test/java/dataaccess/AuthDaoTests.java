package dataaccess;

import dataaccess.authDaos.AuthDao;
import dataaccess.authDaos.AuthDaoSQL;
import dataaccess.userDaos.UserDao;
import dataaccess.userDaos.UserDaoInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

public class AuthDaoTests {
  private AuthDao authDao;
  private UserDao userDao;

  @BeforeEach
  public void setUp() {
    this.authDao = AuthDaoSQL.getInstance();
    this.userDao = UserDaoInMemory.getInstance();
    authDao.clear();
    userDao.clear();
  }

  @Test
  void createAuthTest() throws DataAccessException {
    authDao.createAuth("user1");
    String authToken = authDao.getAuth("user1").authToken();
    assert(authToken != null);
  }

  @Test
  void getAuth() {
  }

  @Test
  void getAuthWithAuthToken() {
  }

  @Test
  void deleteAuth() {
  }

  @Test
  void clear() {
  }
}
