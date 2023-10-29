package skyclash.skyclash.fileIO;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class DataFiles {
    public PlayerData data;
    private String playerName;

    public DataFiles(Player player) {
        this.playerName = player.getName();
        this.data = LoadData();
    }

    public DataFiles(String name) {
        this.playerName = name;
        this.data = LoadData();
    }
    
    public void SetData(PlayerData data) {
        this.data = data;
        SaveData();
    }

    public void CreateFile() {
        String path = "players";
        File folder = new File(path);
        if(!folder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            folder.mkdirs();
        }

        File playerFile = new File(path+File.separator+playerName+".json");
        if(!playerFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                playerFile.createNewFile();
                SaveData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void SaveData() {
        String path = "players"+File.separator+playerName+".json";
        JSONObject jsondata = new JSONObject();
        jsondata.put("name", data.Name);
        jsondata.put("card", data.Card);
        jsondata.put("kit", data.Kit);
        jsondata.put("hasJoined", data.hasJoined);
        jsondata.put("stats", data.Stats);
        jsondata.put("coins", data.Coins);
        jsondata.put("autoready", data.Autoready);
        jsondata.put("owned", data.Owned);

        try {
            @SuppressWarnings("resource") FileWriter file = new FileWriter(path);
            file.write(jsondata.toJSONString());
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerData LoadData() {
        String path = "players"+File.separator+playerName+".json";
        JSONParser parser = new JSONParser();
        try {
            if (!(new File(path).exists())) {
                JSONObject arr = new JSONObject();
                arr.put("temp", 0);
                JSONArray tempArray = new JSONArray();
                tempArray.add("temp");
                data = new PlayerData(playerName, "Damage Potion", "Swordsman", false, arr, 0, false, tempArray);
                CreateFile();
                return data;
            }
            Object p = parser.parse(new FileReader(path));
            JSONObject jsonObject =(JSONObject) p;
            String name = (String) jsonObject.get("name");
            String card = (String) jsonObject.get("card");
            String kit = (String) jsonObject.get("kit");
            boolean hasJoined = (boolean) jsonObject.get("hasJoined");
            JSONObject stats = (JSONObject) jsonObject.get("stats");
            Long coins = (Long) jsonObject.get("coins");
            int coins2 = Math.toIntExact(coins);
            Boolean autoready = false;
            if (jsonObject.containsKey("autoready")) {autoready = (Boolean) jsonObject.get("autoready");}
            JSONArray owned = new JSONArray();
            if (jsonObject.containsKey("owned")) {owned = (JSONArray) jsonObject.get("owned");}
            return new PlayerData(name, card, kit, hasJoined, stats, coins2, autoready, owned);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
