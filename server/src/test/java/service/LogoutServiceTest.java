package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInMemory;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;

public class LogoutServiceTest {

  private final LoginService loginService = new LoginService();
  private final RegisterService registerService = new RegisterService();

  private final LogoutService logoutService = new LogoutService();
  private final ClearService clearService = new ClearService();

  ClearRequest clearRequest = new ClearRequest();

  LoginRequest request = new LoginRequest("testUser", "password123");
  RegisterRequest registerRequest = new RegisterRequest("testUser", "password123", "email");

  @Test
  public void logoutWithRegister() throws DataAccessException {
    registerService.register(registerRequest);
    String authToken = loginService.login(request).authToken();
    LogoutRequest logoutRequest = new LogoutRequest(authToken);
    logoutService.logout(logoutRequest);
    AuthDaoInMemory authDao = AuthDaoInMemory.getInstance();
  }
}
