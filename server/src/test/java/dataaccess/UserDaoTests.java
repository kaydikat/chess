package dataaccess;

import dataaccess.userDaos.UserDao;
import dataaccess.userDaos.UserDaoInMemory;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTests {
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        userDao = UserDaoInMemory.getInstance();
        userDao.clear();
    }

    @Test
    public void testCreateUserPositive() throws DataAccessException {
      UserData user = new UserData("user1", "password1", "user1@example.com");
      userDao.createUser(user);
      UserData retrievedUser = userDao.getUser("user1");
      assertEquals(user, retrievedUser, "Expected the user to be created and retrieved successfully");
    }

    @Test
    public void testCreateUserDuplicateNegative() {
      UserData user =new UserData("user1", "password1", "user1@example.com");
      UserData userDupe=new UserData("user1", "password2", "user2@example.com");

      assertThrows(DataAccessException.class, () -> {
        userDao.createUser(user);
        userDao.createUser(userDupe);
      });
    }

  @Test
  public void testGetUserPositive() throws DataAccessException {
    UserData user = new UserData("user1", "password1", "user1@example.com");
    userDao.createUser(user);
    UserData retrievedUser = userDao.getUser("user1");
    assertEquals(user, retrievedUser);
  }

  @Test
    public void testGetUserNegative() {
        assertThrows(DataAccessException.class, () -> {
        userDao.getUser("user1");
        });
    }

    @Test
    public void testClear() throws DataAccessException {
      UserData user=new UserData("user1", "password1", "user1@example.com");
      userDao.createUser(user);
      userDao.clear();

      assertThrows(DataAccessException.class, () -> {
        userDao.getUser("user1");
      });
    }

}
