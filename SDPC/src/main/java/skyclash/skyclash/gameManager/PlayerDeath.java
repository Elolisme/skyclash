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
import org.bukkit.potion.PotionEffect;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.main;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;

public class PlayerDeath implements Listener {
    public PlayerDeath(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private PlayerStatus pStatus = new PlayerStatus();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!pStatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
            return;
        }

        new StatsManager().changeStat(player, "deaths", 1);
        if (Scheduler.timer > 570) {
            new StatsManager().changeStat(player, "30s Deaths", 1);
        }
        if (event.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
            new StatsManager().changeStat(player, "Void deaths", 1);
        }

        pStatus.SetStatus(player, PlayerState.SPECTATOR);
        player.setGameMode(GameMode.SPECTATOR);
        Location loc = event.getEntity().getLocation();
        player.getWorld().strikeLightningEffect(loc);
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, () -> {
            player.spigot().respawn();
        }, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, () -> {
            player.teleport(loc);
            if (player.getLocation().getY() < 64) {
                Location loc2 = new Location(player.getWorld(), player.getLocation().getX(), 100, player.getLocation().getZ());
                player.teleport(loc2);
            }
        }, 3);

        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        Scheduler.playersLeft--;
        player.sendMessage(ChatColor.RED+"Better luck next time");
        new RemoveTags(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, () -> {
            if (Scheduler.playersLeft <= 1) {
                new EndGame(false);
            }
        }, 5);
    }

}