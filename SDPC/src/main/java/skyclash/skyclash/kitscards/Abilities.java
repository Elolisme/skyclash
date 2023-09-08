package skyclash.skyclash.kitscards;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import skyclash.skyclash.main;
import skyclash.skyclash.cooldowns.Cooldown;

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
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*4, 1));
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

    // homing arrows; credit to BlingHomingArrows
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        // player check
        if (!(e.getEntity() instanceof Player)) {return;}
        Player player = (Player) e.getEntity();

        // archer checks
        if (!(main.playerStatus.get(player.getName()).equals("ingame"))) {return;}
        if (!(player.hasMetadata("Archer"))) {return;}

        // 10% check
        int n = new Random().nextInt(100);
        if (n<50) {return;}

        // code
        double minAngle = 6.283185307179586D;
        Entity minEntity = null;
        for (Entity entity : player.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
            if ((player.hasLineOfSight(entity)) && ((entity instanceof LivingEntity)) && (!entity.isDead())) {
                Vector toTarget = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
                double angle = e.getProjectile().getVelocity().angle(toTarget);
                if (angle < minAngle) {
                    minAngle = angle;
                    minEntity = entity;
                }
            }
        }
        if (minEntity != null) {
            new HomingTask((Arrow)e.getProjectile(), (LivingEntity)minEntity);
        }
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent event) {
        // Frost Knight
        if (event.getDamager().getType() != EntityType.SNOWBALL) {return;}
        Bukkit.getLogger().info("snowball hit");
        Projectile snowball = (Projectile) event.getDamager();
        if (!(snowball.getShooter() instanceof Player)) {return;}
        Bukkit.getLogger().info("found shooter");
        Player shooter = (Player) snowball.getShooter();
        if (!(event.getEntity() instanceof Player)) {return;}
        Player player = (Player) event.getEntity();
        
        if (!shooter.hasMetadata("Frost_Knight")) {return;}
        Bukkit.getLogger().info("is frost knight");
        shooter.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 1, false, false), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0, false, false), true);
        shooter.playSound(shooter.getLocation(), Sound.ARROW_HIT, 1, 0.9f);
    }

    @EventHandler
    public void onLowHealth2(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.hasMetadata("Guardian")) {
            return;
        }
        // Guardian
        if (player.getHealth() < 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 1));
        }

    }

}
