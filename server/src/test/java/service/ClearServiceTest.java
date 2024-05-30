package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import result.ClearResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

  private ClearService clearService;
  private AuthDao authDao;
  private UserDao userDao;
  private GameDao gameDao;

  @BeforeEach
  public void setUp() {
    // Reset the instances of the DAOs to ensure a clean state for each test
    this.authDao = AuthDaoInMemory.getInstance();
    this.userDao = UserDaoInMemory.getInstance();
    this.gameDao = GameDaoInMemory.getInstance();


    clearService = new ClearService();
  }

  @Test
  public void testClear() throws DataAccessException {
    // Setup - Add some data to each DAO
    AuthDaoInMemory authDao = AuthDaoInMemory.getInstance();
    UserDaoInMemory userDao = UserDaoInMemory.getInstance();
    GameDaoInMemory gameDao = GameDaoInMemory.getInstance();

    authDao.createAuth("user1");
    userDao.createUser(new UserData("user1", "password1", "user1@example.com"));
    gameDao.createGame("Test Game");

    // Ensure data is present before clear
    Map<String, UserData> users = userDao.getUsers();
    Map<String, UserData> auths = userDao.getUsers();
    assertFalse(auths.isEmpty());
    assertFalse(users.isEmpty());
    assertFalse(gameDao.listGames().isEmpty());

    // Perform the clear operation
    ClearRequest clearRequest = new ClearRequest(); // Assuming ClearRequest has a no-arg constructor
    ClearResult clearResult = clearService.clear(clearRequest);

    // Assertions to check if the clear operation was successful
    assertTrue(auths.isEmpty(), "Auth data should be cleared");
    assertTrue(users.isEmpty(), "User data should be cleared");
    assertTrue(gameDao.listGames().isEmpty(), "Game data should be cleared");

    // Assuming ClearResult has some methods to verify the result
    assertNotNull(clearResult, "ClearResult should not be null");
  }
}
