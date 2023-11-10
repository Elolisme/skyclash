package skyclash.skyclash.gameManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.main;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;

public class PlayerDeath implements Listener {
    public PlayerDeath(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private PlayerStatus pStatus = new PlayerStatus();
    private Scheduler scheduler = new Scheduler();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!pStatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
            return;
        }

        // Change stats
        new StatsManager().changeStat(player, "deaths", 1);
        if (Scheduler.timer > 570) {
            new StatsManager().changeStat(player, "30s Deaths", 1);
        }
        if (event.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
            new StatsManager().changeStat(player, "Void deaths", 1);
        }

        player.sendMessage(ChatColor.RED+"Better luck next time");
        pStatus.SetStatus(player, PlayerState.SPECTATOR);
        player.setGameMode(GameMode.SPECTATOR);
        
        Location loc = event.getEntity().getLocation();
        player.getWorld().strikeLightningEffect(loc);
        Scheduler.playersLeft--;
        new RemoveTags(player);
        
        // Respawning
        scheduler.scheduleTask(() -> player.spigot().respawn(), 1);
        scheduler.scheduleTask(() -> {
            player.teleport(loc);
            if (player.getLocation().getY() < 64) {
                Location loc2 = new Location(player.getWorld(), player.getLocation().getX(), 100, player.getLocation().getZ());
                player.teleport(loc2);
            }
        }, 3);

        EndGame.CheckGameEnded();
    }
}