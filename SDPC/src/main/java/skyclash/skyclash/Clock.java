package skyclash.skyclash;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.chestgen.ChestManager;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.gameManager.EndGame;

import java.util.List;

import static skyclash.skyclash.main.*;

public class Clock {
    static Score s;
    static Score s2;
    public static Scoreboard board;
    public static Scoreboard emptyboard;
    public static int timer = 0;
    public static int playersLeft = 0;
    public Clock() {
        init();
        new BukkitRunnable(){
            @Override
            public void run(){
                Tick();
            }
        }.runTaskTimer(main.getPlugin(main.class), 0L, 1);
        new BukkitRunnable(){
            @Override
            public void run(){
                Second();
            }
        }.runTaskTimer(main.getPlugin(main.class), 0L, 20);
    }
    void init() {
        // Maps
        Mapsfile maps = new Mapsfile();
        maps.read_file(true, true);
        for (int i = 1; i <= maps.get_size(); i++) {
            mapVotes.put(i, 0);
        }

        // Scoreboard
        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective o = board.registerNewObjective("Ingame", "dummy");
        o.setDisplayName(ChatColor.RED+"SKYCLASH!");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        s = o.getScore(ChatColor.YELLOW+"Time left: ");
        s2 = o.getScore(ChatColor.YELLOW+"Players left: ");
        emptyboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    }

    void Tick() {
        Cooldown.handleCooldowns();
    }
    void Second() {
        List<World> world = Bukkit.getWorlds();
        world.forEach(world1 -> {
            world1.setStorm(false);
            world1.setThundering(false);
        });
        if (timer > 0) {
            timer = timer - 1;
        }
        s.setScore(timer);
        s2.setScore(playersLeft);

        switch (timer) {
            case 420:
            case 60:
            case 240:
                ChestRefill();
                break;
            case 1:
                new EndGame(true);
                break;
        }
    }

    Mapsfile maps = new Mapsfile();
    void ChestRefill() {
        // chest loot generation
        Bukkit.getServer().getOnlinePlayers().forEach((player) -> player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 0.8f));
        maps.read_file(false, false);
        JSONObject info2 = (JSONObject) maps.jsonObject.get(activeWorld);
        JSONArray chestsarray = StringToJSON.convert((String) info2.get("chests"));
        World world1 = Bukkit.getWorld("ingame_map");
        ChestManager cm = new ChestManager(chestsarray, world1, "spawn", "mid");
        cm.loadChestLoot();

        Bukkit.broadcastMessage(ChatColor.YELLOW+"Chests have been refilled");
    }

    public static void End() {
        if (isGameActive) {
            new EndGame(true);
        }
    }
}
