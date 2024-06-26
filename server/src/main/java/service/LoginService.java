package service;

import dataaccess.*;
import dataaccess.authdaos.AuthDao;
import dataaccess.userdaos.UserDao;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;
import password.PasswordUtil;


public class LoginService {

  private final AuthDao authDao;
  private final UserDao userDao;

  public LoginService(AuthDao authDao, UserDao userDao) {
    this.authDao=authDao;
    this.userDao=userDao;
  }

  public LoginResult login(LoginRequest request) throws DataAccessException {

    UserData user = userDao.getUser(request.username());
    if (user == null) {
      return new LoginResult(null, null, "error: unauthorized");
    }
    if (!user.password().equals(request.password())) {
      if (PasswordUtil.verifyUser(request.username(), request.password())) {
        authDao.createAuth(request.username());
        String authToken = authDao.getAuth(request.username()).authToken();
        System.out.println("authToken: " + authToken);

        return new LoginResult(request.username(), authToken, null);
      }
      return new LoginResult(null, null, "error: unauthorized");
    }


    authDao.createAuth(request.username());
    String authToken = authDao.getAuth(request.username()).authToken();
    System.out.println("authToken: " + authToken);

    return new LoginResult(request.username(), authToken, null);
  }
}