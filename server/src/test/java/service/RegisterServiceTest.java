package service;

import dataaccess.*;
import dataaccess.authDaos.AuthDao;
import dataaccess.authDaos.AuthDaoSQL;
import dataaccess.userDaos.UserDao;
import dataaccess.userDaos.UserDaoSQL;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

  private RegisterService registerService;
  private AuthDao authDao;
  private UserDao userDao;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoSQL.getInstance();
    userDao = UserDaoSQL.getInstance();
    registerService = new RegisterService(authDao, userDao);

    // Clear data before each test
    authDao.clear();
    userDao.clear();
  }

  @Test
  public void testRegisterNewUser() throws DataAccessException {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "password123", "email@example.com");
    RegisterResult result = registerService.register(registerRequest);

    assertEquals("testUser", result.username(), "Expected username to match");
    assertNotNull(result.authToken(), "Expected a valid auth token");
    assertNull(result.message(), "Expected no error message");

    // Verify that the user was created in the userDao
    UserData user = userDao.getUser("testUser");
    assertNotNull(user, "Expected user to be created");
    assertEquals("testUser", user.username(), "Expected username to match");
    //assertEquals("password123", user.password(), "Expected password to match");
    assertEquals("email@example.com", user.email(), "Expected email to match");
  }

  @Test
  public void testRegisterExistingUser() throws DataAccessException {
    // Create a user first
    RegisterRequest registerRequest = new RegisterRequest("testUser", "password123", "email@example.com");
    registerService.register(registerRequest);

    // Attempt to register the same user again
    RegisterResult result = registerService.register(registerRequest);

    assertNull(result.username(), "Expected no username for duplicate registration");
    assertNull(result.authToken(), "Expected no auth token for duplicate registration");
    assertEquals("error: user already exists", result.message(), "Expected error message for duplicate registration");
  }

  @Test
  public void testRegisterMissingPassword() throws DataAccessException {
    // Attempt to register a user with a missing password
    RegisterRequest registerRequest = new RegisterRequest("testUser", null, "email@example.com");
    RegisterResult result = registerService.register(registerRequest);

    assertNull(result.username(), "Expected no username for missing password");
    assertNull(result.authToken(), "Expected no auth token for missing password");
    assertEquals("error: missing required field", result.message(), "Expected error message for missing password");
  }
}