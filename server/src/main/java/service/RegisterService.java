package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;
import dataaccess.UserDaoInMemory;

public class RegisterService {

  private final AuthDaoInMemory authDao;
  private final UserDaoInMemory userDao;

  public RegisterService() {
    this.authDao = AuthDaoInMemory.getInstance();
    this.userDao = UserDaoInMemory.getInstance();
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
    System.out.println("authToken: " + authToken);

    return new RegisterResult(request.username(), authToken, null);
  };
}
