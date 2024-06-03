package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.GameDaoInMemory;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import result.CreateGameResult;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameServiceTest {

  private CreateGameService createGameService;
  private AuthDaoInMemory authDao;
  private GameDaoInMemory gameDao;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoInMemory.getInstance();
    gameDao = GameDaoInMemory.getInstance();

    // Clear data before each test
    authDao.clear();
    gameDao.clear();

    // Add a test user and authorization token
    authDao.createAuth("testUser");

    // Initialize createGameService after gameDao is initialized
    createGameService = new CreateGameService(gameDao);
  }

  @Test
  public void testCreateGameWithValidAuth() {
    // Get the valid auth token
    String validAuthToken = authDao.getAuths().values().stream()
            .filter(auth -> auth.username().equals("testUser"))
            .findFirst()
            .map(auth -> auth.authToken())
            .orElse(null);

    assertNotNull(validAuthToken, "Valid auth token should not be null");

    // Create a game request with valid auth token
    CreateGameRequest request = new CreateGameRequest(validAuthToken, "Test Game");
    CreateGameResult result = null;
    try {
      result = createGameService.createGame(request);
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }

    assertEquals(null, result.message(), "Expected no error message for valid auth token");
    assertTrue(result.gameID() != null && result.gameID() > 0, "Expected a valid game ID");

    // Check if the game was created in the gameDao
    GameData game = null;
    try {
      game = gameDao.getGame(result.gameID());
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }

    assertEquals("Test Game", game.gameName(), "Game name should match the requested game name");
  }

  @Test
  public void testCreateGameWithInvalidAuth() {
    // Create a game request with an invalid auth token
    CreateGameRequest request = new CreateGameRequest("invalidAuthToken", "Test Game");
    CreateGameResult result = null;
    try {
      result = createGameService.createGame(request);
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }

    assertEquals("error: invalid authorization", result.message(), "Expected error message for invalid auth token");
    assertEquals(null, result.gameID(), "Expected no game ID for invalid auth token");
  }
}

