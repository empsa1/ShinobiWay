package eportela.shinobiglory;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private static Connection connection;
    private String host;
    private String database;
    private String username;
    private String password;

    public DatabaseManager(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }





    /*PLUGIN DB ACCESS LOGIC*/
    public boolean connect() {
        String url = "jdbc:mysql://" + host + "/" + database + "?useSSL=false";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL database");
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from MySQL database");
            } catch (SQLException e) {
                System.err.println("Failed to disconnect from MySQL database: " + e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public boolean checkDBIntegrity() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute(ShinobiGlory.SQL_PLAYER_TABLE);
            statement.execute(ShinobiGlory.SQL_GROUPS_TABLE);
            statement.execute(ShinobiGlory.SQL_TERRITORY_TABLE);
            statement.execute(ShinobiGlory.SQL_DIPLOMACY_TABLE);
            statement.execute(ShinobiGlory.SQL_WAR_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("shinobiGlory DB entries were well stored and executed");
        return true;
    }

/*COMMANDS*/

    public boolean playerTryInvitePlayer(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryKickPlayer(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryCheckBalance(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryChangeTax(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryCheckGroups(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryDiplomacy(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryCheckGroupProfile(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryCheckDiplomacy(Player player, String[] args) throws SQLException {
        return true;
    }

    public boolean playerTryCheckTurfs(Player player, String[] args) throws SQLException {
        return true;
    }
}