package authentication;

import dataaccess.AuthDaoInMemory;
import model.AuthData;

public class ReturnAuth {

  public static String returnAuth(String authToken) {
    AuthDaoInMemory authDao = AuthDaoInMemory.getInstance();
    try {
      AuthData auth = authDao.getAuth(authToken);
      return auth.username();
    } catch (Exception e) {
      return null;
    }
  }
}
