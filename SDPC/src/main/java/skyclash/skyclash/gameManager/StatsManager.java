package skyclash.skyclash.gameManager;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.kitscards.PlayerData;

@SuppressWarnings("all")
public class StatsManager {
    public static void changeStat(Player player, String stat, int change) {
        DataFiles files = new DataFiles(player);
        PlayerData pdata = files.LoadData();

        if (stat.equals("coins")) {
            int new_coins = pdata.Coins + change;
            pdata.Coins = new_coins;  
        }
        else {              
            int new_change = (int) ((long) pdata.Stats.get(stat) + change);  
            pdata.Stats.put(stat, new_change); 
        } 
        

        files.SetData(pdata);
    }

    public int getStat(Player player, String stat) {
        DataFiles files = new DataFiles(player);
        PlayerData pdata = files.LoadData();
        if (stat.equals("coins")) {
            return pdata.Coins;
        }
        else if (stat.equals("time_h")) {
            return Math.toIntExact((long)pdata.Stats.get("time_h") * 60 * 60);
        }
        else {
            return Math.toIntExact((long)pdata.Stats.get(stat));
        }
    }
}
