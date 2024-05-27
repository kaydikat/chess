package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;
import dataaccess.UserDaoInMemory;

public class RegisterService {
  public RegisterResult register(RegisterRequest request) throws DataAccessException {
    UserDaoInMemory userDao = new UserDaoInMemory();
    if (userDao.getUser(request.username()) != null) {
      throw new DataAccessException("User already exists");
    }
      UserData user=new UserData(request.username(), request.password(), request.email());
      userDao.createUser(user);

    AuthDaoInMemory authDao = new AuthDaoInMemory();
    authDao.createAuth(request.username());
    String authToken = authDao.getAuth(request.username()).authToken();

    return new RegisterResult(request.username(), authToken);
  };
}
