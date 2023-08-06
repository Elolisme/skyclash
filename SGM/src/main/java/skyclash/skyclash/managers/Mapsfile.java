package skyclash.skyclash.managers;

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
    File mapsjson = new File("plugins/maps.json");
    String spawnworld = main.mvcore.getMVWorldManager().getFirstSpawnWorld().getName();
    Collection<MultiverseWorld> worlds = main.mvcore.getMVWorldManager().getMVWorlds();

    public Mapsfile() {
    }

    public void create_file() {
        try {
            //noinspection ResultOfMethodCallIgnored
            mapsjson.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write_file() {
        try {
            @SuppressWarnings("resource") FileWriter file = new FileWriter(mapsjson);
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read_file(boolean update, boolean write) {
        if (!mapsjson.exists()) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Warning: You have no 'plugins/maps.json' file, some things will break!");
            create_file();
        } else {
            org.json.simple.parser.JSONParser parser = new JSONParser();
            try {
                jsonObject = (JSONObject) parser.parse(new FileReader(mapsjson));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (update) {
            update_info();
        }
        if (write) {
            write_file();
        }

    }

    @SuppressWarnings("unchecked")
    public void update_info() {
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
                defaultcoords.add(0);
                defaultcoords.add(0);
                JSONArray coordslist = new JSONArray();
                coordslist.add(defaultcoords);
                coordslist.add(defaultcoords);
                mapdata.put("isdefault", isspawn);
                mapdata.put("ignore", false);
                mapdata.put("chests", coordslist);
                mapdata.put("spawns", coordslist);
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
