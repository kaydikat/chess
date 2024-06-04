package authentication;

import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoSQL;
import model.AuthData;

public class CheckAuth {

    public static boolean checkAuth(String authToken) {
      try {
          AuthDao authDao = AuthDaoSQL.getInstance();
          AuthData auth = authDao.getAuthWithAuthToken(authToken);
          return auth != null;
        } catch (Exception e) {
            return false;
        }
    }
}
