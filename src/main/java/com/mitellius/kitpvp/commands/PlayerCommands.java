package com.mitellius.kitpvp.commands;

import com.mitellius.kitpvp.KitPvpPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands implements CommandExecutor {
    private final KitPvpPlugin kitPvpPlugin;

    public PlayerCommands(KitPvpPlugin kitPvpPlugin) {
        this.kitPvpPlugin = kitPvpPlugin;
    }


    /**
     * Creates the spawn command with her subcommands.
     * @param commandSender
     * @param command
     * @param s
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("spawn")) {
                if (args.length == 0) {
                    StringBuilder commandListBuilder = new StringBuilder("-=- Spawn commands -=- \n");
                    commandListBuilder.append("Usage: /Spawn list\n Returns the list of ingame spawns. \n" +
                            "Usage: /Spawn setSpawn \n Sets at your current location a ingame spawnpoint.\n" +
                            "Usage: /Spawn setLobby \n Sets at your current location the lobby spawnpoint.\n" +
                            "Usage: /Spawn removeSpawn {index} \n Removes a ingame spawnpoint based on your index. Check the spawn list for more information!");
                    player.sendMessage(commandListBuilder.toString());

                } else if (args.length == 1 || args[0].equalsIgnoreCase("removeSpawn")) {
                    if (args[0].equalsIgnoreCase("setLobby")) {
                        kitPvpPlugin.setLobbySpawn(player.getLocation());
                        player.sendMessage(ChatColor.GOLD + "Lobby spawn has been set!");

                    } else if (args[0].equalsIgnoreCase("setSpawn")) {
                        kitPvpPlugin.getRandomSpawn().add(player.getLocation());
                        player.sendMessage(ChatColor.GOLD + "Spawn has been set!");

                    } else if (args[0].equalsIgnoreCase("list")) {
                        StringBuilder spawnListBuilder = new StringBuilder("-=- Spawn list -=- \n");
                        if (kitPvpPlugin.getRandomSpawn() != null && kitPvpPlugin.getRandomSpawn().size() != 0) {
                            int index = 0;
                            for (Location location : kitPvpPlugin.getRandomSpawn()) {
                                spawnListBuilder.append(index + ": " + location.getWorld().getName() +
                                        ", " + String.format("%.1f", location.getX()) +
                                        ", " + String.format("%.1f", location.getY()) +
                                        ", " + String.format("%.1f", location.getZ()) +
                                        "\n");
                                index++;
                            }
                            player.sendMessage(spawnListBuilder.toString());

                        } else {
                            player.sendMessage(ChatColor.RED + "No spawns have been set yet!");
                        }
                    } else if (args[0].equalsIgnoreCase("removeSpawn")) {
                        if (args.length == 1) {
                            commandSender.sendMessage(ChatColor.RED + "Please specify which spawn needs to be removed!");
                        } else {
                            try {
                                int index = Integer.parseInt(args[1]);
                                try {
                                    kitPvpPlugin.getRandomSpawn().remove(index);
                                    commandSender.sendMessage(ChatColor.GOLD + "Spawn with index: " + index + " has been removed!");
                                } catch (IndexOutOfBoundsException error) {
                                    commandSender.sendMessage(ChatColor.RED + "This spawn does not exist please check the spawn list for more info!");
                                }

                            } catch (NumberFormatException error) {
                                commandSender.sendMessage(ChatColor.RED + "Please specify with a number!");
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "This sub-command doesn't exist!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Too many arguments given!");
                }
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Only players can use these commands");
        }
        return true;
    }
}
