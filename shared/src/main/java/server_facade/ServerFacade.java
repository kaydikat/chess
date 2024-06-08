package server_facade;

import com.google.gson.Gson;
import ResponseException.ResponseException;
import model.AuthData;
import model.GameData;
import request.*;
import result.*;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {
  private final String serverUrl;
  private final Gson gson = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

   public AuthData register(String username, String password, String email) throws ResponseException {
     RegisterRequest request = new RegisterRequest(username, password, email);
     RegisterResult result = this.makeRequest("POST", "/user", request, RegisterResult.class, null);

     if (result.message() != null && result.message().contains("error")) {
       throw new ResponseException(400, result.message());
     }

     return new AuthData(result.authToken(), result.username());
   }

  public AuthData login(String username, String password) throws ResponseException {
    LoginRequest request = new LoginRequest(username, password);
    LoginResult result = this.makeRequest("POST", "/session", request, LoginResult.class, null);

    if (result.message() != null && result.message().contains("error")) {
      throw new ResponseException(400, result.message());
    }

    return new AuthData(result.authToken(), result.username());
  }

  public AuthData logout(AuthData authData) throws ResponseException {
    LogoutRequest request = new LogoutRequest(authData.authToken());
    LogoutResult result = this.makeRequest("DELETE", "/session", request, LogoutResult.class, authData.authToken());

    return new AuthData(null, result.message());
  }

  public GameData create(String authToken, String gameName) throws ResponseException {
    CreateGameRequest request = new CreateGameRequest(authToken, gameName);
    CreateGameResult result = this.makeRequest("POST", "/game", request, CreateGameResult.class, authToken);

    if (result.message() != null && result.message().contains("error")) {
      throw new ResponseException(400, result.message());
    }

    return new GameData(result.gameID(), null, null, gameName, result.game());
  }


  public Collection<GameData> list(String authToken) throws ResponseException {
    ListGamesRequest request = new ListGamesRequest(authToken);
    ListGamesResult result = this.makeRequest("GET", "/game", request, ListGamesResult.class, authToken);

    return result.games();
  }
  public GameData join(String authToken, Integer gameID, String color) throws ResponseException {
    JoinGameRequest request = new JoinGameRequest(authToken, color, gameID);
    JoinGameResult result = this.makeRequest("PUT", "/game", request, JoinGameResult.class, authToken);


    return new GameData(gameID, result.whiteUsername(), result.blackUsername(), result.gameName(), result.game());
  }


  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      if (authToken != null) {
        http.setRequestProperty("Authorization", authToken);
      }

      if (!method.equalsIgnoreCase("GET")) {
        writeBody(request, http);
      }
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }
  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}
