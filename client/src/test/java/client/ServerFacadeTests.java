package client;

import ResponseException.ResponseException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server_facade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        var serverUrl = "http://localhost:8080";
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(serverUrl);
    }
    @BeforeEach
    public void clear() {
        try {
            DatabaseManager.clearTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        AuthData authData = facade.register("user1", "password1", "user1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerUserAlreadyRegistered() throws Exception {
        assertThrows(ResponseException.class, () -> {
            facade.register("user1", "password1", "user1@email.com");
            facade.register("user1", "password1", "user1@email.com");
        });
    }


    @Test
    void login() throws Exception {
        AuthData authData = facade.register("user1", "password1", "user1@email.com");
        authData = facade.login("user1", "password1");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginWrongPassword() throws Exception {
        assertThrows(ResponseException.class, () -> {
            facade.register("user1", "password1", "user1@email.com");
            facade.login("user1", "wrongPassword");
        });
    }

    @Test
    void logout() throws Exception {
        AuthData authData = facade.register("user1", "password1", "user1@email.com");
        authData = facade.logout(authData);
        assertNull(authData.authToken());
    }

    @Test
    void logoutWithoutToken() throws Exception {
        AuthData authData=new AuthData(null, "user1");
        assertThrows(ResponseException.class, () -> {
            facade.logout(authData);
        });
    }

    @Test
    void create() throws Exception {
        AuthData authData=facade.register("user1", "password1", "user1@email.com");
        GameData gameData=facade.create(authData.authToken(), "game1");
        assertNotNull(gameData.gameID());
    }
}
