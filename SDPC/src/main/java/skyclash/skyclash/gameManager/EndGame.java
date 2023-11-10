package skyclash.skyclash.gameManager;

import org.bukkit.*;
import org.bukkit.entity.Player;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.lobby.PlayerControls;
import skyclash.skyclash.lobby.VoteMap;
import skyclash.skyclash.main;
import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.WorldManager.SCWorlds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class EndGame {
    public EndGame(boolean isCommand) {
        new VoteMap().resetMaps();
        String winner = FindWinner();
        if (isCommand) {Bukkit.broadcastMessage("The game has abruptly ended");} 

        PlayerStatus.StatusMap.forEach((key, value) -> {
            if (value == PlayerState.SPECTATOR || value == PlayerState.INGAME) {
                Player player = Bukkit.getServer().getPlayer(key);
                
                player.sendMessage(ChatColor.GREEN + "The winner is " + winner);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                VoteMap.playerVote.remove(player.getName());
                displayKills(player);
                
                new PlayerControls().toLobby(player);
                new StatsManager().changeStat(player, "Games", 1);
                new PlayerControls().resetPlayer(player);
                new PlayerStatus().SetLobbyOrReady(player);
                new RemoveTags(player);
            }
        });
        int people_ready = new PlayerStatus().CountPeopleWithStatus(PlayerState.READY);
        if (people_ready >= 2) {new StartGame().AllReady();}
        
        main.killtracker = new HashMap<>();
        new Multiverse().DeleteWorld(SCWorlds.INGAME_MAP);
        main.activeWorld = null;
        main.isGameActive = false;
        Scheduler.playersLeft = 0;
        Scheduler.timer = 0;
    }

    public static void CheckGameEnded() {
        new Scheduler().scheduleTask(() -> {
            if (Scheduler.playersLeft <= 1) {
                new EndGame(false);
            }
        }, 5);
    }

    public String FindWinner() {
        AtomicReference<String> winner = new AtomicReference<>("no one");
        PlayerStatus.StatusMap.forEach((key, value) -> {
            if (value == PlayerState.INGAME) {
                winner.set(key);
                Player winr = Bukkit.getPlayer(key);
                new StatsManager().changeStat(winr, "wins", 1);
                new StatsManager().changeStat(winr, "coins", 50);
                winr.sendMessage(ChatColor.YELLOW+"+50 coins for winning");
            }
        });
        return winner.get();
    }

    public HashMap<String, Integer> SortHashMapByValue(HashMap<String, Integer> hashmap) {
        HashMap<String, Integer> SortedHash = new HashMap<>();
        ArrayList<Integer> sortStrings = new ArrayList<>();
        hashmap.forEach((string, value) -> {sortStrings.add(value);});
        List<Integer> sortStrings2 = sortStrings.stream().distinct().collect(Collectors.toList());
        Collections.sort(sortStrings2);
        Collections.reverse(sortStrings2);
        sortStrings2.forEach((value) -> {
            hashmap.forEach((string, svalue) -> {
                if (value.equals(svalue)) {
                    SortedHash.put(string, value);
                }
            });
        });
        return SortedHash;
    }

    public void displayKills(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_RED+"Top kills:");
        SortHashMapByValue(main.killtracker).forEach((string, value) ->  player.sendMessage("  "+ChatColor.RED+string+": "+ChatColor.YELLOW+value));
        main.killtracker = new HashMap<>();
        player.sendMessage("");
    }
}
