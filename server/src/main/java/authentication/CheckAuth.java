package authentication;

import dataaccess.AuthDao;
import dataaccess.AuthDaoInMemory;
import dataaccess.AuthDaoSQL;
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
