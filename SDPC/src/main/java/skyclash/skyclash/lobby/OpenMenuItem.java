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
import org.bukkit.event.block.Action;
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
    public void onUseItem(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {return;}
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
        ItemStack item;
        ItemMeta meta;

        // player and stats
        inventory.setItem(4, statItem(player));

        // vote for maps
        item = new ItemStack(Material.PAPER);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "Vote For a Map");
            List<String> lore = new ArrayList<>();
            lore.add("Click to access available maps");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(9, item);

        // not ready
        item = new Wool(DyeColor.RED).toItemStack();
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "You are not ready");
            List<String> lore2 = new ArrayList<>();
            lore2.add("Click to become ready");
            meta.setLore(lore2);
            item.setItemMeta(meta);
        }

        // ready
        ItemStack item1 = new Wool(DyeColor.GREEN).toItemStack();
        item1.setAmount(1);
        meta = item1.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "You are ready");
            List<String> lore3 = new ArrayList<>();
            lore3.add("Click to no longer be ready");
            meta.setLore(lore3);
            item1.setItemMeta(meta);
        }

        // spectate
        ItemStack item2 = new Wool(DyeColor.GRAY).toItemStack();
        item2.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GRAY + "Spectate Active Game");
            List<String> lore6 = new ArrayList<>();
            lore6.add("Click to spectate current game");
            meta.setLore(lore6);
            item2.setItemMeta(meta);
        }

        if (main.playerStatus.get(player.getName()).equals("lobby") && !main.isGameActive) {
            inventory.setItem(11, item);
        }
        else if (main.playerStatus.get(player.getName()).equals("ready") && !main.isGameActive){
            inventory.setItem(11, item1);
        } else {
            inventory.setItem(11, item2);
        }

        // shop
        item = new ItemStack(Material.GOLD_INGOT);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Shop");
            List<String> lore4 = new ArrayList<>();
            lore4.add("Buy temporary buffs");
            meta.setLore(lore4);
            item.setItemMeta(meta);
        }
        inventory.setItem(13, item);

        // kits
        item = new ItemStack(Material.DIAMOND_SWORD, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.BLUE + "Select Kit");
            List<String> lore4 = new ArrayList<>();
            lore4.add("Click to change your kit");
            meta.setLore(lore4);
            item.setItemMeta(meta);
        }
        inventory.setItem(15, item);

        // cards
        item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Select Card");
            List<String> lore5 = new ArrayList<>();
            lore5.add("Click to access your cards");
            meta.setLore(lore5);
            item.setItemMeta(meta);
        }
        inventory.setItem(17, item);

        // set inventory
        player.openInventory(inventory);
        player.setMetadata("OpenedMenu", new FixedMetadataValue(main.getPlugin(main.class), "Skyclash Menu"));

    }

    @SuppressWarnings("unchecked")
    private ItemStack statItem(Player player) {
        ItemStack item;
        ItemMeta meta;

        // stats
        DataFiles df = new DataFiles(player);
        PlayerData pdata = df.LoadData();

        JSONObject stats = pdata.Stats;
        item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta smeta = (SkullMeta) item.getItemMeta();
        smeta.setOwner(player.getName().toString());
        item.setItemMeta(smeta);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + player.getName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD+"Your coins: "+ChatColor.BLUE+(long)pdata.Coins);
            lore.add("");
            lore.add(ChatColor.GOLD+"Your stats:");

            Object kdr;
            Object wr;

            if (!stats.containsKey("kills")) {stats.put("kills", 0);}
            if (!stats.containsKey("deaths")) {stats.put("deaths", 0);}
            if (!stats.containsKey("wins")) {stats.put("wins", 0);}
            if (!stats.containsKey("Games")) {stats.put("Games", 0);}

            try {kdr = ((float) Math.round((long)stats.get("kills") *1000 / (long)stats.get("deaths")))/1000;} catch (Exception e) {kdr = "To infinity and beyond...";}
            try {wr = ((float)Math.round((long)stats.get("wins") *1000 / (long)stats.get("Games")))/10;} catch (Exception e) {wr = "To infinity and beyond...";}
            
            lore.add(ChatColor.YELLOW+"KDR: "+ChatColor.BLUE+kdr+" ("+stats.get("kills")+"/"+stats.get("deaths")+")");
            stats.remove("kills");
            stats.remove("deaths");
            lore.add(ChatColor.YELLOW+"WR: "+ChatColor.BLUE+wr+"% ("+stats.get("wins")+"/"+stats.get("Games")+")");
            stats.remove("wins");
            stats.remove("Games");
            lore.add("");

            ArrayList<Long> sortStrings = new ArrayList<>();
            stats.forEach((stat, value) -> {sortStrings.add((Long)value);});
            List<Long> sortStrings2 = sortStrings.stream().distinct().collect(Collectors.toList());
            Collections.sort(sortStrings2);
            Collections.reverse(sortStrings2);

            sortStrings2.forEach((value) -> {
                stats.forEach((stat, svalue) -> {
                    if (value.equals((Long)svalue)) {lore.add(ChatColor.YELLOW+(String)stat+": "+ChatColor.BLUE+stats.get(stat));}
                });
            });
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}