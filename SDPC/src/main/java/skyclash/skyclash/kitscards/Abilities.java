package skyclash.skyclash.kitscards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.main;

import java.util.List;

public class Abilities implements Listener {
    public Abilities(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onThrowPearl(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (!(event.getEntity().getType() == EntityType.ENDER_PEARL)) {
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (!player.hasMetadata("Assassin")) {
            return;
        }

        // Assassin
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
    }

    @EventHandler
    public void onLowHealth(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.hasMetadata("Beserker")) {
            return;
        }

        // Beserker
        if (player.getHealth() < 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*2, 1));
        }

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!player.hasMetadata("Swordsman")) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        Bukkit.getLogger().info(((LivingEntity)entity).getHealth()+" health");
        if(Cooldown.isCooling(player.getName(), "TrueDamage")) {
            return;
        }


        // Swordsman
        double damage = event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE);
        double extra_damage = damage - event.getFinalDamage();
        ((LivingEntity)entity).damage(extra_damage);
        Cooldown.add(player.getName(), "TrueDamage", 10, System.currentTimeMillis());
        player.sendMessage(ChatColor.GREEN+"Dealt "+extra_damage+" more damage");
    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent event) {
        // Bigger Bangs
        List<Entity> entities = event.getEntity().getNearbyEntities(25, 25, 25);
        entities.forEach((entity) -> {
            if (entity.hasMetadata("Bigger Bangs")) {
                event.setRadius(7);
                event.setFire(true);
                entity.sendMessage(ChatColor.RED+"BOOM!");
            }
        });
    }

}
