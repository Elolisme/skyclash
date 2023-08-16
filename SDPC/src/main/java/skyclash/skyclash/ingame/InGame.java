package skyclash.skyclash.ingame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import skyclash.skyclash.main;

public class InGame implements Listener {
    public InGame(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // event handler
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        if (from.getZ() != event.getTo().getZ() && from.getX() != event.getTo().getX() && player.hasMetadata("NoMovement")) {
            player.teleport(event.getFrom());
        }
    }
}