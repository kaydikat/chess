package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import dataaccess.GameDaoInMemory;
import dataaccess.UserDaoInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LogoutResult;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTest {
  private AuthDaoInMemory authDao;
  private UserDaoInMemory userDao;

  private LoginService loginService;
  private RegisterService registerService;
  private LogoutService logoutService;
  private RegisterRequest registerRequest;
  private LoginRequest loginRequest;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoInMemory.getInstance();
    userDao = UserDaoInMemory.getInstance();

    authDao.clear();
    userDao.clear();

    loginService = new LoginService(authDao, userDao);
    registerService = new RegisterService(authDao, userDao);
    logoutService = new LogoutService(authDao);

    registerRequest = new RegisterRequest("testUser", "password123", "email@example.com");
    loginRequest = new LoginRequest("testUser", "password123");
  }

  @Test
  public void testLogoutWithValidAuth() throws DataAccessException {
    // Register a user and login
    registerService.register(registerRequest);
    String authToken = loginService.login(loginRequest).authToken();

    // Logout with the valid auth token
    LogoutRequest logoutRequest = new LogoutRequest(authToken);
    LogoutResult result = logoutService.logout(logoutRequest);

    assertEquals(null, result.message(), "Expected no error message for successful logout");

    // Verify that the auth token is invalidated
    assertThrows(DataAccessException.class, () -> authDao.getAuth(authToken), "Expected DataAccessException for invalidated auth token");
  }

  @Test
  public void testLogoutWithInvalidAuth() {
    // Logout with an invalid auth token
    LogoutRequest logoutRequest = new LogoutRequest("invalidAuthToken");
    LogoutResult result = null;
    try {
      result = logoutService.logout(logoutRequest);
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }

    assertEquals("error: invalid auth token", result.message(), "Expected error message for invalid auth token");
  }
}