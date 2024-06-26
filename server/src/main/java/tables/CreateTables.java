package tables;

public class CreateTables {
  public CreateTables() {
  }

  public final String[] userTable() {
    return new String[] {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(50) NOT NULL,
                password VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL,
                PRIMARY KEY (username)
            )
            """
    };
  }
  public String[] authTable() {
    return new String[] {
            """
            CREATE TABLE IF NOT EXISTS auth (
                id INT NOT NULL AUTO_INCREMENT,
                authToken VARCHAR(36) NOT NULL,
                username VARCHAR(100) NOT NULL,
                PRIMARY KEY (id)
            )
            """
    };
  }

  public String[] gameTable() {
    return new String[] {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL,
                whiteUsername VARCHAR(100),
                blackUsername VARCHAR(100),
                gameName VARCHAR(100) NOT NULL,
                game LONGTEXT NOT NULL,
                PRIMARY KEY (gameID)
            )
            """
    };
  }

  public String[] allTables() {
    return new String[] {
            userTable()[0],
            authTable()[0],
            gameTable()[0]
    };
  }
}
