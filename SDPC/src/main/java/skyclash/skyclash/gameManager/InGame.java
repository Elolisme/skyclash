package skyclash.skyclash.gameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import skyclash.skyclash.main;

public class InGame implements Listener {
    public InGame(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // event handlers
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        if (from.getZ() != event.getTo().getZ() && from.getX() != event.getTo().getX() && player.hasMetadata("NoMovement")) {
            player.teleport(event.getFrom());
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getEntity().getKiller() instanceof Player) {
                Player player = event.getEntity().getKiller();
                if (main.playerStatus.get(player.getName()).equals("ingame")) {
                    player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
                    StatsManager.changeStat(player, "kills", 1);
                    StatsManager.changeStat(player, "coins", 1);
                }
            }
        }
    }
}