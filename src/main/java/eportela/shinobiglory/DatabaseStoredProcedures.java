package eportela.shinobiglory;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseStoredProcedures {

    public static boolean tryRegisterPlayer(Player player) {
        try {
            if (DatabaseQueries.isPlayerRegistered(player.getUniqueId())) {
                return true;
            }
            String sql = "INSERT INTO _player_stats (player_uuid, player_kills, player_balance, group_tag, group_rank) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(sql);
            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, 0);  // Initial kills
            statement.setDouble(3, 0.0);  // Initial balance
            statement.setInt(4, 0);  // Group Tag (assuming 0 means no group)
            statement.setInt(5, 0);  // Initial group rank
            statement.executeUpdate();
            System.out.println("New player " + player.getName() + " registered successfully.");
            player.sendMessage("Welcome to the server!\nPress [arrowDown]key on your keyboard to check your player profile!");
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to register new player " + player.getName() + ": " + e.getMessage());
            return false;
        }
    }
}
