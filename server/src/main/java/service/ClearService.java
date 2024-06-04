package service;

import dataaccess.authdaos.AuthDao;
import dataaccess.gamedaos.GameDao;
import dataaccess.userdaos.UserDao;
import request.ClearRequest;
import result.ClearResult;

public class ClearService {
    private final AuthDao authDao;
    private final UserDao userDao;
    private final GameDao gameDao;

    // Constructor to inject the DAO dependencies
    public ClearService(AuthDao authDao, UserDao userDao, GameDao gameDao) {
        this.authDao = authDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    public ClearResult clear(ClearRequest request) {
        authDao.clear();
        userDao.clear();
        gameDao.clear();

        return new ClearResult();
    }
}
