package service;

import authentication.CheckAuth2;
import dataaccess.authDaos.AuthDao;
import dataaccess.DataAccessException;
import request.LogoutRequest;
import result.LogoutResult;
//import static authentication.CheckAuth.checkAuth;

public class LogoutService {
  private final AuthDao authDao;
  private final CheckAuth2 checkAuth;

  public LogoutService(AuthDao authDao) {
    this.authDao = authDao;
    this.checkAuth = new CheckAuth2(authDao); // Use the same AuthDao instance
  }

  public LogoutResult logout(LogoutRequest request) throws DataAccessException {
    if (checkAuth.checkAuth(request.authToken())) {
      authDao.deleteAuth(request.authToken());
      return new LogoutResult(null);
    } else {
      return new LogoutResult("error: invalid auth token");
    }
  }
}