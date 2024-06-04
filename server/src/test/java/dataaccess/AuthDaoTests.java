package dataaccess;

import dataaccess.DataAccessException;
import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoInMemory;
import dataaccess.authdaos.AuthDaoSQL;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthDaoTests {
  private AuthDao authDao;

  @BeforeEach
  public void setUp() {
    authDao =AuthDaoInMemory.getInstance(); // Or use AuthDaoInMemory for in-memory testing
    authDao.clear();
  }

  @Test
  public void testCreateAuthPositive() throws DataAccessException {
    authDao.createAuth("user1");
    AuthData retrievedAuth = authDao.getAuth("user1");
    assertNotNull(retrievedAuth, "Expected the auth data to be created and retrieved successfully");
  }

  @Test
  public void testCreateAuthDuplicateNegative() {
    assertThrows(DataAccessException.class, () -> {
      authDao.createAuth("user1");
      authDao.createAuth("user1"); // Attempt to create duplicate auth data
    });
  }

  @Test
  public void testGetAuthPositive() throws DataAccessException {
    authDao.createAuth("user1");
    AuthData retrievedAuth = authDao.getAuth("user1");
    assertNotNull(retrievedAuth, "Expected to retrieve auth data for user1");
  }

  @Test
  public void testGetAuthNegative() {
    assertThrows(DataAccessException.class, () -> {
      authDao.getAuth("nonexistentUser");
    });
  }

  @Test
  public void testGetAuthWithAuthTokenPositive() throws DataAccessException {
    authDao.createAuth("user1");
    String authToken = authDao.getAuth("user1").authToken();
    AuthData retrievedAuth = authDao.getAuthWithAuthToken(authToken); // Assume the auth token for user1 is "user1AuthToken"
    assertNotNull(retrievedAuth, "Expected to retrieve auth data with auth token");
  }

  @Test
  public void testGetAuthWithAuthTokenNegative() {
    assertThrows(DataAccessException.class, () -> {
      authDao.getAuthWithAuthToken("nonexistentAuthToken");
    });
  }

  @Test
  public void testDeleteAuthPositive() throws DataAccessException {
    authDao.createAuth("user1");
    String authToken = authDao.getAuth("user1").authToken();
    authDao.deleteAuth(authToken); // Assume the auth token for user1 is "user1AuthToken"
    assertThrows(DataAccessException.class, () -> {
      authDao.getAuthWithAuthToken(authToken);
    });
  }

  @Test
  public void testClear() throws DataAccessException {
    authDao.createAuth("user1");
    authDao.clear();
    assertThrows(DataAccessException.class, () -> {
      authDao.getAuth("user1");
    });
  }
}
