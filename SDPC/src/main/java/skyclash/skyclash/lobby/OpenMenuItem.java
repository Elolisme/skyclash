package skyclash.skyclash.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONObject;

import skyclash.skyclash.main;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.kitscards.PlayerData;

public class OpenMenuItem implements Listener {
    public OpenMenuItem(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    @SuppressWarnings("unchecked")
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
        Inventory inventory = Bukkit.createInventory(player, 18, ChatColor.DARK_BLUE+"Skyclash Menu");

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

        // kits
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

        // cards
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

        // stats
        DataFiles df = new DataFiles(player);
        PlayerData pdata = df.LoadData();

        JSONObject stats = pdata.Stats;
        
        item5 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        item.setAmount(5);
        SkullMeta smeta = (SkullMeta) item5.getItemMeta();
        smeta.setOwner(player.getName().toString());
        item5.setItemMeta(smeta);
        meta5 = item5.getItemMeta();
        if (meta5 != null) {
            meta5.setDisplayName(ChatColor.GREEN + player.getName());
            List<String> lore5 = new ArrayList<>();
            lore5.add(ChatColor.GOLD+"Your coins: "+ChatColor.BLUE+(long)pdata.Coins);
            lore5.add("");
            lore5.add(ChatColor.GOLD+"Your stats:");

            Object kdr;
            Object wr;

            if (!stats.containsKey("kills")) {stats.put("kills", 0);}
            if (!stats.containsKey("deaths")) {stats.put("deaths", 0);}
            if (!stats.containsKey("wins")) {stats.put("wins", 0);}
            if (!stats.containsKey("Games")) {stats.put("Games", 0);}

            try {kdr = ((float) Math.round((long)stats.get("kills") *1000 / (long)stats.get("deaths")))/1000;} catch (Exception e) {kdr = "To infinity and beyond...";}
            try {wr = ((float)Math.round((long)stats.get("wins") *1000 / (long)stats.get("Games")))/10;} catch (Exception e) {wr = "To infinity and beyond...";}
            
            lore5.add(ChatColor.YELLOW+"KDR: "+ChatColor.BLUE+kdr+" ("+stats.get("kills")+"/"+stats.get("deaths")+")");
            stats.remove("kills");
            stats.remove("deaths");
            lore5.add(ChatColor.YELLOW+"WR: "+ChatColor.BLUE+wr+"% ("+stats.get("wins")+"/"+stats.get("Games")+")");
            stats.remove("wins");
            stats.remove("Games");
            lore5.add("");

            ArrayList<Long> sortStrings = new ArrayList<>();
            stats.forEach((stat, value) -> {sortStrings.add((Long)value);});
            List<Long> sortStrings2 = sortStrings.stream().distinct().collect(Collectors.toList());
            Collections.sort(sortStrings2);
            Collections.reverse(sortStrings2);

            sortStrings2.forEach((value) -> {
                stats.forEach((stat, svalue) -> {
                    if (value.equals((Long)svalue)) {lore5.add(ChatColor.YELLOW+(String)stat+": "+ChatColor.BLUE+stats.get(stat));}
                });
            });
            meta5.setLore(lore5);
            item5.setItemMeta(meta5);
        }
        inventory.setItem(13, item5);

        player.openInventory(inventory);
        player.setMetadata("OpenedMenu", new FixedMetadataValue(main.getPlugin(main.class), "Skyclash Menu"));

    }
}