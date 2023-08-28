package skyclash.skyclash.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import skyclash.skyclash.main;

import java.util.ArrayList;
import java.util.List;

public class OpenMenuItem implements Listener {
    public OpenMenuItem(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.containsKey(player.getName())) {
            main.playerStatus.put(player.getName(), "lobby");
        }
        if (!main.playerStatus.get(player.getName()).equals("lobby")) {
            if (!main.playerStatus.get(player.getName()).equals("ready")) {
                return;
            }
        }
        if (player.getItemInHand().getType() != Material.NETHER_STAR) {
            return;
        }
        if (!player.getItemInHand().hasItemMeta()) {
            return;
        }


        if (!player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Skyclash Menu")) {
            return;
        }

        // Main menu
        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.DARK_BLUE+"Skyclash Menu");

        // item 1
        ItemStack item = new ItemStack(Material.PAPER);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Vote For a Map");
            List<String> lore = new ArrayList<>();
            lore.add("Click to access available maps");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(1, item);

        // item 2a
        ItemStack item2 = new Wool(DyeColor.RED).toItemStack();
        item2.setAmount(1);
        ItemMeta meta2 = item.getItemMeta();
        if (meta2 != null) {
            meta2.setDisplayName(ChatColor.RED + "Ready up for a game");
            List<String> lore2 = new ArrayList<>();
            lore2.add("Click to become ready");
            meta2.setLore(lore2);
            item2.setItemMeta(meta2);
        }

        // item 2b
        ItemStack item3 = new Wool(DyeColor.GREEN).toItemStack();
        item3.setAmount(1);
        ItemMeta meta3 = item.getItemMeta();
        if (meta3 != null) {
            meta3.setDisplayName(ChatColor.GREEN + "You are ready for a game");
            List<String> lore3 = new ArrayList<>();
            lore3.add("Click to no longer be ready");
            meta3.setLore(lore3);
            item3.setItemMeta(meta3);
        }

        // item 2c
        ItemStack item6 = new Wool(DyeColor.GRAY).toItemStack();
        item6.setAmount(1);
        ItemMeta meta6 = item.getItemMeta();
        if (meta6 != null) {
            meta6.setDisplayName(ChatColor.GREEN + "Spectate Active Game");
            List<String> lore6 = new ArrayList<>();
            lore6.add("Click to spectate current game");
            meta6.setLore(lore6);
            item6.setItemMeta(meta6);
        }

        if (main.playerStatus.get(player.getName()).equals("ready") && !main.isGameActive) {
            inventory.setItem(3, item3);
        }
        else if (main.playerStatus.get(player.getName()).equals("lobby") && !main.isGameActive){
            inventory.setItem(3, item2);
        } else {
            inventory.setItem(3, item6);
        }

        // item 4
        ItemStack item4 = new ItemStack(Material.DIAMOND_SWORD);
        item.setAmount(4);
        ItemMeta meta4 = item.getItemMeta();
        if (meta4 != null) {
            meta4.setDisplayName(ChatColor.YELLOW + "Select Kit");
            List<String> lore4 = new ArrayList<>();
            lore4.add("Click to change your kit");
            meta4.setLore(lore4);
            item4.setItemMeta(meta4);
        }
        inventory.setItem(5, item4);

        // item 5
        ItemStack item5 = new ItemStack(Material.ENCHANTED_BOOK);
        item.setAmount(5);
        ItemMeta meta5 = item.getItemMeta();
        if (meta5 != null) {
            meta5.setDisplayName(ChatColor.YELLOW + "Select Card");
            List<String> lore5 = new ArrayList<>();
            lore5.add("Click to access your cards");
            meta5.setLore(lore5);
            item5.setItemMeta(meta5);
        }
        inventory.setItem(7, item5);


        player.openInventory(inventory);
        player.setMetadata("OpenedMenu", new FixedMetadataValue(main.getPlugin(main.class), "Skyclash Menu"));

    }
}