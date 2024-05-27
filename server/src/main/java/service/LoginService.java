package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInMemory;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;

public class LoginService {
  public LoginResult login(LoginRequest request) throws DataAccessException {
    UserDaoInMemory userDao = new UserDaoInMemory();
    UserData user = userDao.getUser(request.username());
    if (user == null || !user.password().equals(request.password())) {
      return null;
    }

    AuthDaoInMemory authDao = new AuthDaoInMemory();
    authDao.createAuth(request.username());
    String authToken = authDao.getAuth(request.username()).authToken();

    return new LoginResult(request.username(), authToken);
  }
}
