package skyclash.skyclash.gameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
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
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = event.getEntity();
        if (!main.playerStatus.get(player.getName()).equals("ingame")) {
            return;
        }
        if (!(event.getEntity().getKiller() instanceof Player)) {
            return;
        }
        Player killer = event.getEntity().getKiller();
        if (!main.playerStatus.get(killer.getName()).equals("ingame")) {
            return;
        }
        if (event.getEntity().getName() == killer.getName()) {
            killer.sendMessage(ChatColor.YELLOW+"You killed yourself! Kill credit revoked");
            return;
        }

        killer.playSound(killer.getLocation(), Sound.WITHER_DEATH, 1, 1.3f);
        StatsManager.changeStat(killer, "kills", 1);
        StatsManager.changeStat(killer, "coins", 10);
        killer.sendMessage(ChatColor.YELLOW+"+10 coins for kill");
        StatsManager.addKill(killer);

        if (player.getName().equals("xEzKillz")) {
            StatsManager.changeStat(killer, "xEz Killz", 1);
        }

        if (killer.hasMetadata("Necromancer")) {
            killer.getWorld().spawn(killer.getLocation(), Zombie.class);
        }

        if (killer.hasMetadata("Hit and Run")) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15,1));
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.SKELETON ^ entity.getType() == EntityType.ZOMBIE) {
            try {
                if (event.getTarget().hasMetadata("Necromancer")) {
                    event.setCancelled(true);
                }
            } catch (NullPointerException e) {return;}
        }
        if (entity.getType() == EntityType.CREEPER) {
            try {
                if (event.getTarget().hasMetadata("Creeper")) {
                    event.setCancelled(true);
                }
            } catch (NullPointerException e) {return;}
        }
    }
}