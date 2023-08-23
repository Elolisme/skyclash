package skyclash.skyclash;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import skyclash.skyclash.commands.*;
import skyclash.skyclash.customitems.CustomItems;
import skyclash.skyclash.gameManager.InGame;
import skyclash.skyclash.gameManager.PlayerDC;
import skyclash.skyclash.gameManager.PlayerDeath;
import skyclash.skyclash.kitscards.KitAbilities;
import skyclash.skyclash.lobby.InMenu;
import skyclash.skyclash.lobby.LobbyControls;
import skyclash.skyclash.lobby.OpenMenuItem;

import java.util.HashMap;

public class main extends JavaPlugin {

    // GLOBALS
    public static HashMap<Integer, Integer> mapVotes = new HashMap<>();
    public static HashMap<String, Integer> playerVote = new HashMap<>();
    public static HashMap<String, String> playerStatus = new HashMap<>();
    public static String activeWorld;
    public static boolean isGameActive = false;
    public static MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    @Override
    public void onEnable() {
        // handlers
        new OpenMenuItem(this);
        new LobbyControls(this);
        new InMenu(this);
        new InGame(this);
        new CustomItems(this);
        new PlayerDC(this);
        new PlayerDeath(this);
        new KitAbilities(this);
        new Clock();

        // commands
        this.getCommand("startgame").setExecutor(new startgame());
        this.getCommand("setvotes").setExecutor(new adjust_votes());
        this.getCommand("giveitem").setExecutor(new giveitem());
        this.getCommand("endgame").setExecutor(new end_game());
        this.getCommand("lootchest").setExecutor(new lootchest());
        this.getCommand("lobby").setExecutor(new tolobby());
        this.getCommand("setchest").setExecutor(new setchest());
        this.getCommand("gamespawn").setExecutor(new gamespawn());

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Skyclash's Drug Pollinated Code has started");

        /* # LOGS
        Changes:
        - made maps start with correct settings
        - fixed giveitem command
        - fixed custom items not working? idk either
        - changed /startgame to start in 5 seconds, as opposed to 30
        - battle map does not spawn mobs, set to day on start
        - added /setchest [add|remove|list] command
        - added /gamespawn [add|remove|list] command as well

        Todo:
        - Add ender chest loot (ChestManager)
        */
    }

    @Override
    public void onDisable() {
        Clock.End();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Closing SDPC");
    }
}
