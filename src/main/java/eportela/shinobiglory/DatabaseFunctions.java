package eportela.shinobiglory;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseFunctions {
    public static boolean changePlayerBalance(UUID playerUUID, double change) throws SQLException {
        return true;
    }

    public static boolean playerTryDeposit(Player player, String[] args) throws SQLException {
        return true;
    }

    public static boolean playerTryPayTax(Player player, String[] args) throws SQLException {
        return true;
    }

    public static boolean playerTryCreateGroup(Player player, String[] args) throws SQLException {
        if (!DatabaseQueries.isPlayerRegistered(player.getUniqueId())) {
            player.sendMessage("Please contact staff, you were not automatically registered in the shinobiGlory Database");
            System.out.println("A player by name: " + player.getDisplayName() + " tried to execute a shinobiGlory command " +
                    "without being automatically registered in the shinobiGlory Database");
            return false;
        }
        if (DatabaseQueries.isPlayerInGroup(player.getUniqueId())) {
            player.sendMessage("You are already apart of a group please leave your group before creating a new one!");
            return false;
        }
        if (DatabaseQueries.getPlayerBalance(player.getUniqueId()) < ShinobiGlory.GROUP_CREATION_COST) {
            player.sendMessage("You need to have at least: " + ShinobiGlory.GROUP_CREATION_COST + " in your account to be able to create a new group!");
            return false;
        }
        String groupName = args[3];
        String groupTag = args[4];

        try {
            String sql = "INSERT INTO _groups (group_name, group_tag, group_owner, group_balance, member_limit, group_advisor) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(sql);
            statement.setString(1, groupName);
            statement.setString(1, groupTag);
            statement.setString(2, player.getUniqueId().toString());
            statement.setDouble(3, ShinobiGlory.GROUP_CREATION_COST);
            statement.setInt(4, ShinobiGlory.GROUP_INITIAL_MEMBER_SIZE);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0 && changePlayerBalance(player.getUniqueId(), -ShinobiGlory.GROUP_CREATION_COST)) {
                player.sendMessage("You have successfully created the group: " + groupName);
                return true;
            } else {
                player.sendMessage("Failed to create the group. Please try again later.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Failed to create the group. Please try again later.");
            return false;
        }
    }
}
