package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.UserDaoInMemory;
import dataaccess.GameDaoInMemory;
import request.ClearRequest;

public class ClearService {
    public void clear(ClearRequest request) {
        AuthDaoInMemory authDao = new AuthDaoInMemory();
        UserDaoInMemory userDao = new UserDaoInMemory();
        GameDaoInMemory gameDao = new GameDaoInMemory();

        authDao.clear();
        userDao.clear();
        gameDao.clear();
    }
}
