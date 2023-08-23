package skyclash.skyclash.gameManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import skyclash.skyclash.Clock;
import skyclash.skyclash.main;

public class PlayerDeath implements Listener {
    public PlayerDeath(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!main.playerStatus.containsKey(player.getName())) {
            main.playerStatus.put(player.getName(), "lobby");
        }

        if (!main.playerStatus.get(player.getName()).equals("gameManager")) {
            return;
        }
        // code
        main.playerStatus.put(player.getName(), "spectator");
        player.setGameMode(GameMode.SPECTATOR);
        Location loc = event.getEntity().getLocation();
        player.getWorld().strikeLightningEffect(loc);
        Bukkit.getScheduler().scheduleSyncDelayedTask(main.getPlugin(main.class), () -> {
            player.spigot().respawn();
            player.teleport(loc);
            if (player.getLocation().getY() < 0) {
                Location loc2 = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 100, player.getLocation().getZ());
                player.teleport(loc2);
            }
        }, 2);
        player.getInventory().clear();
        Clock.playersLeft--;
        player.sendMessage(ChatColor.RED+"Better luck next time");


        Bukkit.getScheduler().scheduleSyncDelayedTask(main.getPlugin(main.class), () -> {
            if (Clock.playersLeft <= 1) {
                new EndGame(false);
            }
        }, 5);
    }

}