package skyclash.skyclash;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import skyclash.skyclash.commands.GiveCustomItem;
import skyclash.skyclash.commands.adjust_votes;
import skyclash.skyclash.commands.startgame;
import skyclash.skyclash.handlers.CustomWeapons;
import skyclash.skyclash.handlers.handler_template;
import skyclash.skyclash.handlers.ingame;
import skyclash.skyclash.handlers.lobby;
import skyclash.skyclash.managers.Mapsfile;

import java.util.HashMap;
import java.util.List;

public class main extends JavaPlugin {

    // GLOBALS
    public static HashMap<Integer, Integer> mapselection = new HashMap<>();
    public static HashMap<String, Integer> playermap = new HashMap<>();
    public static HashMap<String, String> playertracker = new HashMap<>();
    public static BukkitTask task;
    public static MultiverseWorld current_world;
    public static MultiverseCore mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    // Scoreboard
    public static Scoreboard board;
    public static Scoreboard emptyboard;
    public static int timer = 0;
    public static int PlayersLeft = 0;
    Score s;
    Score s2;

    @Override
    public void onEnable() {

        // handlers
        new handler_template(this);
        new lobby(this);
        new ingame(this);
        new CustomWeapons(this);

        // commands
        this.getCommand("startgame").setExecutor(new startgame());
        this.getCommand("setvotes").setExecutor(new adjust_votes());
        this.getCommand("giveitem").setExecutor(new GiveCustomItem());

        // Maps
        Mapsfile maps = new Mapsfile();
        maps.read_file(true, true);
        for (int i = 1; i <= maps.get_size(); i++) {
            mapselection.put(i, 0);
        }

        // Scoreboard
        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective("ingame", "dummy");
        o.setDisplayName(ChatColor.RED+"SKYCLASH!");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        s = o.getScore(ChatColor.YELLOW+"Time left: ");
        s2 = o.getScore(ChatColor.YELLOW+"Players left: ");
        emptyboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"SGM has started");

        new BukkitRunnable(){
            @Override
            public void run(){
                Second();
            }
        }.runTaskTimer(this, 0L, 20);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Closing SGM");
    }

    public void Second() {
        List<World> world = Bukkit.getWorlds();
        world.forEach(world1 -> {
            world1.setStorm(false);
            world1.setThundering(false);
        });
        if (timer > 0) {
            timer = timer - 1;
        }
        s.setScore(timer);
        s2.setScore(PlayersLeft);
    }
}
