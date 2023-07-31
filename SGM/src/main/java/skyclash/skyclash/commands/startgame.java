package skyclash.skyclash.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import skyclash.skyclash.main;
import skyclash.skyclash.managers.Cards;
import skyclash.skyclash.managers.DataFiles;
import skyclash.skyclash.managers.Kits;
import skyclash.skyclash.managers.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class startgame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = main.getPlugin(main.class);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 30 seconds");
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                GameStarts();
            }
        }.runTaskLater(plugin, 600);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 10 seconds");
            }
        }.runTaskLater(plugin, 400);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 5 seconds");
            }
        }.runTaskLater(plugin, 500);
        return true;
    }

    public void GameStarts() {
        int max = Integer.MIN_VALUE;
        String max_map = null;
        HashMap<String, Integer> maps = main.mapselection;
        Set<Map.Entry<String,Integer>> entries=maps.entrySet();
        for(Map.Entry<String,Integer> entry:entries) {
            if(entry.getValue()>max) {
                max=entry.getValue();
                max_map=entry.getKey();
                // TODO max_map is map that got most votes
                // TODO set the world to max_map
            }
        }

        Bukkit.broadcastMessage("The map chosen is "+max_map);
        main.playertracker.forEach((key, value) -> {
            if (value.equals("ready")) {
                Player player = Bukkit.getServer().getPlayer(key);
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
                player.setSaturation(20);
                player.setMetadata("NoMovement", new FixedMetadataValue(main.getPlugin(main.class), "1"));
                player.sendMessage("You will be sent to play soon");
                DataFiles datafiles = new DataFiles(player);
                PlayerData data = datafiles.LoadData();
                data.State = "Ingame";
                main.playertracker.put(player.getName(), "ingame");
                datafiles.SetData(data);
                // TODO tp everyone to their spawns in the other world
            }
        });

        Plugin plugin = main.getPlugin(main.class);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.RED + "Starting in: 3");
            }
        }.runTaskLater(plugin, 20);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Starting in: 2");
            }
        }.runTaskLater(plugin, 40);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Starting in: 1");
            }
        }.runTaskLater(plugin, 60);
        main.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "SKYCLASH!");
                StartGame();
            }
        }.runTaskLater(plugin, 80);


    }

    public void StartGame() {
        // during game code
        main.playertracker.forEach((key, value) -> {
            Bukkit.getLogger().info(key);
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
            }

        });
        main.timer = 12000;
    }
}
