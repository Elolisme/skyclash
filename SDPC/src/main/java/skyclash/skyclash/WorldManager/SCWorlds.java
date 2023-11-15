package skyclash.skyclash.WorldManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import skyclash.skyclash.main;
import skyclash.skyclash.chestgen.ChestManager;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.CopyWorld;
import skyclash.skyclash.fileIO.Mapsfile;

public class SCWorlds {
    public static final String INGAME_MAP = "ingame_map";
    private Multiverse multiverse = new Multiverse();
    private Mapsfile maps = new Mapsfile();
    
    @SuppressWarnings("unchecked")
    public Location getLobbyLocation() {
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        AtomicReference<String> default_world = new AtomicReference<>();
        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        AtomicInteger z = new AtomicInteger();
        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean isdefault = (boolean) info1.get("isdefault");
            String name1 = (String) name;
            JSONArray spawnsarray = StringToJSON.convert((String) info1.get("spawns"));
            JSONArray lists = (JSONArray) spawnsarray.get(0);
            if (isdefault) {
                default_world.set(name1);
                x.set(Integer.valueOf(String.valueOf(lists.get(0))));
                y.set(Integer.valueOf(String.valueOf(lists.get(1))));
                z.set(Integer.valueOf(String.valueOf(lists.get(2))));
            }
        });

        new Multiverse().LoadWorld(default_world.get());
        return new Location(new Multiverse().GetBukkitWorld(default_world.get()), x.get(), y.get(), z.get());
    }

    public void CopyWorld(String worldName, String newWorldName) {
        multiverse.LoadWorld(worldName);
        new CopyWorld().copyWorld(multiverse.GetBukkitWorld(worldName), INGAME_MAP);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import "+INGAME_MAP+" normal");
        main.isGameActive = true;
    }

    @SuppressWarnings("unchecked")
    public JSONArray getSpawnArray(String worldName) {
        AtomicReference<JSONObject> worldinfo = new AtomicReference<>();
        maps.readFile(false, false);
        maps.jsonObject.forEach((name, info) -> {
            String name1 = (String) name;
            JSONObject info1 = (JSONObject) info;
            if (name1.equals(worldName)) {
                worldinfo.set(info1);
            }
        });

        JSONObject info2 = worldinfo.get();
        return StringToJSON.convert((String) info2.get("spawns"));
    }

    public void teleportPlayer(Player player, JSONArray coords) {
        int x = Integer.valueOf(String.valueOf(coords.get(0)));
        int y = Integer.valueOf(String.valueOf(coords.get(1)));
        int z = Integer.valueOf(String.valueOf(coords.get(2)));
        Location spawnloc = new Location(new Multiverse().GetBukkitWorld(INGAME_MAP), x, y, z);
        player.teleport(spawnloc);
    }

    public void generateChestLoot(String worldName, Boolean notify) {
        maps.readFile(false, false);
        JSONObject info2 = (JSONObject) maps.jsonObject.get(worldName);
        JSONArray chestsarray = StringToJSON.convert((String) info2.get("chests"));
        new ChestManager(chestsarray, multiverse.GetBukkitWorld(INGAME_MAP), "spawn", "mid", true);

        if (!notify) {
            return;
        }
        
        Bukkit.getServer().getOnlinePlayers().forEach((player) -> {
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 0.8f);
            player.sendMessage(ChatColor.YELLOW+"Chests have been refilled");
        });
    }
}
