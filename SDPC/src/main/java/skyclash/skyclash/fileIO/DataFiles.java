package skyclash.skyclash.fileIO;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import skyclash.skyclash.kitscards.PlayerData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class DataFiles {
    private final Player player;
    private PlayerData data;

    public DataFiles(Player player) {
        this.player = player;
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

        File playerFile = new File(path+File.separator+player.getName()+".json");
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
        String path = "players"+File.separator+player.getName()+".json";
        JSONObject jsondata = new JSONObject();
        jsondata.put("name", data.Name);
        jsondata.put("card", data.Card);
        jsondata.put("kit", data.Kit);
        jsondata.put("hasJoined", data.hasJoined);
        jsondata.put("stats", data.Stats);
        jsondata.put("coins", data.Coins);

        try {
            @SuppressWarnings("resource") FileWriter file = new FileWriter(path);
            file.write(jsondata.toJSONString());
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerData LoadData() {
        String path = "players"+File.separator+player.getName()+".json";
        JSONParser parser = new JSONParser();
        try {
            if (!(new File(path).exists())) {
                JSONObject arr = new JSONObject();
                arr.put("temp", 0);
                data = new PlayerData(player.getName(), "Damage Potion", "Swordsman", false, arr, 0);
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
            return new PlayerData(name, card, kit, hasJoined, stats, coins2);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
