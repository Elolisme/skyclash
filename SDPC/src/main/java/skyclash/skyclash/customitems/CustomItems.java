package skyclash.skyclash.customitems;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import skyclash.skyclash.Scheduler;
import skyclash.skyclash.main;
import skyclash.skyclash.cooldowns.Cooldown;

public class CustomItems implements Listener {
    public CustomItems(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    @Deprecated
    public void onWeaponUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // checks
        if (player.getItemInHand().getType() != Material.FIREWORK_CHARGE) {
            return;
        }
        if (!player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Master Spark")) {
            return;
        }

        // code to run
        player.sendMessage("Player used Master Spark");

        // particles
        Location location = player.getLocation();

        for (int j = 0; j <= 3; j++) {
            for (int i = 0; i <= 6; i++) {
                float move_distance_current = 0.5f;
                int j1 = j;
                new Scheduler().scheduleTask(() -> player.spigot().playEffect(location.add(player.getLocation().getDirection().multiply(move_distance_current * j1)), Effect.FLAME, 0, 0, 0.01f, 0.01f, 0.01f, 0.1f, 10, 50), 5 * i);
            }
        }

        //remove item
        ItemStack item = player.getItemInHand();
        int itemAmount = item.getAmount();
        if (item.getAmount() == 1) {
            player.getInventory().remove(item);
        } else {
            item.setAmount(itemAmount - 1);
        }
    }
    @EventHandler
    public void onShootJBow(EntityShootBowEvent event) {
        // checks
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();
        if (player.getItemInHand().getType() != Material.BOW) {
            return;
        }
        String displayname = player.getItemInHand().getItemMeta().getDisplayName();
        if (displayname == null) {
            return;
        }

        if (!displayname.equals(ChatColor.RED + "Jockey Bow")) {
            return;
        }

        // bow ability
        float arrowforce = event.getForce();
        arrow.remove();
        Location location = player.getEyeLocation();
        Chicken chicken = (Chicken) player.getWorld().spawnEntity(location, EntityType.CHICKEN);
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setBaby(true);
        chicken.setPassenger(zombie);
        Vector velocity = location.getDirection().multiply(2 * arrowforce);
        chicken.setVelocity(velocity);
    }

    @EventHandler
    public void onShootEbow(EntityShootBowEvent event) {
        // checks
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();
        if (player.getItemInHand().getType() != Material.BOW) {
            return;
        }
        String displayname = player.getItemInHand().getItemMeta().getDisplayName();
        if (displayname == null) {
            return;
        }

        if (!displayname.equals(ChatColor.RED + "Explosive Bow")) {
            return;
        }

        // bow ability
        float arrowforce = event.getForce();
        arrow.remove();
        Location location = player.getEyeLocation();
        TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        Vector velocity = location.getDirection().multiply(1.7 * arrowforce);
        tnt.setVelocity(velocity);
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        //checks
        if (player.getInventory().getBoots() == null) {
            return;
        }
        if (player.getInventory().getBoots().getType() != Material.LEATHER_BOOTS) {
            return;
        }
        String displayname = player.getInventory().getBoots().getItemMeta().getDisplayName();
        if (displayname == null) {
            return;
        }

        if (!displayname.equals(ChatColor.RED + "Winged Boots")) {
            return;
        }

        // code
        if (!player.hasPotionEffect(PotionEffectType.JUMP)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15, 0));
        }

    }

    @EventHandler
    public void onUseFireball(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // checks
        if (player.getItemInHand().getType() != Material.FIREBALL) {
            return;
        }

        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        String displayname = player.getItemInHand().getItemMeta().getDisplayName();
        if (displayname == null) {
            return;
        }

        if (!displayname.equals(ChatColor.RED + "Fireball")) {
            return;
        }

        // code to run
        Location location = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2));
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(location, EntityType.FIREBALL);
        fireball.setYield(2);
        fireball.setIsIncendiary(false);
        Vector velocity = location.getDirection().multiply(1);
        fireball.setVelocity(velocity);
        ItemStack fireball_item = player.getItemInHand();
        fireball_item.setAmount(fireball_item.getAmount()-1);
        player.setItemInHand(fireball_item);
        event.setCancelled(true);
    }

    @EventHandler
    public void onUseSword(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        if (player.getItemInHand().getType() != Material.IRON_SWORD) {
            return;
        }

        String displayname = player.getItemInHand().getItemMeta().getDisplayName();
        if (displayname == null) {
            return;
        }

        if (!displayname.equals(ChatColor.RED + "Sword Of Justice")) {
            return;
        }

        // code
        if(Cooldown.isCooling(player.getName(), "Lightning")) {
            Cooldown.coolDurMessage(player, "Lightning");
            return;
        }
        player.getWorld().strikeLightning(player.getLocation().add(player.getLocation().getDirection().multiply(2)));
        Cooldown.add(player.getName(), "Lightning", 2, System.currentTimeMillis());
    }


}
