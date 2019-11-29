package com.mitellius.kitpvp;

import com.mitellius.kitpvp.commands.PlayerCommands;
import com.mitellius.kitpvp.database.SqlConnection;
import com.mitellius.kitpvp.listeners.KitPvpListeners;
import com.mitellius.kitpvp.player.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.ArrayList;

public final class KitPvpPlugin extends JavaPlugin {
    // fields
    private final SqlConnection connection = new SqlConnection(this);
    private final String world = "world";
    private ArrayList<Location> randomSpawn;
    private Location lobbySpawn;

    // static fields
    public static final String KITPVP_STATS_KEY = "mitelius.kitpvp.stats";

    @Override
    public void onEnable() {
        try {
            connection.openConnection();
            connection.checkDatabaseSetup();
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Successfully connected to the database");
        } catch (SQLException e) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Connection failed");
            e.printStackTrace();
        }
        randomSpawn = getConfig().get("RandomSpawns") == null ? new ArrayList<>() : (ArrayList<Location>) getConfig().get("RandomSpawns");
        lobbySpawn = getConfig().get("lobby") == null ? getServer().getWorld(world).getSpawnLocation() : (Location) getConfig().get("lobby");

        getServer().getPluginManager().registerEvents(new KitPvpListeners(this), this);
        getCommand("spawn").setExecutor(new PlayerCommands(this));

        saveDataRunnable();
    }

    @Override
    public void onDisable() {
        getConfig().set("lobby", lobbySpawn);
        getConfig().set("RandomSpawns", randomSpawn);

        saveConfig();
    }

    /**
     * Async task that runs every 5 min. This saves the players that are online, lobby spawn and random spawns.
     */
    public void saveDataRunnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    if (!player.hasMetadata(KITPVP_STATS_KEY)) continue;
                    PlayerStats playerStats = (PlayerStats) player.getMetadata(KitPvpPlugin.KITPVP_STATS_KEY).get(0).value();
                    connection.updatePlayerDataTask(player.getUniqueId(), playerStats);
                }
                getConfig().set("lobby", lobbySpawn);
                getConfig().set("RandomSpawns", randomSpawn);

                saveConfig();
            }
        }.runTaskTimerAsynchronously(this, 0, 6000);

    }


    public SqlConnection getConnection() {
        return connection;
    }

    public ArrayList<Location> getRandomSpawn() {
        return randomSpawn;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }
}
