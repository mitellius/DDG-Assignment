package com.mitellius.kitpvp.selector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KitSelector {
    /**
     * Opens a new Kit Inventory where players can pick a kit.
     * @param player
     */
    public static void openKitInventory(Player player){
        Inventory kitSelector = Bukkit.createInventory(null, 9, ChatColor.DARK_PURPLE + "Kit Selector Menu!");
        ArrayList<String> archerLore = new ArrayList<>();
        ArrayList<String> tankLore = new ArrayList<>();
        ArrayList<String> fighterLore = new ArrayList<>();

        ItemStack archerStack = new ItemStack(Material.BOW);
        ItemStack tankStack = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack fighterStack = new ItemStack(Material.IRON_SWORD);

        ItemMeta archerMeta = archerStack.getItemMeta();
        ItemMeta tankMeta = tankStack.getItemMeta();
        ItemMeta fighterMeta = fighterStack.getItemMeta();

        archerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        tankMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        fighterMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        archerLore.add(ChatColor.LIGHT_PURPLE + "Bow and some arrows!");
        tankLore.add(ChatColor.LIGHT_PURPLE + "Heavy specified gear!");
        fighterLore.add(ChatColor.LIGHT_PURPLE + "Uses the art of the warrior!");

        archerMeta.setLore(archerLore);
        tankMeta.setLore(tankLore);
        fighterMeta.setLore(fighterLore);

        archerMeta.setDisplayName(ChatColor.GOLD + "Archer Kit");
        tankMeta.setDisplayName(ChatColor.GOLD + "Tank Kit");
        fighterMeta.setDisplayName(ChatColor.GOLD + "Fighter Kit");

        archerStack.setItemMeta(archerMeta);
        tankStack.setItemMeta(tankMeta);
        fighterStack.setItemMeta(fighterMeta);

        kitSelector.setItem(0, archerStack);
        kitSelector.setItem(4, tankStack);
        kitSelector.setItem(8, fighterStack);

        player.openInventory(kitSelector);

    }

    /**
     * Gives the ArcherKit to the Player
     * @param player
     */

    public static void giveArcherKit(Player player){
        player.getInventory().clear();
        ItemStack archerBowStack = new ItemStack(Material.BOW);
        ItemStack archerArrowsStack = new ItemStack(Material.ARROW);
        ItemStack archerHelmetStack = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemStack archerChestplateStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack archerLeggingsStack = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack archerBootsStack = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack archerSwordStack = new ItemStack(Material.STONE_SWORD);

        archerBowStack.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        player.getInventory().setHelmet(archerHelmetStack);
        player.getInventory().setChestplate(archerChestplateStack);
        player.getInventory().setLeggings(archerLeggingsStack);
        player.getInventory().setBoots(archerBootsStack);
        player.getInventory().setItemInMainHand(archerSwordStack);
        player.getInventory().setItem(1, archerBowStack);
        player.getInventory().setItem(34, archerArrowsStack);
    }

    /**
     * Gives the TankKit to the Player
     * @param player
     */
    public static void giveTankKit(Player player){
        player.getInventory().clear();
        ItemStack tankSwordStack = new ItemStack(Material.STONE_SWORD);
        ItemStack tankChestplateStack = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack tankLeggingsStack = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack tankBootsStack = new ItemStack(Material.LEATHER_BOOTS);

        player.getInventory().setItemInMainHand(tankSwordStack);
        player.getInventory().setChestplate(tankChestplateStack);
        player.getInventory().setLeggings(tankLeggingsStack);
        player.getInventory().setBoots(tankBootsStack);
    }
    /**
     * Gives the FighterKit to the Player
     * @param player
     */
    public static void giveFighterKit(Player player){
        player.getInventory().clear();
        ItemStack fighterSwordStack = new ItemStack(Material.IRON_SWORD);
        ItemStack fighterShieldStack = new ItemStack(Material.SHIELD);
        ItemStack fighterChestplateStack = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack fighterLeggingsStack = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack fighterBootsStack = new ItemStack(Material.LEATHER_BOOTS);

        player.getInventory().setItemInMainHand(fighterSwordStack);
        player.getInventory().setItemInOffHand(fighterShieldStack);
        player.getInventory().setChestplate(fighterChestplateStack);
        player.getInventory().setLeggings(fighterLeggingsStack);
        player.getInventory().setBoots(fighterBootsStack);
    }

}
