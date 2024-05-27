package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import dataaccess.UserDaoInMemory;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;

import java.util.UUID;

public class LoginService {

  public LoginResult login(LoginRequest request) throws DataAccessException {
    UserDaoInMemory userDao = new UserDaoInMemory();
    UserData user = userDao.getUser(request.username());
    if (user == null || !user.password().equals(request.password())) {
      System.out.println("User not found or password incorrect");
    }

    AuthDaoInMemory authDao = new AuthDaoInMemory();
    authDao.createAuth(request.username());
    String authToken = UUID.randomUUID().toString();
    System.out.println("authToken: " + authToken);

    return new LoginResult(request.username(), authToken);
  }
}
