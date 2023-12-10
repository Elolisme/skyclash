package skyclash.skyclash.fileIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import skyclash.skyclash.WorldManager.Multiverse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MapsFile {
    public HashMap<String, MapData> data = new HashMap<>();
    private String mapFilePath = "plugins"+File.separator+"SDPC"+File.separator+"maps2.yml";
    
    public void createFile() {
        try {
            new File(mapFilePath).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFileYML() {
        try (FileWriter writer = new FileWriter(new File(mapFilePath))) {
            Representer representer = new Representer();
            representer.addClassTag(MapData.class, Tag.MAP);
            Yaml yaml = new Yaml(representer);
            yaml.dump(data, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFileYML() {
        if (!(new File(mapFilePath).exists())) {createFile();return;}
        Yaml yaml = new Yaml(new Constructor(HashMap.class));
        try (InputStream inputStream = new FileInputStream(new File(mapFilePath))) {
            HashMap<String, Object> loadedData = (HashMap<String, Object>) yaml.load(inputStream);
            loadedData.forEach((key, value) -> {
                Gson gson = new GsonBuilder().create();
                String jsonString = gson.toJson(value);
                data.put(key, (MapData) gson.fromJson(jsonString, MapData.class));
            });
            addNewWorlds();
        } catch (YAMLException | IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"There was an error opening the maps file");
            throw new RuntimeException(e);
        }
    }

    private void addNewWorlds() {
        new Multiverse().getMultiverseWorlds().forEach((world) -> {
            String worldname = world.getName();
            AtomicBoolean exists = new AtomicBoolean(false);
            data.forEach((name, data) -> {
                if (name.equals(worldname)) {
                    exists.set(true);
                }
            });

            if (!exists.get()) {
                MapData mapdata = new MapData("BEDROCK", false, new ArrayList<>(), new ArrayList<>(), false);
                data.put(worldname, mapdata);
            }
        });
    }

    public int getPlayableMaps() {
        AtomicInteger size = new AtomicInteger();
        data.forEach((name, info) -> {
            boolean ignore = info.getIgnore();
            if (!ignore) {
                size.getAndIncrement();
            }
        });
        return size.get();
    }
}