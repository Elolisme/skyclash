package skyclash.skyclash;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import skyclash.skyclash.chestgen.OpenEChest;
import skyclash.skyclash.commands.*;
import skyclash.skyclash.customitems.CustomItems;
import skyclash.skyclash.gameManager.InGame;
import skyclash.skyclash.gameManager.PlayerDC;
import skyclash.skyclash.gameManager.PlayerDeath;
import skyclash.skyclash.kitscards.Abilities;
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
        new Abilities(this);
        new OpenEChest(this);
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
        Changes 1.2.0:
        - added swordsman true damage every 10 seconds and beserker resistance on low health (5) passives
        - made bigger bangs cause nearby explosions within 25 blocks have a greater explosion radius
        - made bigger bangs spawn with flint and steel and 2 tnt
        - fixed xp clearing
        - fixed players being able to hit each other maybe
        - added grace period of 5 seconds
        - make setchest list show locations that have no chests

        Todo:
        - add setchest wand item
        - add enchants to chest items
        - add pearl cooldown
        - add kdr and lifetime stats, half done
        */
    }

    @Override
    public void onDisable() {
        Clock.End();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Closing SDPC");
    }
}
