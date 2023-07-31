package skyclash.skyclash.handlers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import skyclash.skyclash.main;
import skyclash.skyclash.managers.DataFiles;
import skyclash.skyclash.managers.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class lobby implements Listener {

    public lobby(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // event handler
    @EventHandler
    public void onUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!main.playertracker.containsKey(player.getName())) {
            main.playertracker.put(player.getName(), "lobby");
        }
        if (player.getItemInHand().getType() != Material.NETHER_STAR) {
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

        if (main.playertracker.get(player.getName()).equals("ready")) {
            inventory.setItem(3, item3);
        }
        else {
            inventory.setItem(3, item2);
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

    @EventHandler
    public void onCloseMenu(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (player.hasMetadata("OpenedMenu")) {
            player.removeMetadata("OpenedMenu", main.getPlugin(main.class));
        }
        if (player.hasMetadata("OpenedMenu2")) {
            player.removeMetadata("OpenedMenu2", main.getPlugin(main.class));
        }
        if (player.hasMetadata("OpenedMenu3")) {
            player.removeMetadata("OpenedMenu3", main.getPlugin(main.class));
        }
        if (player.hasMetadata("OpenedMenu4")) {
            player.removeMetadata("OpenedMenu4", main.getPlugin(main.class));
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        DataFiles dataFiles = new DataFiles(player);
        PlayerData data4 = dataFiles.LoadData();

        if (event.getItemDrop() == null) {
            return;
        }
        if (event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR && data4.State.equals("Lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        DataFiles dataFiles2 = new DataFiles(player);
        PlayerData data1 = dataFiles2.LoadData();

        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType() == Material.NETHER_STAR && data1.State.equals("Lobby") && player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
        }

        }
        // on opening main menu
        if (player.hasMetadata("OpenedMenu")) {
            event.setCancelled(true);

            if (event.getSlot() == 1) {
                player.closeInventory();

                // menu map selection
                Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.DARK_BLUE+"Map Selection");

                // item 1
                ItemStack item = new ItemStack(Material.GRASS);
                item.setAmount(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Map1: "+main.mapselection.get("Map1")+" Votes");
                    item.setItemMeta(meta);
                }
                inventory.setItem(0, item);

                // item 2
                ItemStack item2 = new Wool(DyeColor.PURPLE).toItemStack();
                item2.setAmount(1);
                ItemMeta meta2 = item.getItemMeta();
                if (meta2 != null) {
                    meta2.setDisplayName(ChatColor.RED + "Map2: "+main.mapselection.get("Map2")+" Votes");
                    item2.setItemMeta(meta2);
                }
                inventory.setItem(1, item2);

                // item 3
                ItemStack item3 = new ItemStack(Material.STONE);
                item3.setAmount(1);
                ItemMeta meta3 = item.getItemMeta();
                if (meta3 != null) {
                    meta3.setDisplayName(ChatColor.RED + "Map3: "+main.mapselection.get("Map3")+" Votes");
                    item3.setItemMeta(meta3);
                }
                inventory.setItem(2, item3);


                player.openInventory(inventory);
                player.setMetadata("OpenedMenu2", new FixedMetadataValue(main.getPlugin(main.class), "Map Selection"));

            } else if (event.getSlot() == 3) {
                
                if (main.playertracker.get(player.getName()).equals("ready")) {
                    player.sendMessage(ChatColor.YELLOW+"You are no longer ready");
                    main.playertracker.put(player.getName(), "lobby");
                    CheckStopGame();
                } else {
                    player.sendMessage(ChatColor.YELLOW+"You are now ready");
                    main.playertracker.put(player.getName(), "ready");
                    CheckStartGame();
                }
                player.closeInventory();

            } else if (event.getSlot() == 5) {
                player.closeInventory();

                // kit selection
                Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.DARK_BLUE+"Kit Selection");

                // item 1
                // TODO add kits
                ItemStack item = new Potion(PotionType.INVISIBILITY).toItemStack(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.GRAY + "Assassin");
                    item.setItemMeta(meta);
                }
                inventory.setItem(0, item);

                // item 2
                ItemStack item2 = new Potion(PotionType.STRENGTH).toItemStack(1);
                ItemMeta meta2 = item.getItemMeta();
                if (meta2 != null) {
                    meta2.setDisplayName(ChatColor.RED + "Beserker");
                    item2.setItemMeta(meta2);
                }
                inventory.setItem(1, item2);

                // item 3
                ItemStack item3 = new ItemStack(Material.IRON_SWORD);
                item3.setAmount(1);
                ItemMeta meta3 = item3.getItemMeta();
                if (meta3 != null) {
                    meta3.setDisplayName(ChatColor.RED + "Swordsman");
                    item3.setItemMeta(meta3);
                }
                inventory.setItem(2, item3);


                player.openInventory(inventory);
                player.setMetadata("OpenedMenu3", new FixedMetadataValue(main.getPlugin(main.class), "Kit Selection"));

            } else if (event.getSlot() == 7) {
                player.closeInventory();

                // card selection
                // TODO add cards
                Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.DARK_BLUE+"Card Selection");

                // item 1
                ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
                item.setAmount(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.GREEN + "Creeper");
                    item.setItemMeta(meta);
                }
                inventory.setItem(0, item);

                // item 2
                ItemStack item2 = new ItemStack(Material.TNT);
                item2.setAmount(1);
                ItemMeta meta2 = item.getItemMeta();
                if (meta2 != null) {
                    meta2.setDisplayName(ChatColor.RED + "Bigger Bangs");
                    item2.setItemMeta(meta2);
                }
                inventory.setItem(1, item2);

                // item 3
                ItemStack item3 = new Potion(PotionType.INSTANT_DAMAGE).toItemStack(1);
                item3.setAmount(1);
                ItemMeta meta3 = item.getItemMeta();
                if (meta3 != null) {
                    meta3.setDisplayName(ChatColor.RED + "Damage Potion");
                    item3.setItemMeta(meta3);
                }
                inventory.setItem(2, item3);


                player.openInventory(inventory);
                player.setMetadata("OpenedMenu4", new FixedMetadataValue(main.getPlugin(main.class), "Card Selection"));

            }
        }

        // clicks in menu 2
        else if (player.hasMetadata("OpenedMenu2")) {
            if (0 <= event.getSlot() && event.getSlot() <= 8) {
                int slot = event.getSlot() + 1;
                if (main.playermap.containsKey(player.getName())) {
                    int oldmap = main.playermap.get(player.getName());
                    if (oldmap == slot) {
                        player.sendMessage(ChatColor.RED + "You have already voted for this map!");
                        player.closeInventory();
                        return;
                    }
                    main.decrementvalue("Map" + oldmap);
                }
                main.incrementvalue("Map" + slot);
                main.playermap.put(player.getName(), slot);
                player.sendMessage(ChatColor.GREEN + "You have voted for Map " + slot);
                player.closeInventory();
            }
        }

        // clicks in menu 3
        else if (player.hasMetadata("OpenedMenu3")) {
            DataFiles dataFiles = new DataFiles(player);
            PlayerData data2 = dataFiles.LoadData();
            if (event.getSlot() == 0) {
                data2.Kit = "Assassin";
            } else if (event.getSlot() == 1) {
                data2.Kit = "Beserker";
            } else if (event.getSlot() == 2) {
                data2.Kit = "Swordsman";
            }
            // TODO add kits here as well
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW+"You have chosen "+data2.Kit+" kit");
            dataFiles.SetData(data2);
        }

        // clicks in menu 4
        else if (player.hasMetadata("OpenedMenu4")) {
            DataFiles dataFiles = new DataFiles(player);
            PlayerData data3 = dataFiles.LoadData();
            if (event.getSlot() == 0) {
                data3.Card = "Creeper";
            } else if (event.getSlot() == 1) {
                data3.Card = "Bigger Bangs";
            } else if (event.getSlot() == 2) {
                data3.Card = "Damage Potion";
            }
            // TODO add cards here as well
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "You have chosen " + data3.Card + " card");
            dataFiles.SetData(data3);
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // FIXME remove soon
        player.sendMessage(ChatColor.RED+"PLEASE DO NOT BUILD IN AGENTLAB WORLD!\nCHECK DISCORD FOR INFO ON multiverse");

        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.LoadData();

        // if they in lobby
        if (data.State.equals("Lobby")  && player.getGameMode() != GameMode.CREATIVE) {
            if (!player.getInventory().contains(Material.NETHER_STAR)) {
                GiveItem(player);
                player.setScoreboard(main.emptyboard);
            }
        }
        // if they have joined
        else if (data.State.equals("null") && player.getGameMode() != GameMode.CREATIVE) {
            GiveItem(player);
            player.sendMessage(ChatColor.GREEN+"Welcome to skyclash!\nClick the nether star to access the menu\n\nGLHF (plugin by TitanPlayz)");
            data.State = "Lobby";
            data.Name = player.getName();
        }
        datafiles.SetData(data);
    }

    @EventHandler
    public void onGamemodeSwitch(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.LoadData();
        if (data.State.equals("Lobby")  && event.getNewGameMode() != GameMode.CREATIVE) {
            if (!player.getInventory().contains(Material.NETHER_STAR)) {
                GiveItem(player);
            }
        }
    }

    public void GiveItem(Player player) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Skyclash Menu");
            List<String> lore = new ArrayList<>();
            lore.add("Click to access the menu");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        player.getInventory().setItem(8, item);
    }

    public void CheckStartGame() {
        AtomicInteger people_ready = new AtomicInteger();
        main.playertracker.forEach((key, value) -> {
            if (value.equals("ready")) {
                people_ready.getAndIncrement();
            }
        });
        if (Integer.parseInt(String.valueOf(people_ready)) == 2) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "startgame");
        }
    }

    public void CheckStopGame() {
        AtomicInteger people_ready = new AtomicInteger();
        main.playertracker.forEach((key, value) -> {
            if (value.equals("ready")) {
                people_ready.getAndDecrement();
            }
        });
        if (Integer.parseInt(String.valueOf(people_ready)) == 1) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game cancelled due to insufficient people ready");
            main.task.cancel();
        }
    }
}