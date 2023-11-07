package skyclash.skyclash.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.SpawnEgg;
import org.bukkit.material.Wool;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.json.simple.JSONObject;

import skyclash.skyclash.main;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.fileIO.PlayerData;

public class Inventories {
    public static Inventory mainInventory(Player player) {
        // Main menu
        Inventory inventory = Bukkit.createInventory(player, 18, ChatColor.DARK_BLUE+"Skyclash Menu");
        ItemStack item;
        ItemMeta meta;

        DataFiles datafiles = new DataFiles(player);
        PlayerData playerdata = datafiles.data;

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
            meta.setDisplayName(ChatColor.RED + "Not Ready");
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
            meta.setDisplayName(ChatColor.GREEN + "Ready");
            List<String> lore3 = new ArrayList<>();
            lore3.add("Click to be on Autoready");
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

        // autoready
        ItemStack item3 = new Wool(DyeColor.BLUE).toItemStack();
        item3.setAmount(1);
        meta = item3.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.BLUE + "Autoready");
            List<String> lore7 = new ArrayList<>();
            lore7.add("Click to no longer be ready");
            meta.setLore(lore7);
            item3.setItemMeta(meta);
        }

        // Not in skyclash
        ItemStack item4 = new Wool(DyeColor.WHITE).toItemStack();
        item4.setAmount(1);
        meta = item4.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "You are not in skyclash");
            List<String> lore8 = new ArrayList<>();
            lore8.add("use /hub to go back to skyclash");
            meta.setLore(lore8);
            item4.setItemMeta(meta);
        }

        if (main.playerStatus.get(player.getName()).equals("lobby") && !main.isGameActive) {
            inventory.setItem(11, item);
        } else if (main.playerStatus.get(player.getName()).equals("ready") && !main.isGameActive){
            if (playerdata.Autoready == false) {
                inventory.setItem(11, item1);
            } else {
                inventory.setItem(11, item3);
            }
        } else if (main.isGameActive) {
            inventory.setItem(11, item2);
        } else {
            inventory.setItem(11, item4);
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

        return inventory;
    }


    public static Inventory kitsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 36, ChatColor.DARK_BLUE+"Kit Selection");

        ItemStack item;
        ItemMeta meta;

        item = new ItemStack(Material.BOW);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Archer");
            item.setItemMeta(meta);
        }
        inventory.setItem(0, item);

        item = new Potion(PotionType.INVISIBILITY).toItemStack(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Assassin");
            item.setItemMeta(meta);
        }
        inventory.setItem(1, item);

        item = new Potion(PotionType.STRENGTH).toItemStack(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Berserker");
            item.setItemMeta(meta);
        }
        inventory.setItem(2, item);

        item = new ItemStack(Material.GOLDEN_APPLE);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Cleric");
            item.setItemMeta(meta);
        }
        inventory.setItem(3, item);

        item = new ItemStack(Material.SNOW_BALL);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Frost_Knight");
            item.setItemMeta(meta);
        }
        inventory.setItem(4, item);

        item = new ItemStack(Material.IRON_CHESTPLATE);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Guardian");
            item.setItemMeta(meta);
        }
        inventory.setItem(5, item);

        // jumpman
        item = new ItemStack(Material.SLIME_BLOCK, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Jumpman");
            item.setItemMeta(meta);
        }
        inventory.setItem(6, item);

        item = new SpawnEgg(EntityType.SKELETON).toItemStack(1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Necromancer");
            item.setItemMeta(meta);
        }
        inventory.setItem(7, item);
        
        // swordsman
        item = new ItemStack(Material.IRON_SWORD);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Swordsman");
            item.setItemMeta(meta);
        }
        inventory.setItem(8, item);

        item = new ItemStack(Material.GOLD_INGOT);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Treasure_hunter");
            item.setItemMeta(meta);
        }
        inventory.setItem(9, item);

        item = new Potion(PotionType.SPEED).toItemStack(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Scout");
            item.setItemMeta(meta);
        }
        inventory.setItem(10, item);

        item = new ItemStack(Material.WATCH);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Jester");
            item.setItemMeta(meta);
        }
        inventory.setItem(11, item);

        for (int i=27; i<36; i++) {
            FillerItems(inventory, i);
        }
        
        item = new ItemStack(Material.COMPASS, 1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Information");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN+"Kits are the main architect or class you are playing.");
            lore.add(ChatColor.DARK_GREEN+"Click on a kit to select it.");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(31, item);

        return inventory;
    }

    // cards
    public static Inventory cardsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 36, ChatColor.DARK_BLUE+"Card Selection");

        ItemStack item;
        ItemMeta meta;

        item = new ItemStack(Material.CHEST, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Apple Finder");
            item.setItemMeta(meta);
        }
        inventory.setItem(0, item);

        item = new ItemStack(Material.TNT);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Bigger Bangs");
            item.setItemMeta(meta);
        }
        inventory.setItem(1, item);

        item = new Potion(PotionType.INSTANT_DAMAGE).toItemStack(1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Damage Potion");
            item.setItemMeta(meta);
        }
        inventory.setItem(2, item);

        item = new ItemStack(Material.BOW, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Elven Archer");
            item.setItemMeta(meta);
        }
        inventory.setItem(3, item);

        item = new ItemStack(Material.IRON_BOOTS, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Hit and Run");
            item.setItemMeta(meta);
        }
        inventory.setItem(4, item);

        item = new SpawnEgg(EntityType.ZOMBIE).toItemStack(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Pacify");
            item.setItemMeta(meta);
        }
        inventory.setItem(5, item);

        item = new ItemStack(Material.ENDER_PEARL, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Pearl Absorption");
            item.setItemMeta(meta);
        }
        inventory.setItem(6, item);
        
        item = new ItemStack(Material.SUGAR, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Sugar Rush");
            item.setItemMeta(meta);
        }
        inventory.setItem(7, item);
        
        item = new ItemStack(Material.IRON_CHESTPLATE, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Blast Protection");
            item.setItemMeta(meta);
        }
        inventory.setItem(9, item);

        item = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Creeper");
            item.setItemMeta(meta);
        }
        inventory.setItem(10, item);

        item = new ItemStack(Material.REDSTONE, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Lifesteal");
            item.setItemMeta(meta);
        }
        inventory.setItem(11, item);

        item = new ItemStack(Material.ROTTEN_FLESH, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Monster Hunter");
            item.setItemMeta(meta);
        }
        inventory.setItem(12, item);
        
        item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Quiver Refill");
            item.setItemMeta(meta);
        }
        inventory.setItem(13, item);

        for (int i=27; i<36; i++) {
            FillerItems(inventory, i);
        }
        
        item = new ItemStack(Material.COMPASS, 1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Information");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN+"Cards are passive buffs or items for an entire game.");
            lore.add(ChatColor.DARK_GREEN+"The top row will cost 15 coins each game to play");
            lore.add(ChatColor.DARK_GREEN+"    and 150 coins to permanently buy.");
            lore.add(ChatColor.DARK_GREEN+"The next row will cost 22 coins each game to play");
            lore.add(ChatColor.DARK_GREEN+"    and 220 coins to permanently buy.");
            lore.add(ChatColor.GREEN+"Click \"Buy Mode\" to permanently buy a card");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(30, item);

        item = new ItemStack(Material.BARRIER, 1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "No Card");
            item.setItemMeta(meta);
        }
        inventory.setItem(31, item);

        item = new Wool(DyeColor.GRAY).toItemStack(1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.BLUE + "Click for Buy Mode");
            item.setItemMeta(meta);
        }
        inventory.setItem(32, item);
        return inventory;
    }

    public static Inventory shopInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.DARK_BLUE+"Shop");

        ItemStack item;
        ItemMeta meta;
        DataFiles datafiles = new DataFiles(player);

        item = new ItemStack(Material.GOLDEN_APPLE);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "+1 Golden Apple");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD+"10");
            lore.add(ChatColor.GRAY+"Golden apple at the start of the next game");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(0, item);


        // Change item to green wool if bought
        for (int i = 0; i <= 17; i++) {
            if (inventory.getItem(i) != null && inventory.getItem(i).hasItemMeta()) {
                if (datafiles.data.Owned.contains(ChatColor.stripColor(inventory.getItem(i).getItemMeta().getDisplayName()))) {
                    inventory.getItem(i).setType(Material.WOOL);
                    inventory.getItem(i).setDurability((short)5);
                    ItemMeta meta1 = inventory.getItem(i).getItemMeta();
                    List<String> Lore = new ArrayList<>();
                    Lore.add("Already bought");
                    meta1.setLore(Lore);
                    inventory.getItem(i).setItemMeta(meta1);
                }
            }
        }

        for (int i=18; i<27; i++) {
            FillerItems(inventory, i);
        }
        
        item = new ItemStack(Material.COMPASS, 1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Information");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN+"Buy items to gain a temporary advantage over others");
            lore.add(ChatColor.DARK_GREEN+"Click an item to buy it, lasts one game");
            lore.add(ChatColor.YELLOW+"Coins: "+datafiles.data.Coins);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(22, item);

        return inventory;
    }

    @SuppressWarnings("unchecked")
    public static Inventory mapsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.DARK_BLUE+"Map Selection");

        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        AtomicInteger count = new AtomicInteger();

        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean ignore = (boolean) info1.get("ignore");
            if (!ignore) {
                String name1 = (String) name;
                String material = (String) info1.get("icon");
                ItemStack item;

                if (material.equals("purple_wool")) {
                    item = new Wool(DyeColor.PURPLE).toItemStack();
                } else {
                    item = new ItemStack(Material.matchMaterial(material.toUpperCase()));
                }
                item.setAmount(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + name1 + ": " +main.mapVotes.get(count.get()+1)+" Votes");
                    item.setItemMeta(meta);
                }
                inventory.setItem(count.get(), item);
                count.getAndIncrement();
            }
        });
        return inventory;
    }

    
    @SuppressWarnings("unchecked")
    public static ItemStack statItem(Player player) {
        ItemStack item;
        ItemMeta meta;

        DataFiles datafiles = new DataFiles(player);
        PlayerData playerdata = datafiles.data;
        JSONObject stats = playerdata.Stats;

        // item
        item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta smeta = (SkullMeta) item.getItemMeta();
        smeta.setOwner(player.getName().toString());
        item.setItemMeta(smeta);
        meta = item.getItemMeta();
        if (meta == null) {return item;}
        meta.setDisplayName(ChatColor.GREEN + player.getName());
        List<String> lore = new ArrayList<>();

        // Coins
        lore.add(ChatColor.GOLD+"Your coins: "+ChatColor.BLUE+(long)playerdata.Coins);
        lore.add("");
        lore.add(ChatColor.GOLD+"Your stats:");

        // Custom wr and kdr
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

        // all other stats in order
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
        return item;
    }

    public static void FillerItems(Inventory inventory, int slot) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {meta.setDisplayName(" ");meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);item.setItemMeta(meta);}
        inventory.setItem(slot, item);
    }

    @SuppressWarnings("unchecked")
    public static Inventory ViewAndEditMaps(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 18, ChatColor.DARK_BLUE+"Map Selection");

        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        AtomicInteger count = new AtomicInteger();

        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean ignore = (boolean) info1.get("ignore");
            if (!ignore) {
                String name1 = (String) name;
                String material = (String) info1.get("icon");
                ItemStack item;

                if (material.equals("purple_wool")) {
                    item = new Wool(DyeColor.PURPLE).toItemStack();
                } else {
                    item = new ItemStack(Material.matchMaterial(material.toUpperCase()));
                }
                item.setAmount(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(name1);
                    List<String> lore = new ArrayList<>();
                    lore.add("Click to teleport to map");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                inventory.setItem(count.get(), item);
                count.getAndIncrement();
            }
        });

        for (int i=9; i<18; i++) {
            FillerItems(inventory, i);
        }

        ItemStack item;
        ItemMeta meta;
        item = new ItemStack(Material.COMPASS, 1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Information");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN+"Click a map to teleport to its actual world");
            lore.add(ChatColor.GRAY+"People who are dont have op will be put in spectator mode");
            lore.add(ChatColor.DARK_GREEN+"Use /lobby to return");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(13, item);

        // back to lobby
        item = new ItemStack(Material.BEDROCK, 1);
        item.setAmount(1);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Spawn");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY+"Click to go back to spawn");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(14, item);

        return inventory;
    }
    
}
