package skyclash.skyclash.WorldManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import skyclash.skyclash.main;
import skyclash.skyclash.chestgen.ChestManager;
import skyclash.skyclash.fileIO.CopyWorld;
import skyclash.skyclash.fileIO.MapData;
import skyclash.skyclash.fileIO.Mapsfile;

public class SCWorlds {
    public static final String INGAME_MAP = "ingame_map";
    private Multiverse multiverse = new Multiverse();
    private Mapsfile maps = new Mapsfile();
    
    // Get the world which is the lobby, and get its first spawn location
    public Location getLobbySpawnLocation() {
        Mapsfile maps = new Mapsfile();
        maps.loadFileYML();
        AtomicReference<String> default_world = new AtomicReference<>();
        AtomicReference<ArrayList<Integer>> spawnCoords = new AtomicReference<>();

        // loop through all maps in maps.json
        maps.data.forEach((name, info) -> {
            // see if its lobby world
            String name1 = (String) name;
            if (info.getIsdefault()) {
                default_world.set(name1);
                spawnCoords.set(info.getSpawns().get(0));
            }
        });

        new Multiverse().LoadWorld(default_world.get());
        return new Location(new Multiverse().GetBukkitWorld(default_world.get()), spawnCoords.get().get(0), spawnCoords.get().get(1), spawnCoords.get().get(2));
    }

    public String getLobbyName() {
        Mapsfile maps = new Mapsfile();
        maps.loadFileYML();
        AtomicReference<String> default_world = new AtomicReference<>();

        // loop through all maps in maps.json
        maps.data.forEach((name, info) -> {
            // see if its lobby world
            String name1 = (String) name;
            if (info.getIsdefault()) {
                default_world.set(name1);
            }
        });

        return default_world.get();
    }

    public void CopyWorld(String worldName, String newWorldName) {
        multiverse.LoadWorld(worldName);
        new CopyWorld().copyWorld(multiverse.GetBukkitWorld(worldName), INGAME_MAP);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import "+INGAME_MAP+" normal");
        main.isGameActive = true;
    }

    // Get the spawn locations from maps.json of a certain world
    public ArrayList<ArrayList<Integer>> getSpawnArray(String worldName) {
        maps.loadFileYML();
        return maps.data.get(worldName).getSpawns();
    }

    public void teleportPlayer(Player player, ArrayList<Integer> coords) {
        Location spawnloc = new Location(new Multiverse().GetBukkitWorld(INGAME_MAP), coords.get(0), coords.get(1), coords.get(2));
        player.teleport(spawnloc);
    }

    public void generateChestLoot(String worldName, Boolean notify) {
        maps.loadFileYML();
        MapData info = maps.data.get(worldName);
        ArrayList<ArrayList<Integer>> chestsarray = info.getChests();
        new ChestManager(chestsarray, multiverse.GetBukkitWorld(INGAME_MAP));
        if (!notify) {
            return;
        }
        Bukkit.getServer().getOnlinePlayers().forEach((player) -> {
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 0.8f);
            player.sendMessage(ChatColor.YELLOW+"Chests have been refilled");
        });
    }
}
