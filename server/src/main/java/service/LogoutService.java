package service;


import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import request.LogoutRequest;
import result.LogoutResult;

import static authentication.CheckAuth.checkAuth;

public class LogoutService {
  private final AuthDaoInMemory authDao;

  public LogoutService() {
    this.authDao = AuthDaoInMemory.getInstance();
  }
  public LogoutResult logout(LogoutRequest request) throws DataAccessException {
    if (request == null) {
      return new LogoutResult();
    }
    if (checkAuth(request.getAuthToken())) {
      authDao.deleteAuth(request.getAuthToken());
    }

    return new LogoutResult();
  }
}
