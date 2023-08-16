package skyclash.skyclash.ingame;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.Clock;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.main;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerDC implements Listener {
    public PlayerDC(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings({"UnnecessaryBoxing", "unchecked"})
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.get(player.getName()).equals("ingame")) {
            return;
        }
        Clock.playersLeft--;
        Bukkit.broadcastMessage(ChatColor.RED+player.getName()+" has died due to disconnecting");

        Bukkit.getScheduler().scheduleSyncDelayedTask(main.getPlugin(main.class), () -> {
            if (Clock.playersLeft <= 1) {
                new EndGame(false);
            }
        }, 5);
        // get lobby spawn location
        Mapsfile maps = new Mapsfile();
        maps.read_file(false, false);
        AtomicReference<String> default_world = new AtomicReference<>();
        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        AtomicInteger z = new AtomicInteger();
        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean isdefault = (boolean) info1.get("isdefault");
            String name1 = (String) name;
            JSONArray spawnsarray = StringToJSON.convert((String) info1.get("spawns"));
            JSONArray lists = (JSONArray) spawnsarray.get(0);
            if (isdefault) {
                default_world.set(name1);
                x.set(Integer.valueOf(String.valueOf(lists.get(0))));
                y.set(Integer.valueOf(String.valueOf(lists.get(1))));
                z.set(Integer.valueOf(String.valueOf(lists.get(2))));
            }
        });
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        worldManager.loadWorld(default_world.get());
        World w = Bukkit.getWorld(default_world.get());
        Location spawnloc = new Location(w, x.get(), y.get(), z.get());

        player.setScoreboard(Clock.emptyboard);
        player.getInventory().clear();
        player.getEquipment().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setSaturation(20);
        main.playerStatus.put(player.getName(), "lobby");
        main.playerVote.remove(player.getName());
        if (player.hasMetadata("NoMovement")) {
            player.removeMetadata("NoMovement", main.getPlugin(main.class));
        }
        player.teleport(spawnloc);
    }

}
