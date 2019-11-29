package com.mitellius.kitpvp.listeners;

import com.mitellius.kitpvp.KitPvpPlugin;
import com.mitellius.kitpvp.player.PlayerStats;
import com.mitellius.kitpvp.selector.KitSelector;
import com.mitellius.kitpvp.scoreboard.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class KitPvpListeners implements Listener {
    private final KitPvpPlugin kitPvpPlugin;
    private final Map<UUID, PlayerStats> pending = new ConcurrentHashMap<>();

    public KitPvpListeners(KitPvpPlugin kitPvpPlugin) {
        this.kitPvpPlugin = kitPvpPlugin;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event){
        UUID playerUuid = event.getUniqueId();
        PlayerStats playerStats = kitPvpPlugin.getConnection().getPlayerDataTask(playerUuid);
        if (playerStats == null) {
            playerStats = new PlayerStats(0, 0, 0);
            kitPvpPlugin.getConnection().updatePlayerDataTask(playerUuid, playerStats);
        }
        pending.put(playerUuid, playerStats);
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event){
        Player player = event.getPlayer();
        PlayerStats playerStats = pending.get(player.getUniqueId());
        player.setMetadata(KitPvpPlugin.KITPVP_STATS_KEY, new FixedMetadataValue(kitPvpPlugin, playerStats));
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(kitPvpPlugin.getLobbySpawn());
        player.getInventory().clear();

        Scoreboard.buildSideBar(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        Player player = event.getEntity().getPlayer();
        event.getDrops().clear();

        if (player.getKiller() != null) {
            PlayerStats playerStats = (PlayerStats) player.getMetadata(KitPvpPlugin.KITPVP_STATS_KEY).get(0).value();
            playerStats.updateStats(player, playerStats, false);

            PlayerStats killerStats = (PlayerStats) killer.getMetadata(KitPvpPlugin.KITPVP_STATS_KEY).get(0).value();
            killerStats.updateStats(killer, killerStats, true);
        }
    }


    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(kitPvpPlugin.getLobbySpawn());
    }

    @EventHandler
    public void soulBound(PlayerDropItemEvent event) {
        if (!event.getItemDrop().getItemStack().hasItemMeta() && !event.getItemDrop().getItemStack().getItemMeta().hasLore()) {
            return;
        }
        if (event.getItemDrop().getItemStack().getItemMeta().getLore().contains("Soulbound")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase("Kitpvp")) {
                    KitSelector.openKitInventory(player);
                }

            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int randomInt = (int) (Math.random() * (kitPvpPlugin.getRandomSpawn().size()));
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', ChatColor.DARK_PURPLE + "Kit Selector Menu!"))) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);

                switch (event.getCurrentItem().getType()) {
                    case BOW:
                        KitSelector.giveArcherKit(player);
                        player.teleport(kitPvpPlugin.getRandomSpawn().get(randomInt));
                        break;

                    case IRON_CHESTPLATE:
                        KitSelector.giveTankKit(player);
                        player.teleport(kitPvpPlugin.getRandomSpawn().get(randomInt));
                        break;

                    case IRON_SWORD:
                        KitSelector.giveFighterKit(player);
                        player.teleport(kitPvpPlugin.getRandomSpawn().get(randomInt));
                        break;

                    default:
                        return;
                }
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerStats playerStats = (PlayerStats) player.getMetadata(KitPvpPlugin.KITPVP_STATS_KEY).get(0).value();
        kitPvpPlugin.getConnection().updatePlayerDataTask(player.getUniqueId(), playerStats);
    }
}
