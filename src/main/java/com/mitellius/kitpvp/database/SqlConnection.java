package com.mitellius.kitpvp.database;

import com.mitellius.kitpvp.KitPvpPlugin;
import com.mitellius.kitpvp.player.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.UUID;

public class SqlConnection {
    private static Connection connection;
    private KitPvpPlugin kitPvpPlugin;
    private String database;

    public SqlConnection(KitPvpPlugin kitPvpPlugin) {
        this.kitPvpPlugin = kitPvpPlugin;
    }

    /**
     * Opens the database connection with the plugin.
     * @throws SQLException
     */
    public void openConnection() throws SQLException {
        if (kitPvpPlugin.getConfig().isSet("databaseSettings.host") &&
                kitPvpPlugin.getConfig().isSet("databaseSettings.port") &&
                kitPvpPlugin.getConfig().isSet("databaseSettings.username") &&
                kitPvpPlugin.getConfig().isSet("databaseSettings.database")) {

            database = kitPvpPlugin.getConfig().getString("databaseSettings.database");
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            kitPvpPlugin.getConfig().getString("databaseSettings.host") + ":" +
                            kitPvpPlugin.getConfig().getString("databaseSettings.port") + "/" +
                            this.database,
                    kitPvpPlugin.getConfig().getString("databaseSettings.username"),
                    kitPvpPlugin.getConfig().getString("databaseSettings.password"));

        } else {
            kitPvpPlugin.getConfig().set("databaseSettings.text", "Note that if you have a password you need to add the password: {password}");
            kitPvpPlugin.getConfig().set("databaseSettings.host", "hostname");
            kitPvpPlugin.getConfig().set("databaseSettings.port", "portnumber");
            kitPvpPlugin.getConfig().set("databaseSettings.database", "databasename");
            kitPvpPlugin.getConfig().set("databaseSettings.username", "username");
            kitPvpPlugin.getConfig().set("databaseSettings.password", "password");
            kitPvpPlugin.saveConfig();
            kitPvpPlugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Check your database settings!");
        }
    }

    /**
     * Retrieves data based on the uuid of the player and returns a playerStats Object.
     *
     * @param playerUuid
     * @return
     */
    public PlayerStats getPlayerDataTask(UUID playerUuid) {
        PlayerStats playerStats = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE uuid = ? ;");
            statement.setString(1, playerUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                playerStats = new PlayerStats(resultSet.getInt("kills"), resultSet.getInt("deaths"), resultSet.getDouble("coins"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerStats;
    }

    /**
     * updates the player if the player doesn't exist insert the new player with default values.
     *
     * @param uuid
     * @param playerStats
     */
    public void updatePlayerDataTask(UUID uuid, PlayerStats playerStats) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO player (uuid, kills, deaths, coins) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE kills = VALUES(kills), deaths = VALUES(deaths), coins = VALUES(coins) ;");
                    statement.setString(1, uuid.toString());
                    statement.setInt(2, playerStats.getKills());
                    statement.setInt(3, playerStats.getDeaths());
                    statement.setDouble(4, playerStats.getCoins());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskAsynchronously(kitPvpPlugin);
    }

    /**
     * Closes the connection with the database.
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Checks if the table exists if not create the table
     */
    public void checkDatabaseSetup() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS player (\n" +
                    "  playerid int NOT NULL AUTO_INCREMENT,\n" +
                    "  uuid VARCHAR(36) NOT NULL UNIQUE,\n" +
                    "  kills int(11) NOT NULL DEFAULT 0,\n" +
                    "  deaths int(11) NOT NULL DEFAULT 0,\n" +
                    "  coins DOUBLE NOT NULL DEFAULT 0,\n" +
                    "  PRIMARY KEY (playerid)\n" +
                    ");").execute();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

}
