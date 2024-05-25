package authentication;
import dataaccess.AuthDaoInMemory;
import model.AuthData;
import dataaccess.AuthDaoInMemory;


public class CheckAuth {
    public static boolean checkAuth(String authToken) {
        AuthDaoInMemory authDao = new AuthDaoInMemory();
        try {
            AuthData auth = authDao.getAuth(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
