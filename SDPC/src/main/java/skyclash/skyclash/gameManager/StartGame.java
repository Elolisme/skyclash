package skyclash.skyclash.gameManager;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.chestgen.ChestManager;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.fileIO.CopyWorld;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.kitscards.Cards;
import skyclash.skyclash.kitscards.Kits;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.main;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StartGame {
    
    private AtomicReference<String> VotedMap = new AtomicReference<>("");
    private Plugin plugin = main.getPlugin(main.class);
    private Mapsfile maps = new Mapsfile();
    private static BukkitTask task1;
    private static BukkitTask task2;
    private static BukkitTask task3;

    public StartGame(boolean isCommand) {
        int delay;
        if (isCommand) {
            delay = 5;
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 5 seconds");
        } else {
            delay = 20;
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 20 seconds");
        }


        task2 = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 10 seconds");
            }
        }.runTaskLater(plugin, 20*10);
        task3 = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 5 seconds");
            }
        }.runTaskLater(plugin, 20*15);
        task1 = new BukkitRunnable() {
            @Override
            public void run() {
                PreGame();
                Start();
            }
        }.runTaskLater(plugin, 20*delay);

        if (isCommand) {
            task2.cancel();
            task3.cancel();
        }
    }

    @SuppressWarnings({"UnnecessaryBoxing", "unchecked"})
    private void PreGame() {

        // find map with the highest vote
        AtomicInteger max = new AtomicInteger(-1);
        AtomicInteger max_map = new AtomicInteger();
        main.mapVotes.forEach((mapid, votes) -> {
            if(votes > max.get()) {
                max.set(votes);
                max_map.set(mapid);
            } else if (votes == max.get()) {
                Random rd = new Random();
                if (rd.nextBoolean()) {
                    max.set(votes);
                    max_map.set(mapid);
                }
            }
        });

        // get world with the highest votes
        maps.readFile(false, false);
        AtomicInteger count = new AtomicInteger(1);
        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            String name1 = (String) name;
            boolean ignore = (boolean) info1.get("ignore");
            if (!ignore) {
                if (max_map.get() == count.get()) {
                    VotedMap.set(name1);
                }
                count.getAndIncrement();
            }
        });
        Bukkit.broadcastMessage(ChatColor.GOLD + VotedMap.get() + ChatColor.YELLOW + " will be prepared shortly...");

        // copy world files
        List<World> worlds = Bukkit.getWorlds();
        for (World world: worlds) {
            world.save();
        }


        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        worldManager.loadWorld(VotedMap.get());
        new CopyWorld().copyWorld(Bukkit.getWorld(VotedMap.get()), "ingame_map");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import ingame_map normal");
        main.isGameActive = true;
        main.activeWorld = VotedMap.get();

        // set world settings
        worldManager.getMVWorld("ingame_map").setAllowMonsterSpawn(false);
        worldManager.getMVWorld("ingame_map").setBedRespawn(false);
        Bukkit.getWorld("ingame_map").setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");
        Bukkit.getWorld("ingame_map").setTime(6000);
        Bukkit.getWorld("ingame_map").getWorldBorder().setCenter(0, 0);
        Bukkit.getWorld("ingame_map").getWorldBorder().setSize(300);

        // prepare all ready players
        AtomicInteger counter = new AtomicInteger(1);
        main.playerStatus.forEach((key, value) -> {
            if (value.equals("ready")) {
                // get spawn information from world
                Scheduler.playersLeft++;
                Player player = Bukkit.getServer().getPlayer(key);
                AtomicReference<JSONObject> worldinfo = new AtomicReference<>();
                maps.jsonObject.forEach((name, info) -> {
                    String name1 = (String) name;
                    JSONObject info1 = (JSONObject) info;
                    if (name1.equals(VotedMap.get())) {
                        worldinfo.set(info1);
                    }
                });

                // spawn player to their respective spawns
                JSONObject info2 = worldinfo.get();
                JSONArray spawnsarray = StringToJSON.convert((String) info2.get("spawns"));

                int length = spawnsarray.size();
                if (Scheduler.playersLeft > length) {
                    player.sendMessage(ChatColor.RED+"Since this map only supports up to "+length+" players, you were not able to play");
                    main.playerStatus.put(player.getName(), "lobby");
                    Scheduler.playersLeft--;
                }
                else {
                    JSONArray lists = (JSONArray) spawnsarray.get((Scheduler.playersLeft -1));
                    int x = Integer.valueOf(String.valueOf(lists.get(0)));
                    int y = Integer.valueOf(String.valueOf(lists.get(1)));
                    int z = Integer.valueOf(String.valueOf(lists.get(2)));
                    Location spawnloc = new Location(Bukkit.getWorld("ingame_map"), x, y, z);
                    player.teleport(spawnloc);
                    new StatsManager().addPlayer(player);

                    // player setup for game
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    player.getInventory().setBoots(new ItemStack(Material.AIR));
                    player.setHealth(20);
                    player.setSaturation(20);
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }
                    player.setLevel(0);
                    player.setExp(0);
                    player.setMetadata("NoMovement", new FixedMetadataValue(main.getPlugin(main.class), "1"));
                    player.sendMessage("You will be sent to play soon");
                    new RemoveTags(player);
                    main.playerStatus.put(player.getName(), "ingame");
                    Cooldown.add(player.getName(), "Pearl", 20, System.currentTimeMillis());
                    counter.getAndIncrement();
                }
            }
        });
    }

    private void Start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.RED + "Starting in: 3");
            }
        }.runTaskLater(plugin, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Starting in: 2");
            }
        }.runTaskLater(plugin, 2*20);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Starting in: 1");
            }
        }.runTaskLater(plugin, 3*20);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "SKYCLASH!");

                // chest loot generation
                maps.readFile(false, false);
                JSONObject info2 = (JSONObject) maps.jsonObject.get(VotedMap.get());
                JSONArray chestsarray = StringToJSON.convert((String) info2.get("chests"));
                World world1 = Bukkit.getWorld("ingame_map");
                new ChestManager(chestsarray, world1, "spawn", "mid", true);

                // start game code
                main.playerStatus.forEach((key, value) -> {
                    if (main.playerStatus.get(key).equals("ingame")) {
                        Player player = Bukkit.getServer().getPlayer(key);
                        if (player.hasMetadata("NoMovement")) {
                            player.removeMetadata("NoMovement", main.getPlugin(main.class));
                        }
                        DataFiles datafiles = new DataFiles(player);
                        PlayerData data = datafiles.data;
                        new Kits(data.Kit, player).GiveKit();
                        new Cards(data.Card, player).GiveCard();
                        new giveItems(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 254, true));
                        player.setScoreboard(Scheduler.board);
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 0.9f);
                    }
                });
                Scheduler.timer = 600;
            }
        }.runTaskLater(plugin, 4*20);
    }

    public static void EndTasks() {
        task1.cancel();
        task2.cancel();
        task3.cancel();
        task1 = null;
        task2 = null;
        task3 = null;
    }
}
