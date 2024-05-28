package service;

import chess.ChessPosition;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInMemory;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import service.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class LoginServiceTest {
  private final LoginService loginService = new LoginService();
  private final RegisterService registerService = new RegisterService();
  LoginRequest request = new LoginRequest("testUser", "password123");
  RegisterRequest registerRequest = new RegisterRequest("testUser", "password123", "email");
  @Test
  public void loginPositive() throws DataAccessException {
    loginService.login(request);
  }
  @Test
  public void loginWithRegister() throws DataAccessException {
    registerService.register(registerRequest);
    loginService.login(request);
  }

}