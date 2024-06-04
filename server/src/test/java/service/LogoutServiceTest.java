package service;

import dataaccess.*;
import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoSQL;
import dataaccess.userdaos.UserDao;
import dataaccess.userdaos.UserDaoSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LogoutResult;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTest {
  private AuthDao authDao;
  private UserDao userDao;

  private LoginService loginService;
  private RegisterService registerService;
  private LogoutService logoutService;
  private RegisterRequest registerRequest;
  private LoginRequest loginRequest;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoSQL.getInstance();
    userDao = UserDaoSQL.getInstance();

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
    registerService.register(registerRequest);
    String authToken = loginService.login(loginRequest).authToken();

    LogoutRequest logoutRequest = new LogoutRequest(authToken);
    LogoutResult result = logoutService.logout(logoutRequest);

    assertEquals(null, result.message(), "Expected no error message for successful logout");

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