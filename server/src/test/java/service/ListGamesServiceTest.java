package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.ListGamesRequest;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

class ListGamesServiceTest {
  CreateGameService createGameService = new CreateGameService();
  ListGamesService listGamesService = new ListGamesService();
  RegisterService registerService = new RegisterService();
  RegisterRequest registerRequest = new RegisterRequest("testUser", "password123", "email");

  @Test
  public void ListGamesServiceTest() throws DataAccessException {
    String authToken = registerService.register(registerRequest).authToken();
    CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Chess1");
    createGameService.createGame(createGameRequest);
    createGameRequest = new CreateGameRequest(authToken, "Chess2");
    createGameService.createGame(createGameRequest);
    ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
    listGamesService.listGames(listGamesRequest);
  }
}