package eportela.shinobiglory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseQueries {
    public static boolean isPlayerRegistered(UUID playerUUID) {
        try {
            String sql = "SELECT * FROM _player_stats WHERE player_uuid = ?";
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(sql);
            statement.setString(1, playerUUID.toString());
            return statement.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Failed to check if player " + playerUUID + " is registered: " + e.getMessage());
            return false;
        }
    }

    public static boolean isPlayerInGroup(UUID playerUUID) throws SQLException {
        try {
            String sql = "SELECT COUNT(*) AS count FROM _groups WHERE group_owner = ?";
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(sql);
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            } else {
                throw new SQLException("Failed to check if player is in a group.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to check if player is in a group: " + e.getMessage());
            throw e; // Re-throw the exception to be handled in the calling method if needed
        }
    }

    public static double getPlayerBalance(UUID playerUUID) throws SQLException {
        try {
            String sql = "SELECT player_balance FROM _player_stats WHERE player_uuid = ?";
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(sql);
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("player_balance");
            } else {
                throw new SQLException("Player not found in the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to get player balance: " + e.getMessage());
            throw e; // Re-throw the exception to be handled in the calling method if needed
        }
    }
}
