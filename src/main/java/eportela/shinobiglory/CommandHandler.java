package eportela.shinobiglory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class CommandHandler implements CommandExecutor {

    private final ShinobiGlory plugin;

    public CommandHandler(ShinobiGlory plugin) {
        this.plugin = plugin;
    }

    private boolean warSubCommandHandler(Player player, String[] args) {
        return true;
    }

    private boolean sgSubCommandHandler(Player player, String[] args) {
        DatabaseManager db = ShinobiGlory.databaseManager;
        if (args[2] != null && args[2].length() != 0) {
            try {
                if (args[2].equalsIgnoreCase("create")) {
                    return (DatabaseFunctions.playerTryCreateGroup(player, args));
                } else if (args[2].equalsIgnoreCase("invite")) {
                    return (db.playerTryInvitePlayer(player, args));
                } else if (args[2].equalsIgnoreCase("kick")) {
                    return (db.playerTryKickPlayer(player, args));
                } else if (args[2].equalsIgnoreCase("balance")) {
                    return (db.playerTryCheckBalance(player, args));
                } else if (args[2].equalsIgnoreCase("deposit")) {
                    return (DatabaseFunctions.playerTryDeposit(player, args));
                } else if (args[2].equalsIgnoreCase("tax")) {
                    return (db.playerTryChangeTax(player, args));
                } else if (args[2].equalsIgnoreCase("payTax")) {
                    return (DatabaseFunctions.playerTryPayTax(player, args));
                } else if (args[2].equalsIgnoreCase("list")) {
                    return (db.playerTryCheckGroups(player, args));
                } else if (args[2].equalsIgnoreCase("ally") || args[2].equalsIgnoreCase("enemy") ||
                        args[2].equalsIgnoreCase("neutral")) {
                    return (db.playerTryDiplomacy(player, args));
                } else if (args[2].equalsIgnoreCase("profile")) {
                    return (db.playerTryCheckGroupProfile(player, args));
                } else if (args[2].equalsIgnoreCase("diplomacy")) {
                    return (db.playerTryCheckDiplomacy(player, args));
                } else if (args[2].equalsIgnoreCase("turfs")) {
                    return (db.playerTryCheckTurfs(player, args));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        player.sendMessage(ShinobiGlory.SG_HELP_MESSAGE);
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return false;
        }
        Player player = (Player) sender;

        if (args[1].length() != 0 && args[1] != null) {
            if (command.getName().equalsIgnoreCase("SG")) {
                return (sgSubCommandHandler(player, args));
            }
            else if (command.getName().equalsIgnoreCase("WAR")) {
                return (warSubCommandHandler(player, args));
            }
            else if (command.getName().equalsIgnoreCase("HELP")) {
                player.sendMessage(ShinobiGlory.SG_HELP_MESSAGE);
                return true;
            }
            player.sendMessage("Invalid Command!");
            return false;
        }
        player.sendMessage("Something went wrong");
        return false;
    }
}