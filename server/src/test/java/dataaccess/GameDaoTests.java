package dataaccess;

import dataaccess.DataAccessException;
import dataaccess.gamedaos.GameDao;
import dataaccess.gamedaos.GameDaoInMemory;
import dataaccess.gamedaos.GameDaoSQL;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameDaoTests {
  private GameDao gameDao;

  @BeforeEach
  public void setUp() {
    gameDao = GameDaoInMemory.getInstance();
    gameDao.clear();
  }

  @Test
  public void testCreateGameIDPositive() throws DataAccessException {
    GameData game = new GameData(null, null, null, null, null);
    Integer gameID = gameDao.createGameID(game);
    assertNotNull(gameID);
  }

  @Test
  public void testCreateGamePositive() throws DataAccessException {
    GameData createdGame = gameDao.createGame("TestGame");
    assertNotNull(createdGame, "Expected game to be created and returned");
    assertEquals("TestGame", createdGame.gameName(), "Expected the game name to match");
  }

  @Test
  public void testGetGamePositive() throws DataAccessException {
    GameData createdGame = gameDao.createGame("TestGame");
    GameData retrievedGame = gameDao.getGame(createdGame.gameID());
    //assertEquals(createdGame, retrievedGame, "Expected to retrieve the same game");
  }

  @Test
  public void testGetGameNegative() {
    assertThrows(DataAccessException.class, () -> {
      gameDao.getGame(999);
    });
  }

  @Test
  public void testListGamesPositive() throws DataAccessException {
    gameDao.createGame("TestGame1");
    gameDao.createGame("TestGame2");
    Collection<GameData> games = gameDao.listGames();
    assertEquals(2, games.size(), "Expected to retrieve a list of 2 games");
  }

  @Test
  public void testAddColorPositive() throws DataAccessException {
    GameData createdGame = gameDao.createGame("TestGame");
    //gameDao.addColor(createdGame.getGameID(), "Red", "user1");
   // GameData updatedGame = gameDao.getGame(createdGame.getGameID());
    //assertTrue(updatedGame.getPlayerColors().containsKey("Red"), "Expected the game to have the 'Red' color added for user1");
  }

  @Test
  public void testAddColorNegative() {
    assertThrows(DataAccessException.class, () -> {
      gameDao.addColor(999, "Red", "user1"); // Assuming 999 is an invalid game ID
    });
  }

  @Test
  public void testClear() throws DataAccessException {
    gameDao.createGame("TestGame");
    gameDao.clear();
    assertTrue(gameDao.listGames().isEmpty(), "Expected no games to be listed after clearing");
  }
}
