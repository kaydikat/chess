package authentication;

import dataaccess.authDaos.AuthDao;
import dataaccess.authDaos.AuthDaoSQL;
import model.AuthData;

public class ReturnAuth {

  public static String returnAuth(String authToken) {
    AuthDao authDao = AuthDaoSQL.getInstance();
    try {
      AuthData auth = authDao.getAuthWithAuthToken(authToken);
      return auth.username();
    } catch (Exception e) {
      return null;
    }
  }
}
