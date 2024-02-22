package eportela.shinobiglory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShinobiGlory extends JavaPlugin implements Listener {
    public static final double PLUGIN_VERSION = 1.0;
    public static DatabaseManager databaseManager;
    public static final double GROUP_CREATION_COST = 10000.00;
    private static final String DATABASE_NAME = "shinobiGloryDB";
    private static final String DATABASE_USER = "DEVELOPER";
    private static final String DATABASE_HOST ="localhost";
    private static final String DATABASE_PASSWORD = "PASSWORD";
    public static final int GROUP_INITIAL_MEMBER_SIZE = 3;
    public static final int FACTION_MINIMAL_TERRITORIES_ELEDGEBILITY  = 7;
    public static final int KINGDOM_MINIMAL_TERRITORIES_ELEDGEBILITY = 12;
    public static final String CURRENCY_NAME = "TO BE DEFINED";

    public static enum DIPLOMACY_STATUS {
        NEUTRAL,
        ENEMIES,
        ALLIED
    }

    public static enum PLAYER_RANK_IN_GROUP {
        MEMBER,
        OFFICER,
        ADVISOR,
        LEADER
    }

    public static enum GROUP_LEVEL {
        CREW,
        FACTION,
        KINGDOM
    }

    public static enum TERRITORY_TYPE {
        FARM,
        MILITARY,
        METROPOLIS,
        VILLAGE
    }

    public static enum TERRITORY_PRODUCTION_LEVEL {
        LEVEL1,
        LEVEL2,
        LEVEL3,
        LEVEL4,
        LEVEL5,
        LEVEL6,
        LEVEL7,
        LEVEL8
    }

    public static enum TERRITORY_DEFENSES {
        LEVEL1,
        LEVEL2,
        LEVEL3,
        LEVEL4,
        LEVEL5,
        LEVEL6,
        LEVEL7,
        LEVEL8,
        LEVEL9,
        LEVEL10
    }

    public static final String SG_HELP_MESSAGE =
            "Invalid Command!\nUSAGE:\n" +
            "/sg create <group_name> <group_acronym> <group_starting_balance>\n" +
            "/sg invite <player_name>\n" +
            "/sg kick <player_name>\n" +
            "/sg disband <group_name>\n" +
            "/sg balance\n" +
            "/sg deposit <amount>\n" +
            "/sg tax <new_tax>\n" +
            "/sg payTax <amount>\n" +
            "/sg list\n" +
            "/sg ally <group_name>\n" +
            "/sg enemy <group_name>\n" +
            "/sg neutral <group_name>\n" +
            "/sg profile\n" +
            "/sg diplomacy\n" +
            "/sg turfs\n" +
            "If you have any problems contact your friendly staff :)";

    public static final String SQL_PLAYER_TABLE =
            "CREATE TABLE IF NOT EXISTS _player_stats(" +
            "player_uuid VARCHAR(36) PRIMARY KEY, " +
            "player_kills INT, " +
            "player_balance DOUBLE, " +
            "group_tag VARCHAR(4), " +
            "GROUP_RANK INT, " +
            "FOREIGN KEY (group_tag) REFERENCES _groups(group_tag)" +
            ");";
    public static final String SQL_GROUPS_TABLE =
            "CREATE TABLE IF NOT EXISTS _groups(" +
            "group_tag VARCHAR(4) PRIMARY KEY UNIQUE, " +
            "group_name VARCHAR(16) UNIQUE, " +
            "group_owner VARCHAR(36), " +
            "group_balance DOUBLE, " +
            "member_limit INT, " +
            "group_advisor VARCHAR(36), " +
            "group_tax DOUBLE, " +
            "group_territory_number INT, " +
            "FOREIGN KEY (group_owner) REFERENCES _player_stats(player_uuid), " +
            "FOREIGN KEY (group_advisor) REFERENCES _player_stats(player_uuid)" +
            ");";
    public static final String SQL_TERRITORY_TABLE =
            "CREATE TABLE IF NOT EXISTS _territory(" +
            "territory_id INT PRIMARY KEY AUTOINCREMENT, " +
            "territory_name VARCHAR(16) UNIQUE, " +
            "territory_owner_group VARCHAR(4), " +
            "territory_x1 INT, " +
            "territory_z1 INT, " +
            "territory_x2 INT, " +
            "territory_z2 INT, " +
            "territory_type INT, " +
            "territory_production_level INT, " +
            "territory_defenses_level INT, " +
            "FOREIGN KEY (territory_owner_group REFERENCES _groups(group_tag)" +
            ");";
    public static final String SQL_DIPLOMACY_TABLE =
            "CREATE TABLE IF NOT EXISTS _diplomacy(" +
            "group_tag1 VARCHAR(4) PRIMARY KEY, " +
            "group_tag2 VARCHAR(4) PRIMARY KEY, " +
            "diplomacy_type INT, " +
            "FOREIGN KEY (group_tag1) REFERENCES _groups(group_tag), " +
            "FOREIGN KEY (group_tag2) REFERENCES _groups(group_tag)" +
            ");";
    public static final String SQL_WAR_TABLE =
            "CREATE TABLE IF NOT EXISTS _war(" +
            "territory_id INT PRIMARY KEY, " +
            "warring_group_tag VARCHAR(4) PRIMARY KEY, " +
            "war_date DATE, " +
            "war_time TIME, " +
            "FOREIGN KEY (territory_id) REFERENCES _territory(territory_id), " +
            "FOREIGN KEY (warring_group_tag) REFERENCES _groups(group_tag)" +
            ");";

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager(DATABASE_HOST, DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD);
        if (!databaseManager.connect()) {
            getLogger().warning("Failed to connect to the database. Plugin will be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return ;
        }
        if (databaseManager.checkDBIntegrity() == false)
                return ;
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("SG").setExecutor(new CommandHandler(this));
        System.out.println("Loaded plugin correctly");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        DatabaseStoredProcedures.tryRegisterPlayer(player);
    }

    @Override
    public void onDisable() {
        System.out.println("Disabled plugin correctly");
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
    }
}
