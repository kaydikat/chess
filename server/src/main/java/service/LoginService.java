package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInMemory;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;

import java.util.UUID;

public class LoginService {

  private final AuthDaoInMemory authDao;
  private final UserDaoInMemory userDao;

  public LoginService() {
    this.authDao = AuthDaoInMemory.getInstance();
    this.userDao = UserDaoInMemory.getInstance();
  }
  public LoginResult login(LoginRequest request) throws DataAccessException {

    UserData user = userDao.getUser(request.username());
    if (user == null) {
      return new LoginResult(null, null, "error: unauthorized");
    }
    if (!user.password().equals(request.password())) {
      return new LoginResult(null, null, "error: unauthorized");
    }

    authDao.createAuth(request.username());
    String authToken = authDao.getAuth(request.username()).authToken();
    System.out.println("authToken: " + authToken);

    return new LoginResult(request.username(), authToken, null);
  }
}
