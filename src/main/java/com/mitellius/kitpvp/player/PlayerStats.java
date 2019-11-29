package com.mitellius.kitpvp.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerStats {
    private int kills;
    private int deaths;
    private double coins;

    public PlayerStats(int kills, int deaths, double coins) {
        this.kills = kills;
        this.deaths = deaths;
        this.coins = coins;
    }

    /**
     * Updates the PlayerStats object for the player.
     * @param player
     * @param playerStats
     * @param playerKiller
     * @return
     */
    public PlayerStats updateStats(Player player, PlayerStats playerStats, Boolean playerKiller) {
        int currentPlayerKills, currentPlayerDeaths;
        double currentPlayerCoins, playerKDA;

        currentPlayerCoins = playerKiller ? getCoins() + 10 : getCoins() - 10;
        currentPlayerKills = playerKiller ? getKills() + 1 : getKills();
        currentPlayerDeaths = playerKiller ? getDeaths() : getDeaths() + 1;

        if (currentPlayerDeaths == 0) {
            playerKDA = currentPlayerKills;
        } else {
            playerKDA = (double) currentPlayerKills / currentPlayerDeaths;
        }

        if (playerKiller) {
            player.sendMessage(ChatColor.GOLD + "Congratulations, you just gained 10 coins!");
            player.getScoreboard().getTeam("player_kills").setSuffix(Integer.toString(currentPlayerKills));
            player.getScoreboard().getTeam("player_coins").setSuffix(Double.toString(currentPlayerCoins));
            player.getScoreboard().getTeam("player_kda").setSuffix(String.format("%.2f", playerKDA));

            setCoins(currentPlayerCoins);
            setDeaths(currentPlayerDeaths);
            setKills(currentPlayerKills);
        } else {
            player.sendMessage(ChatColor.RED + "Unfortunate, you just lost 10 coins!");
            player.getScoreboard().getTeam("player_deaths").setSuffix(Integer.toString(currentPlayerDeaths));
            player.getScoreboard().getTeam("player_coins").setSuffix(Double.toString(currentPlayerCoins));
            player.getScoreboard().getTeam("player_kda").setSuffix(String.format("%.2f", playerKDA));

            setCoins(currentPlayerCoins);
            setDeaths(currentPlayerDeaths);
            setKills(currentPlayerKills);
        }

        return playerStats;

    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public double getCoins() {
        return coins;
    }
}
