package skyclash.skyclash.fileIO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.ChatColor;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataFiles {
    public PlayerData data;
    private String playerName;
    private static String folderPath = "plugins"+File.separator+"SDPC"+File.separator+"players";

    public DataFiles(Player player) {
        this.playerName = player.getName();
        loadFile();
    }

    public DataFiles(String name) {
        this.playerName = name;
        loadFile();
    }
    
    public void SetData(PlayerData data) {
        this.data = data;
        saveFile();
    }

    public void createFile() {
        File folder = new File(folderPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        File playerFile = new File(folderPath+File.separator+playerName+".json");
        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                data = new PlayerData(playerName);
                saveFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveFile() {
        String path = folderPath+File.separator+playerName+".json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile() {
        String path = folderPath+File.separator+playerName+".json";
        try {
            if (!(new File(path).exists())) {
                createFile();
                data = new PlayerData(playerName);
                return;
            }
            JsonObject object = new JsonParser().parse(new FileReader(path)).getAsJsonObject();
            Gson gson = new Gson();
            PlayerData playerData = gson.fromJson(object, PlayerData.class);
            data = playerData;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"There was an error opening the player file");
            throw new RuntimeException(e);
        }
    }
}