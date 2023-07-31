package skyclash.skyclash.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import skyclash.skyclash.main;

public class handler_template implements Listener {
    public handler_template(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // event handler
    @EventHandler
    public void onTick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
    }
}