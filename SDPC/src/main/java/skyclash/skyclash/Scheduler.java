package skyclash.skyclash;

import static skyclash.skyclash.main.isGameActive;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.WorldManager.SCWorlds;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.fileIO.MOTD;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.gameManager.EndGame;
import skyclash.skyclash.kitscards.Abilities;
import skyclash.skyclash.lobby.VoteMap;

public class Scheduler {

    private Score s;
    private Score s2;
    public static Scoreboard board;
    public static Scoreboard emptyboard;
    public static int timer = 0;
    public static int playersLeft = 0;

    public void init() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Skyclash's Drug Pollinated Code has started");
        scheduleRepeatingTask(()->Tick(), 1);
        scheduleRepeatingTask(()->Second(), 20);
        scheduleRepeatingTask(()->new MOTD().changeMOTD(), 20*60*30);

        // Maps
        Mapsfile maps = new Mapsfile();
        maps.readFile(true, true);
        for (int i = 1; i <= maps.getMapArraySize(); i++) {
            new VoteMap().addMap(i);
        }

        scheduleTask(()->{
            // Scoreboard
            board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
            Objective o = board.registerNewObjective("Ingame", "dummy");
            o.setDisplayName(ChatColor.RED+"SKYCLASH!");
            o.setDisplaySlot(DisplaySlot.SIDEBAR);
            s = o.getScore(ChatColor.YELLOW+"Time left: ");
            s2 = o.getScore(ChatColor.YELLOW+"Players left: ");
            emptyboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        }, 1);
    }

    public void Tick() {
        Cooldown.handleCooldowns();
    }

    public void Second() {
        // world settings
        List<World> world = Bukkit.getWorlds();
        world.forEach(world1 -> {
            world1.setStorm(false);
            world1.setThundering(false);
        });

        // timer related events
        if (timer > 0) {
            timer = timer - 1;
            s.setScore(timer);
            s2.setScore(playersLeft);
        }
        if (timer == 420) {
            new Multiverse().GetBukkitWorld(SCWorlds.INGAME_MAP).getWorldBorder().setSize(20, 400);
        }
        if (timer == 1) {
            new EndGame(true);
        }
        if (timer % 5 == 0 && timer != 0) {Abilities.Every5Seconds();} 
    }

    public static void CloseServer() {
        if (isGameActive) {new EndGame(true);}
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Closing SDPC");
    }

    public BukkitTask scheduleTask(Runnable task, int delay) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLater(main.plugin, delay);
    }

    public void scheduleRepeatingTask(Runnable task, int periodTicks) {
        new BukkitRunnable(){@Override
            public void run() {
                task.run();
            }
        }.runTaskTimer(main.plugin, 0L, periodTicks);
    }
}