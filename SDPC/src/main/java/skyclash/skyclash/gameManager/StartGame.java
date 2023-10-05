package skyclash.skyclash.gameManager;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.Clock;
import skyclash.skyclash.chestgen.ChestManager;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.kitscards.Cards;
import skyclash.skyclash.kitscards.Kits;
import skyclash.skyclash.kitscards.PlayerData;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.main;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static skyclash.skyclash.fileIO.CopyWorld.copyWorld;

public class StartGame {
    AtomicReference<String> VotedMap = new AtomicReference<>("");
    Plugin plugin = main.getPlugin(main.class);
    Mapsfile maps = new Mapsfile();
    public static BukkitTask task1;
    public static BukkitTask task2;
    public static BukkitTask task3;
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
    public void PreGame() {

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
        maps.read_file(false, false);
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
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        worldManager.loadWorld(VotedMap.get());
        copyWorld(Bukkit.getWorld(VotedMap.get()), "ingame_map");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import ingame_map normal");
        worldManager.unloadWorld(VotedMap.get());
        main.isGameActive = true;
        main.activeWorld = VotedMap.get();

        // set world settings
        worldManager.getMVWorld("ingame_map").setAllowMonsterSpawn(false);
        Bukkit.getWorld("ingame_map").setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");
        Bukkit.getWorld("ingame_map").setTime(6000);

        // prepare all ready players
        AtomicInteger counter = new AtomicInteger(1);
        main.playerStatus.forEach((key, value) -> {
            if (value.equals("ready")) {
                // get spawn information from world
                Clock.playersLeft++;
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
                if (Clock.playersLeft > length) {
                    player.sendMessage(ChatColor.RED+"Since this map only supports up to "+length+" players, you were not able to play");
                    main.playerStatus.put(player.getName(), "lobby");
                    Clock.playersLeft--;
                }
                else {
                    JSONArray lists = (JSONArray) spawnsarray.get((Clock.playersLeft -1));
                    int x = Integer.valueOf(String.valueOf(lists.get(0)));
                    int y = Integer.valueOf(String.valueOf(lists.get(1)));
                    int z = Integer.valueOf(String.valueOf(lists.get(2)));
                    Location spawnloc = new Location(Bukkit.getWorld("ingame_map"), x, y, z);
                    player.teleport(spawnloc);
                    StatsManager.addPlayer(player);

                    // player setup for game
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    player.setHealth(20);
                    player.setSaturation(20);
                    player.setLevel(0);
                    player.setExp(0);
                    player.setMetadata("NoMovement", new FixedMetadataValue(main.getPlugin(main.class), "1"));
                    player.sendMessage("You will be sent to play soon");
                    new RemoveTags(player);
                    main.playerStatus.put(player.getName(), "ingame");
                    counter.getAndIncrement();
                }
            }
        });
    }

    public void Start() {
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
                maps.read_file(false, false);
                JSONObject info2 = (JSONObject) maps.jsonObject.get(VotedMap.get());
                JSONArray chestsarray = StringToJSON.convert((String) info2.get("chests"));
                World world1 = Bukkit.getWorld("ingame_map");
                ChestManager cm = new ChestManager(chestsarray, world1, "spawn", "mid");
                cm.loadChestLoot();

                // start game code
                main.playerStatus.forEach((key, value) -> {
                    if (main.playerStatus.get(key).equals("ingame")) {
                        Player player = Bukkit.getServer().getPlayer(key);
                        if (player.hasMetadata("NoMovement")) {
                            player.removeMetadata("NoMovement", main.getPlugin(main.class));
                        }
                        DataFiles datafiles = new DataFiles(player);
                        PlayerData data = datafiles.LoadData();
                        Kits kit1 = new Kits(data.Kit, player);
                        kit1.GiveKit();
                        Cards card1 = new Cards(data.Card, player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 254, true));
                        card1.GiveCard();
                        player.setScoreboard(Clock.board);
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 0.9f);
                    }
                });
                Clock.timer = 600;
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
