package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.GameDaoInMemory;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.ListGamesResult;

import static org.junit.jupiter.api.Assertions.*;

class ListGamesServiceTest {
  private CreateGameService createGameService;
  private ListGamesService listGamesService;
  private RegisterService registerService;
  private AuthDaoInMemory authDao;
  private GameDaoInMemory gameDao;
  private RegisterRequest registerRequest;

  @BeforeEach
  public void setUp() {
    createGameService = new CreateGameService();
    listGamesService = new ListGamesService();
    registerService = new RegisterService();
    authDao = AuthDaoInMemory.getInstance();
    gameDao = GameDaoInMemory.getInstance();

    // Clear data before each test
    authDao.clear();
    gameDao.clear();

    // Set up the register request
    registerRequest = new RegisterRequest("testUser", "password123", "email");
  }

  @Test
  public void testListGamesWithValidAuth() throws DataAccessException {
    // Register a user and get the auth token
    String authToken = registerService.register(registerRequest).authToken();

    // Create some games
    CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Chess1");
    createGameService.createGame(createGameRequest);
    createGameRequest = new CreateGameRequest(authToken, "Chess2");
    createGameService.createGame(createGameRequest);

    // List games with a valid auth token
    ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
    ListGamesResult result = listGamesService.listGames(listGamesRequest);

    assertEquals(null, result.message(), "Expected no error message for valid auth token");
    assertTrue(result.games() != null && result.games().size() == 2, "Expected two games to be listed");

    // Check the games' names
    boolean chess1Found = result.games().stream().anyMatch(game -> "Chess1".equals(game.gameName()));
    boolean chess2Found = result.games().stream().anyMatch(game -> "Chess2".equals(game.gameName()));
    assertTrue(chess1Found, "Expected to find game 'Chess1' in the list");
    assertTrue(chess2Found, "Expected to find game 'Chess2' in the list");
  }

  @Test
  public void testListGamesWithInvalidAuth() {
    // List games with an invalid auth token
    ListGamesRequest listGamesRequest = new ListGamesRequest("invalidAuthToken");
    ListGamesResult result = listGamesService.listGames(listGamesRequest);

    assertEquals("error: invalid auth token", result.message(), "Expected error message for invalid auth token");
    assertNull(result.games(), "Expected no games to be listed for invalid auth token");
  }
}