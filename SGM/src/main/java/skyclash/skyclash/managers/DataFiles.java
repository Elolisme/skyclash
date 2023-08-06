package skyclash.skyclash.managers;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataFiles {
    private final Player player;
    private PlayerData data;
    public DataFiles(Player player) {
        this.player = player;
        this.data = new PlayerData(player.getName(), "null", "null", "null");
        LoadData();
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
    @SuppressWarnings("unchecked")
    public void SaveData() {
        String path = "players"+File.separator+player.getName()+".json";
        JSONObject jsondata = new JSONObject();
        jsondata.put("name", data.Name);
        jsondata.put("card", data.Card);
        jsondata.put("kit", data.Kit);
        jsondata.put("state", data.State);

        try {
            @SuppressWarnings("resource") FileWriter file = new FileWriter(path);
            file.write(jsondata.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerData LoadData() {
        String path = "players"+File.separator+player.getName()+".json";
        JSONParser parser = new JSONParser();
        try {
            if (!(new File(path).exists())) {
                CreateFile();
                return new PlayerData(player.getName(), "null", "null", "null");
            }
            Object p = parser.parse(new FileReader(path));
            JSONObject jsonObject =(JSONObject) p;
            String name = (String) jsonObject.get("name");
            String card = (String) jsonObject.get("card");
            String kit = (String) jsonObject.get("kit");
            String state = (String) jsonObject.get("state");
            return new PlayerData(name, card, kit, state);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
