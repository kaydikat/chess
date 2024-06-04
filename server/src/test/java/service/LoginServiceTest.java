package service;

import dataaccess.*;
import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoSQL;
import dataaccess.userdaos.UserDao;
import dataaccess.userdaos.UserDaoSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
  private LoginService loginService;
  private RegisterService registerService;
  private AuthDao authDao;
  private UserDao userDao;
  private LoginRequest loginRequest;
  private RegisterRequest registerRequest;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoSQL.getInstance();
    userDao = UserDaoSQL.getInstance();

    // Clear data before each test
    authDao.clear();
    userDao.clear();

    loginService = new LoginService(authDao, userDao);
    registerService = new RegisterService(authDao, userDao);

    // Set up the login and register requests
    loginRequest = new LoginRequest("testUser", "password123");
    registerRequest = new RegisterRequest("testUser", "password123", "email@example.com");
  }

  @Test
  public void testLoginPositive() throws DataAccessException {
    // Register a user
    registerService.register(registerRequest);

    // Login with the registered user
    LoginResult result = loginService.login(loginRequest);

    assertEquals(null, result.message(), "Expected no error message for successful login");
    assertNotNull(result.authToken(), "Expected a valid auth token for successful login");
  }

  @Test
  public void testLoginWithUnregisteredUser() {
    // Attempt to login with an unregistered user
    try {
      LoginResult result = loginService.login(loginRequest);
      assertEquals("error: unauthorized", result.message(), "Expected error message for invalid login attempt");
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }
  }

  @Test
  public void testLoginWithIncorrectPassword() throws DataAccessException {
    // Register a user
    registerService.register(registerRequest);

    // Attempt to login with the incorrect password
    LoginRequest incorrectPasswordRequest = new LoginRequest("testUser", "wrongpassword");
    try {
      LoginResult result = loginService.login(incorrectPasswordRequest);
      assertEquals("error: unauthorized", result.message(), "Expected error message for invalid login attempt");
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }
  }

  @Test
  public void testLoginWithInvalidUsername() {
    // Attempt to login with a non-existent username
    LoginRequest invalidUsernameRequest = new LoginRequest("nonExistentUser", "password123");
    try {
      LoginResult result = loginService.login(invalidUsernameRequest);
      assertEquals("error: unauthorized", result.message(), "Expected error message for invalid login attempt");
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }
  }
}
