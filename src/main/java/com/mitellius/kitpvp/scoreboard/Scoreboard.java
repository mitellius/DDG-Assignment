package com.mitellius.kitpvp.scoreboard;

import com.mitellius.kitpvp.KitPvpPlugin;
import com.mitellius.kitpvp.player.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

public class Scoreboard implements Listener {
    /**
     * Builds the scoreboard for the player.
     * @param player
     */
    public static void buildSideBar(Player player) {
        PlayerStats playerStats = (PlayerStats) player.getMetadata(KitPvpPlugin.KITPVP_STATS_KEY).get(0).value();

        org.bukkit.scoreboard.Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("Kitpvp", "dummy");
        obj.setDisplayName("-=-" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " Kit Pvp " + ChatColor.WHITE + "-=-");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score playerName = obj.getScore(ChatColor.GOLD + "Hello! " + player.getDisplayName());
        playerName.setScore(6);

        Score line = obj.getScore("----------------");
        line.setScore(5);
        Team playerCoins = board.registerNewTeam("player_coins");
        playerCoins.addEntry(ChatColor.RED.toString());
        playerCoins.setPrefix(ChatColor.YELLOW + "Coins: ");
        playerCoins.setSuffix(Double.toString(playerStats.getCoins()));
        obj.getScore(ChatColor.RED.toString()).setScore(4);

        Team playerKills = board.registerNewTeam("player_kills");
        playerKills.addEntry(ChatColor.GREEN.toString());
        playerKills.setPrefix(ChatColor.YELLOW + "Kills: ");
        playerKills.setSuffix(Integer.toString(playerStats.getKills()));
        obj.getScore(ChatColor.GREEN.toString()).setScore(3);

        Team playerDeaths = board.registerNewTeam("player_deaths");
        playerDeaths.addEntry(ChatColor.GOLD.toString());
        playerDeaths.setPrefix(ChatColor.YELLOW + "Deaths: ");
        playerDeaths.setSuffix(Integer.toString(playerStats.getDeaths()));
        obj.getScore(ChatColor.GOLD.toString()).setScore(2);

        double kda;
        if (playerStats.getDeaths() == 0) {
            kda = playerStats.getKills();
        } else {
            kda = (double) playerStats.getKills() / playerStats.getDeaths();
        }
        Team playerKDA = board.registerNewTeam("player_kda");
        playerKDA.addEntry(ChatColor.AQUA.toString());
        playerKDA.setPrefix(ChatColor.YELLOW + "KDA: ");
        playerKDA.setSuffix(String.format("%.2f", kda));
        obj.getScore(ChatColor.AQUA.toString()).setScore(1);

        player.setScoreboard(board);
    }

}
