package skyclash.skyclash;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
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
import skyclash.skyclash.lobby.LobbyControls;
import java.util.HashMap;
import java.util.Random;

public class main extends JavaPlugin {

    public static String activeWorld;
    public static boolean isGameActive = false;
    public static MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    public static Plugin plugin;
    public static HashMap<Player, ItemStack[]> EnderChestItems = new HashMap<>();
    public static HashMap<String, Integer> killtracker = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = main.getPlugin(main.class);

        // handlers
        new LobbyControls(this);
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
        
        new Scheduler();
    }

    @Override
    public void onDisable() {
        Scheduler.End();  
    }



    // void world generator - https://github.com/chaseoes/VoidWorld
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
