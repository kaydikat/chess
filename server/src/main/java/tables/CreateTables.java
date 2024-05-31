package tables;

public class CreateTables {
  public CreateTables() {
  }

  public final String[] UserTable() {
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
  public String[] AuthTable() {
    return new String[] {
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(36) NOT NULL,
                username VARCHAR(100) NOT NULL,
                PRIMARY KEY (authToken)
            )
            """
    };
  }

  public String[] GameTable() {
    return new String[] {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT,
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
            UserTable()[0],
            AuthTable()[0],
            GameTable()[0]
    };
  }
}
