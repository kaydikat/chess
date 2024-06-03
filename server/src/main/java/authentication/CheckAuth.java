package authentication;

import dataaccess.AuthDao;
import dataaccess.AuthDaoInMemory;
import dataaccess.AuthDaoSQL;
import model.AuthData;

public class CheckAuth {

    public static boolean checkAuth(String authToken) {
        AuthDao authDao = AuthDaoInMemory.getInstance();
        try {
            AuthData auth = authDao.getAuth(authToken);
            return auth != null;
        } catch (Exception e) {
            return false;
        }
    }
}
