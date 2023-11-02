package skyclash.skyclash.fileIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import skyclash.skyclash.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Mapsfile {
    public JSONObject jsonObject = new JSONObject();
    File mapsjson = new File("plugins"+File.separator+"maps.json");
    String spawnworld = main.mvcore.getMVWorldManager().getFirstSpawnWorld().getName();
    Collection<MultiverseWorld> worlds = main.mvcore.getMVWorldManager().getMVWorlds();

    public Mapsfile() {
    }

    public void createFile() {
        try {
            //noinspection ResultOfMethodCallIgnored
            mapsjson.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile() {
        try (FileWriter file = new FileWriter(mapsjson)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, file);
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile(boolean update, boolean write) {
        if (!mapsjson.exists()) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Warning: You have no 'plugins/maps.json' file, some things will break!");
            createFile();
        } else {
            org.json.simple.parser.JSONParser parser = new JSONParser();
            try {
                jsonObject = (JSONObject) parser.parse(new FileReader(mapsjson));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (update) {
            addNewWorld();
        }
        if (write) {
            writeFile();
        }

    }

    @SuppressWarnings("unchecked")
    public void addNewWorld() {
        worlds.forEach((world) -> {
            String worldname = world.getName();
            AtomicBoolean exists = new AtomicBoolean(false);

            jsonObject.forEach((name, data) -> {
                String name1 = (String) name;
                if (name1.equals(worldname)) {
                    exists.set(true);
                }
            });

            if (!exists.get()) {
                JSONObject mapdata = new JSONObject();
                boolean isspawn = worldname.equals(spawnworld);
                JSONArray defaultcoords = new JSONArray();
                defaultcoords.add(0);
                defaultcoords.add(64);
                defaultcoords.add(0);
                JSONArray coordslist = new JSONArray();
                coordslist.add(defaultcoords);
                String coords = String.valueOf(coordslist);
                mapdata.put("isdefault", isspawn);
                mapdata.put("ignore", false);
                mapdata.put("chests", coords);
                mapdata.put("spawns", coords);
                mapdata.put("icon", "BEDROCK");
                jsonObject.put(worldname, mapdata);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public int get_size() {
        AtomicInteger size = new AtomicInteger();
        jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean ignore = (boolean) info1.get("ignore");
            if (!ignore) {
                size.getAndIncrement();
            }
        });

        return size.get();
    }
}
