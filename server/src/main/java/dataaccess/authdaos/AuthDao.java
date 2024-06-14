package dataaccess.authdaos;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDao {
  void createAuth(String username) throws DataAccessException;
  AuthData getAuth(String username) throws DataAccessException;
  AuthData getAuthWithAuthToken(String authToken) throws DataAccessException;
  String getUsername(String authToken) throws DataAccessException;
  void deleteAuth(String authToken) throws DataAccessException;
  void clear();
}
