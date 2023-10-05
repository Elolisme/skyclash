package skyclash.skyclash.lobby;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.SpawnEgg;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import skyclash.skyclash.Clock;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.gameManager.StartGame;
import skyclash.skyclash.kitscards.PlayerData;
import skyclash.skyclash.main;

import java.util.concurrent.atomic.AtomicInteger;

public class InMenu implements Listener {
    public InMenu(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        // Checks
        Player player = (Player) event.getWhoClicked();
        DataFiles dataFiles2 = new DataFiles(player);
        PlayerData data1 = dataFiles2.LoadData();
        if (!main.playerStatus.containsKey(player.getName())) {
            main.playerStatus.put(player.getName(), "lobby");
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        if ((event.getCurrentItem().getType() == Material.AIR)) {
            return;
        }
        if (event.getCurrentItem().getType() == Material.NETHER_STAR) {
            if (data1.hasJoined) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
        }
        if (!main.playerStatus.get(player.getName()).equals("lobby")) {
            if (!main.playerStatus.get(player.getName()).equals("ready")) {
                return;
            }
        }

        // Opening main menu
        if (player.hasMetadata("OpenedMenu")) {
            event.setCancelled(true);

            // open maps
            if (event.getSlot() == 1) {
                // menu map selection
                Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.DARK_BLUE+"Map Selection");

                Mapsfile maps = new Mapsfile();
                maps.read_file(false, false);
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
                player.openInventory(inventory);
                player.setMetadata("OpenedMenu2", new FixedMetadataValue(main.getPlugin(main.class), "Map Selection"));

            }

            // ready system
            else if (event.getSlot() == 3) {
                player.closeInventory();
                if (main.playerStatus.get(player.getName()).equals("ready") && !main.isGameActive) {
                    player.sendMessage(ChatColor.YELLOW+"You are no longer ready");
                    main.playerStatus.put(player.getName(), "lobby");
                    CheckStartGame(false);
                } else if (main.playerStatus.get(player.getName()).equals("lobby") && !main.isGameActive){
                    player.sendMessage(ChatColor.YELLOW+"You are now ready");
                    main.playerStatus.put(player.getName(), "ready");
                    CheckStartGame(true);
                } else {
                    player.sendMessage(ChatColor.RED+"Spectating current game, use /lobby to return to lobby");
                    Location spawnloc = new Location(Bukkit.getWorld("ingame_map"), 0, 70, 0);
                    main.playerStatus.put(player.getName(), "spectator");
                    player.setScoreboard(Clock.board);
                    player.teleport(spawnloc);
                    player.getInventory().clear();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.setGameMode(GameMode.SPECTATOR);
                        }
                    }.runTaskLater(main.getPlugin(main.class), 5);
                }

            }

            // open kits
            else if (event.getSlot() == 5) {
                Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.DARK_BLUE+"Kit Selection");

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
                    meta.setDisplayName(ChatColor.RED + "Beserker");
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
                


                player.openInventory(inventory);
                player.setMetadata("OpenedMenu3", new FixedMetadataValue(main.getPlugin(main.class), "Kit Selection"));

            }

            // open cards
            else if (event.getSlot() == 7) {
                Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.DARK_BLUE+"Card Selection");

                ItemStack item;
                ItemMeta meta;

                item = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
                item.setAmount(1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Creeper");
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

                // card
                item = new Potion(PotionType.INSTANT_DAMAGE).toItemStack(1);
                item.setAmount(1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Damage Potion");
                    item.setItemMeta(meta);
                }
                inventory.setItem(2, item);

                // card
                item = new ItemStack(Material.IRON_CHESTPLATE, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Blast Protection");
                    item.setItemMeta(meta);
                }
                inventory.setItem(3, item);

                // card
                item = new ItemStack(Material.BOW, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Elven Archer");
                    item.setItemMeta(meta);
                }
                inventory.setItem(4, item);

                // card
                item = new ItemStack(Material.ENCHANTED_BOOK, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Quiver Refill");
                    item.setItemMeta(meta);
                }
                inventory.setItem(5, item);

                // card
                item = new ItemStack(Material.CHEST, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Apple Finder");
                    item.setItemMeta(meta);
                }
                inventory.setItem(6, item);

                // card
                item = new ItemStack(Material.IRON_BOOTS, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Hit and Run");
                    item.setItemMeta(meta);
                }
                inventory.setItem(7, item);

                // card
                item = new SpawnEgg(EntityType.ZOMBIE).toItemStack(1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Pacify");
                    item.setItemMeta(meta);
                }
                inventory.setItem(8, item);

                // card
                item = new ItemStack(Material.ENDER_PEARL, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Pearl Absorption");
                    item.setItemMeta(meta);
                }
                inventory.setItem(9, item);

                // card
                item = new ItemStack(Material.SUGAR, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Sugar Rush");
                    item.setItemMeta(meta);
                }
                inventory.setItem(10, item);

                // card
                item = new ItemStack(Material.REDSTONE, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Lifesteal");
                    item.setItemMeta(meta);
                }
                inventory.setItem(11, item);

                // card
                item = new ItemStack(Material.ROTTEN_FLESH, 1);
                meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Monster Hunter");
                    item.setItemMeta(meta);
                }
                inventory.setItem(12, item);

                player.openInventory(inventory);
                player.setMetadata("OpenedMenu4", new FixedMetadataValue(main.getPlugin(main.class), "Card Selection"));
            }
        }

        // Map menu
        else if (player.hasMetadata("OpenedMenu2")) {
            player.closeInventory();
            event.setCancelled(true);
            if (0 <= event.getRawSlot() && event.getRawSlot() <= (main.mapVotes.size()-1)) {
                int slot = event.getSlot() + 1;
                if (main.playerVote.containsKey(player.getName())) {
                    int oldmap = main.playerVote.get(player.getName());
                    if (oldmap == slot) {
                        player.sendMessage(ChatColor.RED + "You have already voted for this map!");
                        return;
                    }
                    Integer num = main.mapVotes.get(oldmap);
                    num--;
                    main.mapVotes.put(oldmap, num);
                }
                Integer num = main.mapVotes.get(slot);
                num++;
                main.mapVotes.put(slot, num);
                main.playerVote.put(player.getName(), slot);
                String[] display = event.getCurrentItem().getItemMeta().getDisplayName().split(":");
                player.sendMessage(ChatColor.GREEN + "You have voted for "+ display[0]);
            }
        }

        // Kits menu
        else if (player.hasMetadata("OpenedMenu3")) {
            player.closeInventory();
            event.setCancelled(true);
            if (0 <= event.getRawSlot() && event.getRawSlot() <= 26) {
                DataFiles dataFiles = new DataFiles(player);
                PlayerData data2 = dataFiles.LoadData();
                data2.Kit = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                player.sendMessage(ChatColor.YELLOW + "You have chosen " + data2.Kit + " kit");
                dataFiles.SetData(data2);
            }
        }

        // Cards Menu
        else if (player.hasMetadata("OpenedMenu4")) {
            player.closeInventory();
            event.setCancelled(true);
            if (0 <= event.getRawSlot() && event.getRawSlot() <= 26) {
                DataFiles dataFiles = new DataFiles(player);
                PlayerData data3 = dataFiles.LoadData();
                data3.Card = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                player.sendMessage(ChatColor.YELLOW + "You have chosen " + data3.Card + " card");
                dataFiles.SetData(data3);
            }
        }
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

    public static void CheckStartGame(boolean started) {
        AtomicInteger people_ready = new AtomicInteger(0);
        main.playerStatus.forEach((key, value) -> {
            if (value.equals("ready")) {
                people_ready.getAndIncrement();
            }
        });
        if (started && Integer.parseInt(String.valueOf(people_ready)) == 2) {
            new StartGame(false);
        }
        if (!started && Integer.parseInt(String.valueOf(people_ready)) == 1) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game cancelled due to insufficient people ready");
            if (StartGame.task1 != null) {
                StartGame.EndTasks();
            }
        }
    }
}
