package skyclash.skyclash.gameManager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;


public class StatsManager {
    public static HashMap<String, Integer> killtracker = new HashMap<>();

    public void addPlayer(Player player) {
        killtracker.put(player.getName(), 0);
    }
    public void addKill(Player player) {
        killtracker.put(player.getName(), killtracker.get(player.getName()) + 1);
    }

    // all other stats
    @SuppressWarnings("unchecked")
    public void changeStat(Player player, String stat, int change) {
        DataFiles files = new DataFiles(player);
        PlayerData pdata = files.data;
        pdata.Stats.remove("temp");

        // remove obselete stats
        HashMap<String, String> obseletes = new HashMap<>();
        obseletes.put("total_games", "Games");
        obseletes.put("DC", "Disconnect deaths");
        obseletes.put("VoidDeath", "Void deaths");
        obseletes.put("joins", "Joins");
        
        obseletes.forEach((Object1, Object2) -> {
            if (pdata.Stats.containsKey(Object1)) {
                int new_change = (int) ((long) pdata.Stats.get(Object1));
                pdata.Stats.put(Object2, new_change); 
                pdata.Stats.remove(Object1);
            }
        });

        if (stat.equals("coins")) {
            int new_coins = pdata.Coins + change;
            pdata.Coins = new_coins;  
        }
        else if (pdata.Stats.containsKey(stat)) {            
            int new_change = (int) ((long) pdata.Stats.get(stat) + change);  
            pdata.Stats.put(stat, new_change); 
        } 
        else {
            pdata.Stats.put(stat, change); 
        }

        files.SetData(pdata);
    }
}
