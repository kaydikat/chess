package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.UserDaoInMemory;
import dataaccess.GameDaoInMemory;
import request.ClearRequest;
import result.ClearResult;

public class ClearService {
    public ClearResult clear(ClearRequest request) {
        AuthDaoInMemory authDao = new AuthDaoInMemory();
        UserDaoInMemory userDao = new UserDaoInMemory();
        GameDaoInMemory gameDao = new GameDaoInMemory();

        authDao.clear();
        userDao.clear();
        gameDao.clear();

        return new ClearResult();
    }
}
