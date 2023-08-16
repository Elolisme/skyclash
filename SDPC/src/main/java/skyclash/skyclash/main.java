package skyclash.skyclash;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import skyclash.skyclash.commands.*;
import skyclash.skyclash.customitems.CustomWeapons;
import skyclash.skyclash.ingame.InGame;
import skyclash.skyclash.ingame.PlayerDC;
import skyclash.skyclash.ingame.PlayerDeath;
import skyclash.skyclash.kitscards.KitAbilities;
import skyclash.skyclash.lobbymenu.InMenu;
import skyclash.skyclash.lobbymenu.LobbyControls;
import skyclash.skyclash.lobbymenu.OpenMenuItem;

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
        new CustomWeapons(this);
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

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Skyclash's Drug Pollinated Code has started");
    }

    @Override
    public void onDisable() {
        Clock.End();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Closing SDPC");
    }
}
