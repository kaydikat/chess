package service;

import dataaccess.*;
import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoSQL;
import dataaccess.gamedaos.GameDao;
import dataaccess.gamedaos.GameDaoSQL;
import dataaccess.userdaos.UserDao;
import dataaccess.userdaos.UserDaoSQL;
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
  private AuthDao authDao;
  private GameDao gameDao;
  private UserDao userDao;
  private RegisterRequest registerRequest;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoSQL.getInstance();
    gameDao = GameDaoSQL.getInstance();
    userDao = UserDaoSQL.getInstance();

    createGameService = new CreateGameService(gameDao);
    listGamesService = new ListGamesService(gameDao);
    registerService = new RegisterService(authDao, userDao);

    // Clear data before each test
    authDao.clear();
    gameDao.clear();
    userDao.clear();

    // Set up the register request
    registerRequest = new RegisterRequest("testUser", "password123", "email@example.com");
  }

  @Test
  public void testListGamesWithValidAuth() throws DataAccessException {
    registerService.register(registerRequest);

    String authToken = authDao.getAuth("testUser").authToken();

    assertNotNull(authDao.getAuthWithAuthToken(authToken), "Auth token should be valid after registration");

    // Create some games
    CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Chess1");
    createGameService.createGame(createGameRequest);
    createGameRequest = new CreateGameRequest(authToken, "Chess2");
    createGameService.createGame(createGameRequest);

    // List games with a valid auth token
    ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
    ListGamesResult result = listGamesService.listGames(listGamesRequest);

    assertNull(result.message(), "Expected no error message for valid auth token");
    assertNotNull(result.games(), "Expected a non-null games list");
    assertEquals(2, result.games().size(), "Expected two games to be listed");

    // Check the games' names
    boolean chess1Found = result.games().stream().anyMatch(game -> "Chess1".equals(game.gameName()));
    boolean chess2Found = result.games().stream().anyMatch(game -> "Chess2".equals(game.gameName()));
    assertTrue(chess1Found, "Expected to find game 'Chess1' in the list");
    assertTrue(chess2Found, "Expected to find game 'Chess2' in the list");
  }

  @Test
  public void testListGamesWithInvalidAuth() throws DataAccessException {
    // List games with an invalid auth token
    ListGamesRequest listGamesRequest = new ListGamesRequest("invalidAuthToken");
    ListGamesResult result = listGamesService.listGames(listGamesRequest);

    assertEquals("error: invalid auth token", result.message(), "Expected error message for invalid auth token");
    assertNull(result.games(), "Expected no games to be listed for invalid auth token");
  }
}