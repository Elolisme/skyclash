package skyclash.skyclash;

import static skyclash.skyclash.main.isGameActive;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.WorldManager.SCWorlds;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.fileIO.LootChestIO;
import skyclash.skyclash.fileIO.MOTD;
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
        checkVersion();

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Skyclash's Drug Pollinated Code has started");
        scheduleRepeatingTask(()->Tick(), 1);
        scheduleRepeatingTask(()->Second(), 20);
        scheduleRepeatingTask(()->new MOTD().changeMOTD(), 20*60*30);
        scheduleTask(()->{setupScoreboards();}, 1);
        new VoteMap().initialiseMaps();
        LootChestIO.downloadLootChestFiles();
    }

    private void setupScoreboards() {
        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective("Ingame", "dummy");
        o.setDisplayName(ChatColor.RED+"SKYCLASH!");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        s = o.getScore(ChatColor.YELLOW+"Time left: ");
        s2 = o.getScore(ChatColor.YELLOW+"Players left: ");
        emptyboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    }

    private void Tick() {
        Cooldown.handleCooldowns();
    }

    private void Second() {
        new Multiverse().everySecond();

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
        if (timer % 5 == 0 && timer != 0) {
            Abilities.Every5Seconds();
        } 
    }

    public static void CloseServer() {
        if (isGameActive) {new EndGame(true);}
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Closing SDPC");
    }

    // task delayer and repeater
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

    private static void checkVersion() {
        URL url;
        String latestVersion;
        try {
            url = new URL("https://raw.githubusercontent.com/Elolisme/skyclash/main/CHANGELOG.md");
            List<String> lines = Resources.readLines(url, Charsets.UTF_8);
            latestVersion = lines.get(1).replace("## v", "");
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Could not get file with version from Github");
            return;
        }

        File folder = new File("plugins/");
        File[] listOfFiles = folder.listFiles();
        String version = "";
        File oldfile = null;

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().contains("SDPC-")) {
                oldfile = file;
                version = file.getName().replace("SDPC-", "").replace(".jar", "");
            }
        }

        if (oldfile == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"How");
            return;
        }

        if (version.equals(latestVersion)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"SDPC is up to date");
            return;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"Not up to date, should be on version "+latestVersion);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"Downloading the latest version of SDPC...");

        try {
            URL url2 = new URL("https", "github.com", "/Elolisme/skyclash/blob/main/SDPC/target/SDPC-"+latestVersion+".jar");
            File file = new File( "plugins" + File.separator + "SDPC-"+latestVersion+".jar");
            FileUtils.copyURLToFile(url2, file, 10*1000, 10*1000);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"The download failed");
            return;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"The files have been downloaded");

        try {
            Bukkit.shutdown();
            oldfile.delete();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Restart the server immediately");
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Could not delete files or stop server");
            return;
        }
    }
}