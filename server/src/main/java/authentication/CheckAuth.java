package authentication;

import dataaccess.AuthDaoInMemory;
import model.AuthData;

public class CheckAuth {

    public static boolean checkAuth(String authToken) {
        AuthDaoInMemory authDao = AuthDaoInMemory.getInstance();
        try {
            AuthData auth = authDao.getAuth(authToken);
            return auth != null;
        } catch (Exception e) {
            return false;
        }
    }
}
