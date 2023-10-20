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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import skyclash.skyclash.Clock;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.main;

public class PlayerDeath implements Listener {
    public PlayerDeath(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private static Location respawnLoc;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!main.playerStatus.get(player.getName()).equals("ingame")) {
            return;
        }
        // code
        StatsManager.changeStat(player, "deaths", 1);
        if (Clock.timer > 570) {
            StatsManager.changeStat(player, "30s Deaths", 1);
        }
        if (event.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
            StatsManager.changeStat(player, "Void deaths", 1);
        }
        
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        main.playerStatus.put(player.getName(), "spectator");
        Clock.playersLeft--;
        respawnLoc = event.getEntity().getLocation();
        player.spigot().respawn();
        
        new RemoveTags(player);

        if (Clock.playersLeft <= 1) {
            new EndGame(false);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.get(player.getName()).equals("spectator")) {
            return;
        }
        player.getWorld().strikeLightningEffect(respawnLoc);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(ChatColor.RED+"Better luck next time");
        player.getInventory().clear();
        if (player.getLocation().getY() < 0) {respawnLoc.setY(100);}
        player.teleport(respawnLoc);
    }

}
