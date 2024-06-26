package service;

import dataaccess.*;
import dataaccess.authdaos.AuthDao;
import dataaccess.userdaos.UserDao;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterService {

  private final AuthDao authDao;
  private final UserDao userDao;

  public RegisterService(AuthDao authDao, UserDao userDao) {
    this.authDao = authDao;
    this.userDao = userDao;
  }
  public RegisterResult register(RegisterRequest request) throws DataAccessException {
    if (userDao.getUser(request.username()) != null) {
      return new RegisterResult(null, null, "error: user already exists");
    }
    if (request.password() == null) {
        return new RegisterResult(null, null, "error: missing required field");
    }
      UserData user=new UserData(request.username(), request.password(), request.email());
      userDao.createUser(user);

    authDao.createAuth(request.username());
    String authToken = authDao.getAuth(request.username()).authToken();

    return new RegisterResult(request.username(), authToken, null);
  };
}
