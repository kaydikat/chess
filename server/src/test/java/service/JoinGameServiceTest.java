package service;

import dataaccess.AuthDaoInMemory;
import dataaccess.GameDaoInMemory;
import dataaccess.UserDaoInMemory;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import result.JoinGameResult;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {
  private JoinGameService joinGameService;
  private CreateGameService createGameService;
  private RegisterService registerService;
  private AuthDaoInMemory authDao;
  private GameDaoInMemory gameDao;
  private UserDaoInMemory userDao;
  private RegisterRequest registerRequest;

  @BeforeEach
  public void setUp() {
    authDao = AuthDaoInMemory.getInstance();
    gameDao = GameDaoInMemory.getInstance();
    userDao = UserDaoInMemory.getInstance();

    joinGameService = new JoinGameService(gameDao, userDao);
    createGameService = new CreateGameService(gameDao);
    registerService = new RegisterService(authDao, userDao);

    // Clear data before each test
    authDao.clear();
    gameDao.clear();
    userDao.clear();

    // Set up the register request
    registerRequest = new RegisterRequest("testUser", "password123", "email@example.com");
  }

  @Test
  public void testJoinGameWithValidAuthAndValidColor() throws DataAccessException {
    // Register a user and get the auth token
    String authToken = registerService.register(registerRequest).authToken();

    // Create a game
    CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Chess1");
    int gameID = createGameService.createGame(createGameRequest).gameID();

    // Join the game as a white player
    JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "white", gameID);
    JoinGameResult result = joinGameService.joinGame(joinGameRequest);

    assertEquals(null, result.message(), "Expected no error message for valid join game request");

    // Verify the game state
    GameData game = gameDao.getGame(gameID);
    assertEquals("testUser", game.whiteUsername(), "Expected 'testUser' to be the white player");
  }

  @Test
  public void testJoinGameWithInvalidAuth() {
    // Create a join game request with an invalid auth token
    JoinGameRequest joinGameRequest = new JoinGameRequest("invalidAuthToken", "white", 1);
    JoinGameResult result = joinGameService.joinGame(joinGameRequest);

    assertEquals("error: invalid auth token", result.message(), "Expected error message for invalid auth token");
  }

  @Test
  public void testJoinGameWithInvalidColor() throws DataAccessException {
    // Register a user and get the auth token
    String authToken = registerService.register(registerRequest).authToken();

    // Create a game
    CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Chess1");
    int gameID = createGameService.createGame(createGameRequest).gameID();

    // Create a join game request with an invalid color
    JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "purple", gameID);
    JoinGameResult result = joinGameService.joinGame(joinGameRequest);

    assertEquals("error: invalid player color", result.message(), "Expected error message for invalid player color");
  }

  @Test
  public void testJoinGameWithColorAlreadyExists() throws DataAccessException {
    // Register a user and get the auth token
    String authToken = registerService.register(registerRequest).authToken();

    // Create a game
    CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Chess1");
    int gameID = createGameService.createGame(createGameRequest).gameID();

    // Join the game as a white player
    JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "white", gameID);
    joinGameService.joinGame(joinGameRequest);

    // Register another user and get the auth token
    RegisterRequest registerRequest2 = new RegisterRequest("testUser2", "password123", "email2@example.com");
    String authToken2 = registerService.register(registerRequest2).authToken();

    // Try to join the game as a white player again
    joinGameRequest = new JoinGameRequest(authToken2, "white", gameID);
    JoinGameResult result = joinGameService.joinGame(joinGameRequest);

    assertEquals("error: color already exists", result.message(), "Expected error message for color already exists");
  }

  @Test
  public void testJoinGameWithBadGameID() {
    try {
      // Register a user and get the auth token
      String authToken = registerService.register(registerRequest).authToken();

      // Create a join game request with a non-existent game ID
      JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "white", 9999);
      JoinGameResult result = joinGameService.joinGame(joinGameRequest);

      assertEquals("error: bad game ID", result.message(), "Expected error message for bad game ID");
    } catch (DataAccessException e) {
      fail("DataAccessException was thrown: " + e.getMessage());
    }
  }
}