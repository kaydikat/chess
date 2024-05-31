package service;


import dataaccess.AuthDao;
import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import request.LogoutRequest;
import result.LogoutResult;

import static authentication.CheckAuth.checkAuth;

public class LogoutService {
  private final AuthDao authDao;

  public LogoutService(AuthDao authDao) {
    this.authDao = authDao;
  }
  public LogoutResult logout(LogoutRequest request) throws DataAccessException {
    if (checkAuth(request.authToken())) {
      authDao.deleteAuth(request.authToken());
      return new LogoutResult(null);
    } else {
      return new LogoutResult("error: invalid auth token");
    }
  }
}
