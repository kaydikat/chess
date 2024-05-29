package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.UserDaoInMemory;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
  private LoginService loginService;
  private RegisterService registerService;
  private AuthDaoInMemory authDao;
  private UserDaoInMemory userDao;
  private LoginRequest loginRequest;
  private RegisterRequest registerRequest;

  @BeforeEach
  public void setUp() {
    loginService = new LoginService();
    registerService = new RegisterService();
    authDao = AuthDaoInMemory.getInstance();
    userDao = UserDaoInMemory.getInstance();

    // Clear data before each test
    authDao.clear();
    userDao.clear();

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

