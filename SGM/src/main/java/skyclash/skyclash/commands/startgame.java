package skyclash.skyclash.commands;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import skyclash.skyclash.main;
import skyclash.skyclash.managers.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class startgame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = main.getPlugin(main.class);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 30 seconds");

        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 10 seconds");
            }
        }.runTaskLater(plugin, 20*20);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 5 seconds");
            }
        }.runTaskLater(plugin, 20*25);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                GameStarts();
            }
        }.runTaskLater(plugin, 20*1);
        return true;
    }

    @SuppressWarnings("unchecked")
    public void GameStarts() {
        AtomicInteger max = new AtomicInteger(-1);
        AtomicInteger max_map = new AtomicInteger();
        AtomicReference<String> mapselection = new AtomicReference<>("");

        // find map with highest vote
        main.mapselection.forEach((mapid, votes) -> {
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

        // match map to world
        Mapsfile maps1 = new Mapsfile();
        maps1.read_file(false, false);
        AtomicInteger count = new AtomicInteger(1);
        maps1.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            String name1 = (String) name;
            boolean ignore = (boolean) info1.get("ignore");
            if (!ignore) {
                if (max_map.get() == count.get()) {
                    mapselection.set(name1);
                }
                count.getAndIncrement();
            }
        });
        Bukkit.broadcastMessage(ChatColor.GOLD + mapselection.get() + ChatColor.YELLOW + " will be prepared shortly...");

        // copy world files
        copyWorld(Bukkit.getWorld(mapselection.get()), "ingame_map");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import ingame_map normal");


        Plugin plugin = main.getPlugin(main.class);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                prepare_players();
                Bukkit.broadcastMessage(ChatColor.GOLD + "Starting in: 3");
            }
        }.runTaskLater(plugin, 20);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Starting in: 2");
            }
        }.runTaskLater(plugin, 2*20);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Starting in: 1");
            }
        }.runTaskLater(plugin, 3*20);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "SKYCLASH!");
                StartGame();
            }
        }.runTaskLater(plugin, 4*20);


    }

    public void StartGame() {
        // during game code
        main.playertracker.forEach((key, value) -> {
            main.PlayersLeft++;

            Player player = Bukkit.getServer().getPlayer(key);
            player.removeMetadata("NoMovement", main.getPlugin(main.class));

            DataFiles datafiles = new DataFiles(player);
            PlayerData data = datafiles.LoadData();

            if (value.equals("ingame")) {
                Kits kit1 = new Kits(data.Kit, player);
                kit1.GiveKit();
                Cards card1 = new Cards(data.Card, player);
                card1.GiveCard();
                player.setScoreboard(main.board);
                player.updateInventory();
            }

        });
        main.timer = 600;


    }

    private static void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = Files.newInputStream(source.toPath());
                    OutputStream out = Files.newOutputStream(target.toPath());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void copyWorld(World originalWorld, String newWorldName) {
        File copiedFile = new File(Bukkit.getWorldContainer(), newWorldName);
        copyFileStructure(originalWorld.getWorldFolder(), copiedFile);
        new WorldCreator(newWorldName).createWorld();
    }

    public void prepare_players() {
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        main.current_world = worldManager.getMVWorld("ingame_map");
        worldManager.loadWorld(main.current_world.getName());
        World w = Bukkit.getWorld("ingame_map");

        main.playertracker.forEach((key, value) -> {
            if (value.equals("ready")) {
                Player player = Bukkit.getServer().getPlayer(key);
                player.updateInventory();
                player.closeInventory();
                // TODO CHEST LOOT
                // TODO CHANGE SPAWN LOCATIONS
                Location spawnloc = new Location(w, 0, 64, 0);
                player.teleport(spawnloc);

                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.setHealth(20);
                player.setSaturation(20);
                player.setMetadata("NoMovement", new FixedMetadataValue(main.getPlugin(main.class), "1"));
                player.sendMessage("You will be sent to play soon");

                DataFiles datafiles = new DataFiles(player);
                PlayerData data = datafiles.LoadData();
                data.State = "Ingame";
                main.playertracker.put(player.getName(), "ingame");
                datafiles.SetData(data);
            }
        });

    }
}
