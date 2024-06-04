package authentication;

import dataaccess.authDaos.AuthDao;
import model.AuthData;

public class CheckAuth2 {

  private final AuthDao authDao;

  public CheckAuth2(AuthDao authDao) {
    this.authDao = authDao;
  }

  public boolean checkAuth(String authToken) {
    try {
      AuthData auth = authDao.getAuthWithAuthToken(authToken);
      return auth != null;
    } catch (Exception e) {
      return false;
    }
  }
}
