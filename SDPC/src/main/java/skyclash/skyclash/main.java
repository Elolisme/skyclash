package skyclash.skyclash;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import skyclash.skyclash.chestgen.OpenEChest;
import skyclash.skyclash.commands.*;
import skyclash.skyclash.customitems.CustomItems;
import skyclash.skyclash.gameManager.InGame;
import skyclash.skyclash.gameManager.PlayerDC;
import skyclash.skyclash.gameManager.PlayerDeath;
import skyclash.skyclash.kitscards.Abilities;
import skyclash.skyclash.lobby.MenuLogic;
import skyclash.skyclash.lobby.LobbyListeners;
import java.util.Random;

// List of all my sources for code:
// main.java        | void world generator - https://github.com/chaseoes/VoidWorld
// chestgen         | chest generation - https://github.com/Veraimt/CustomLootChest
// cooldown         | ability cooldowns - https://bukkit.org/threads/tutorial-better-cooldowns.165811/
// abilities.java   | homing arrows - https://bukkit.org/threads/make-an-arrow-follow-a-target.247755/

public class main extends JavaPlugin {
    public static boolean isGameActive = false;
    public static Plugin plugin;
    
    @Override
    public void onEnable() {
        // handlers
        new LobbyListeners(this);
        new MenuLogic(this);
        new InGame(this);
        new CustomItems(this);
        new PlayerDC(this);
        new PlayerDeath(this);
        new Abilities(this);
        new OpenEChest(this);
        
        // commands
        this.getCommand("startgame").setExecutor(new startgame());
        this.getCommand("setvotes").setExecutor(new adjust_votes());
        this.getCommand("giveitem").setExecutor(new giveitem());
        this.getCommand("endgame").setExecutor(new end_game());
        this.getCommand("lobby").setExecutor(new tolobby());
        this.getCommand("setchest").setExecutor(new setchest());
        this.getCommand("gamespawn").setExecutor(new gamespawn());
        this.getCommand("leaderboard").setExecutor(new leaderboard());
        this.getCommand("scworld").setExecutor(new scworld());
        
        plugin = main.getPlugin(main.class);
        new Scheduler().init();
    }

    @Override
    public void onDisable() {
        Scheduler.CloseServer();  
    }

    // SDPC void world generator
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidWorldGenerator();
    }

    public class VoidWorldGenerator extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}
