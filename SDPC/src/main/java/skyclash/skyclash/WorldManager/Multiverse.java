package skyclash.skyclash.WorldManager;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

public class Multiverse {
    public MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    private MVWorldManager worldManager = mvcore.getMVWorldManager();

    public Boolean LoadWorld(String worldName) {
        return worldManager.loadWorld(worldName);
    }

    public MultiverseWorld GetWorld(String worldName) {
        if (LoadWorld(worldName)) {
            return worldManager.getMVWorld(worldName);
        }
        return null;
    }

    public World GetBukkitWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            LoadWorld(worldName);
            world = Bukkit.getWorld(worldName);
        }
        return world;
    }

    public void DeleteWorld(String worldName) {
        if (GetWorld(worldName) != null) {
            worldManager.deleteWorld(worldName, true, true);
        }
    }

    public void SaveAll() {
        List<World> worlds = Bukkit.getWorlds();
        for (World world: worlds) {
            world.save();
        }
    }

    public void PrepareWorld(String worldName) {
        MultiverseWorld mvworld = GetWorld(worldName);
        World world = GetBukkitWorld(worldName);
        mvworld.setAllowMonsterSpawn(false);
        mvworld.setBedRespawn(false);
        world.setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");
        world.setTime(6000);
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(300);
    }

    public Collection<MultiverseWorld> getMultiverseWorlds() {
        return mvcore.getMVWorldManager().getMVWorlds();
    }

    public MultiverseWorld getSpawnWorld() {
        return mvcore.getMVWorldManager().getFirstSpawnWorld();
    }

    public void setSpawnWorld(String worldName) {
        mvcore.getMVWorldManager().setFirstSpawnWorld(worldName);
    }

    public Boolean createNewSCWorld(String worldName) {
        if (worldManager.getMVWorld(worldName) != null) {return false;}
        worldManager.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "SDPC");
        World newWorld = GetBukkitWorld(worldName);
        MultiverseWorld newMVworld = worldManager.getMVWorld(newWorld);

        newWorld.getBlockAt(0, 64, 0).setType(Material.GLASS);
        newWorld.setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");
        newWorld.setTime(6000);

        newMVworld.setSpawnLocation(new Location(newWorld, 0, 65, 0));
        newMVworld.setAllowMonsterSpawn(false);
        newMVworld.setBedRespawn(false);
        newMVworld.setGameMode(GameMode.CREATIVE);
        return true;
    }

}
