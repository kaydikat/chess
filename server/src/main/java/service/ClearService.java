package service;

import dataaccess.*;
import request.ClearRequest;
import result.ClearResult;

public class ClearService {
    private final AuthDao authDao;
    private final UserDao userDao;
    private final GameDao gameDao;

    // Constructor to inject the DAO dependencies
    public ClearService() {
        this.authDao = AuthDaoInMemory.getInstance();
        this.userDao = UserDaoInMemory.getInstance();
        this.gameDao = GameDaoInMemory.getInstance();
    }

    public ClearResult clear(ClearRequest request) {
        authDao.clear();
        userDao.clear();
        gameDao.clear();

        return new ClearResult();
    }
}
