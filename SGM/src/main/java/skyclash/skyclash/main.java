package skyclash.skyclash;

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

import java.util.HashMap;
import java.util.List;

public class main extends JavaPlugin {
    public static HashMap<String, Integer> mapselection = new HashMap<>();
    public static HashMap<String, Integer> playermap = new HashMap<>();
    public static HashMap<String, String> playertracker = new HashMap<>();
    public static BukkitTask task;

    // Scoreboard
    public static int timer = 0;
    public static Scoreboard board;
    public static Scoreboard emptyboard;
    public static int PlayersLeft = 0;
    public static void incrementvalue(String key) {
        Integer num = mapselection.get(key);
        num++;
        mapselection.put(key, num);
    }
    public static void decrementvalue(String key) {
        Integer num = mapselection.get(key);
        num--;
        mapselection.put(key, num);
    }

    public Score s;
    public Score s2;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("SGM started");

        // handlers
        new handler_template(this);
        new lobby(this);
        new ingame(this);
        new CustomWeapons(this);

        // commands
        this.getCommand("startgame").setExecutor(new startgame());
        this.getCommand("setvotes").setExecutor(new adjust_votes());
        this.getCommand("giveitem").setExecutor(new GiveCustomItem());

        new BukkitRunnable(){
            @Override
            public void run(){
                Tick();
            }
        }.runTaskTimer(this, 0L, 1L);

        mapselection.put("Map1", 0);
        mapselection.put("Map2", 0);
        mapselection.put("Map3", 0);
        mapselection.put("Map4", 0);
        mapselection.put("Map5", 0);
        mapselection.put("Map6", 0);
        mapselection.put("Map7", 0);
        mapselection.put("Map8", 0);
        mapselection.put("Map9", 0);

        // Scoreboard
        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective("ingame", "dummy");
        o.setDisplayName(ChatColor.RED+"SKYCLASH!");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        s = o.getScore(ChatColor.YELLOW+"Time left: ");
        s2 = o.getScore(ChatColor.YELLOW+"Players left: ");
        s.setScore(timer);
        s2.setScore(PlayersLeft);

        emptyboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    }

    @Override
    public void onDisable() {

    }

    public void Tick() {
        List<World> world = Bukkit.getWorlds();
        world.forEach(world1 -> {
            world1.setStorm(false);
            world1.setThundering(false);
        });

        if (timer > 0) {
            timer = timer - 1;
        }
        s.setScore((int) Math.floor((double) timer / 20));
        s2.setScore(PlayersLeft);

    }
}
