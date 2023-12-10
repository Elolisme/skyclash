package skyclash.skyclash.gameManager;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;
import skyclash.skyclash.kitscards.Cards;
import skyclash.skyclash.kitscards.Kits;
import skyclash.skyclash.lobby.PlayerControls;
import skyclash.skyclash.lobby.VoteMap;
import skyclash.skyclash.main;
import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.WorldManager.SCWorlds;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StartGame {
    private static BukkitTask task1 = null;
    private static BukkitTask task2 = null;
    private static BukkitTask task3 = null;
    private static BukkitTask Chesttask1 = null;
    private static BukkitTask Chesttask2 = null;
    private static BukkitTask Chesttask3 = null;

    private PlayerStatus pStatus = new PlayerStatus();
    private Scheduler scheduler = new Scheduler();
    private World votedMap;

    public void AllReady() {
        CancelEvents();
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 20 seconds");
        task1 = scheduler.scheduleTask(()->Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 10 seconds"), 10*20);
        task2 = scheduler.scheduleTask(()->Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 5 seconds"), 15*20);
        task3 = scheduler.scheduleTask(()->StartPrep(), 20*20);
    }

    public void AllReadyCommand() {
        CancelEvents();
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in 5 seconds");
        task3 = scheduler.scheduleTask(()->StartPrep(), 5*20);
    }

    private void StartPrep() {
        votedMap = new VoteMap().getHighestVotedWorld();
        SCWorlds scworlds = new SCWorlds();
        Multiverse mv = new Multiverse();

        Bukkit.broadcastMessage(ChatColor.GOLD + votedMap.getName() + ChatColor.YELLOW + " will be prepared shortly...");
        mv.SaveAll();
        scworlds.CopyWorld(votedMap.getName(), SCWorlds.INGAME_MAP);
        mv.PrepareWorld(SCWorlds.INGAME_MAP);

        AtomicInteger counter = new AtomicInteger(1);
        PlayerStatus.StatusMap.forEach((key, value) -> {
            if (value != PlayerState.READY) {
                return;
            }
            Player player = Bukkit.getServer().getPlayer(key);
            Scheduler.playersLeft++;
            ArrayList<ArrayList<Integer>> spawnsarray = scworlds.getSpawnArray(votedMap.getName());
            int arrayLength = spawnsarray.size();

            if (Scheduler.playersLeft > arrayLength) {
                player.sendMessage(ChatColor.RED+"Since this map only supports up to "+arrayLength+" players, you were not able to play");
                pStatus.SetLobbyOrReady(player);
                Scheduler.playersLeft--;
                return;
            } 

            new PlayerControls().resetPlayer(player);
            new StatsManager().addPlayer(player);
            scworlds.teleportPlayer(player, spawnsarray.get(Scheduler.playersLeft - 1));
            EndGame.RemoveTags(player);
            player.setMetadata("NoMovement", new FixedMetadataValue(main.plugin, "1"));
            player.sendMessage("You will be sent to play soon");
            pStatus.SetStatus(player, PlayerState.INGAME);
            if (!player.hasMetadata("nopearlcooldown")) {
                Cooldown.add(player.getName(), "Pearl", 20, System.currentTimeMillis());
            }
            counter.getAndIncrement();
        });
        scheduler.scheduleTask(()->Bukkit.broadcastMessage(ChatColor.RED + "Starting in: 3"), 1*20);
        scheduler.scheduleTask(()->Bukkit.broadcastMessage(ChatColor.YELLOW + "Starting in: 2"), 2*20);
        scheduler.scheduleTask(()->Bukkit.broadcastMessage(ChatColor.GREEN + "Starting in: 1"), 3*20);
        scheduler.scheduleTask(()->CountdownZero(), 4*20);
    }

    private void CountdownZero() {
        // chest loot
        new SCWorlds().generateChestLoot(votedMap.getName(), false);
        Chesttask1 = scheduler.scheduleTask(()->new SCWorlds().generateChestLoot(votedMap.getName(), true), 20*60*3);
        Chesttask2 = scheduler.scheduleTask(()->new SCWorlds().generateChestLoot(votedMap.getName(), true), 20*60*6);
        Chesttask3 = scheduler.scheduleTask(()->new SCWorlds().generateChestLoot(votedMap.getName(), true), 20*60*9);

        Bukkit.broadcastMessage(ChatColor.GREEN + "SKYCLASH!");
        Scheduler.timer = 600;
        PlayerStatus.StatusMap.forEach((key, value) -> {
            Player player = Bukkit.getServer().getPlayer(key);
            DataFiles datafiles = new DataFiles(player);
            if (!pStatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
                return;
            }
            new Kits(datafiles.data.kit, player).GiveKit();
            new Cards(datafiles.data.card, player).GiveCard();
            new ShopItems(player);

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 254, true));
            player.setScoreboard(Scheduler.board);
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 0.9f);
            if (player.hasMetadata("NoMovement")) {
                player.removeMetadata("NoMovement", main.plugin);
            }
        });
    }

    public void CancelEvents() {
        if (task1 != null) {task1.cancel();}
        if (task2 != null) {task2.cancel();}
        if (task3 != null) {task3.cancel();}
        task1 = null;
        task2 = null;
        task3 = null;
    }

    public void CancelChestEvents() {
        if (Chesttask1 != null) {Chesttask1.cancel();}
        if (Chesttask2 != null) {Chesttask2.cancel();}
        if (Chesttask3 != null) {Chesttask3.cancel();}
        Chesttask1 = null;
        Chesttask2 = null;
        Chesttask3 = null;
    }
}