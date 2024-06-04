package authentication;

import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoSQL;
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
