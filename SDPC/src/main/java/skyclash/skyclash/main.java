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
        Changes 1.1.3:
        - actaully removed tags from players at start and end of game
        - fixed players being able to hit each other in lobby
        - nerfs: swordsman strength level to 1 (from 2)
                 assassin now gets 1 pearl and invis pot (from 2 each)
                 beserker pots are 15 seconds (from 60)
                 beserker pots are regen 2, resistance 1 (from 3, 2)
        - fixed dc's
        - fixed armour clearing
        - fixed effect clearing
        - fixed spectators dcing
        - fixed startgame not working sometimes due to offline players being ready
        - fixed /lobby for deopped players


        Todo:
        - make setchest list show locations that have no chests
        - add setchest wand item
        - add enchants to chest items
        - add pearl cooldown
        */
    }

    @Override
    public void onDisable() {
        Clock.End();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Closing SDPC");
    }
}
