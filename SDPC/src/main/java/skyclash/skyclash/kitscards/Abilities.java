package skyclash.skyclash.kitscards;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import skyclash.skyclash.Scheduler;
import skyclash.skyclash.main;
import skyclash.skyclash.cooldowns.Cooldown;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;

public class Abilities implements Listener {
    public Abilities(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private PlayerStatus playerstatus = new PlayerStatus();

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
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*5, 1));
    }

    @EventHandler
    public void onLowHealth(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.hasMetadata("Berserker")) {
            return;
        }

        // Berserker
        if (player.getHealth() < 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*6, 1));
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
        if(Cooldown.isCooling(player.getName(), "TrueDamage")) {
            return;
        }


        // Swordsman
        double damage = event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE);
        double extra_damage = damage - event.getFinalDamage();
        float display_damage = (float) Math.round(extra_damage*1000);
        ((LivingEntity)entity).damage(extra_damage);
        Cooldown.add(player.getName(), "TrueDamage", 15, System.currentTimeMillis());
        player.sendMessage(ChatColor.GREEN+"Dealt "+display_damage/1000+" more damage");
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

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) {return;}
        Player player = (Player) e.getEntity();
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {return;}
        if (!(player.hasMetadata("Archer"))) {return;}

        // 50% check
        int n = new Random().nextInt(100);
        if (n<70) {return;}

        double minAngle = 6.283185307179586;
        Entity minEntity = null;
        for (Entity entity : player.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
            Boolean target = false;
            // Just checks - if monster, or if player who is ingame, and if not dead and can be seen
            if (entity instanceof Monster) {target = true;}
            if (entity instanceof Player) {if (playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {target = true;}}
            if (player.hasLineOfSight(entity) && !entity.isDead() && target) {
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
    public void onShootBow2(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {return;}
        Player player = (Player) event.getEntity();
        if (!(player.hasMetadata("Elven Archer"))) {return;}
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 0));
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent event) {
        // Frost Knight
        if (event.getDamager().getType() != EntityType.SNOWBALL) {return;}
        Projectile snowball = (Projectile) event.getDamager();
        if (!(snowball.getShooter() instanceof Player)) {return;}
        Player shooter = (Player) snowball.getShooter();
        if (!(event.getEntity() instanceof Player)) {return;}
        Player player = (Player) event.getEntity();
        
        if (!shooter.hasMetadata("Frost_Knight")) {return;}
        shooter.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 1, false, true), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0, false, true), true);
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
        if (player.getHealth() < 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 1));
        }
    }

    @EventHandler
    public void onJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getVelocity().getY() <= 0) {return;}
        if (!player.hasMetadata("Jumpman")) {return;}
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.SLIME_BLOCK) {return;}
        player.setVelocity(player.getVelocity().add(new Vector(0, 0.8, 0)));
        player.playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, null);
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        Material[] items = {Material.GOLDEN_APPLE, Material.DIAMOND, Material.GOLD_SWORD, Material.TNT, Material.GOLD_AXE};

        Player player = event.getPlayer();
        if (!player.hasMetadata("Treasure_hunter")) {return;}
        if (!(event.getBlock().getState() instanceof Chest)) {return;}
        player.getInventory().addItem(new ItemStack(items[new Random().nextInt(items.length)], 1));
    }

    @EventHandler
    public void onExplosiveHit(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.hasMetadata("Blast Protection")) {
            return;
        }
        if (!(event.getCause() == DamageCause.ENTITY_EXPLOSION || event.getCause() == DamageCause.BLOCK_EXPLOSION)) {
            return;
        }

        // Blast Protection
        double damage = event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE);
        damage = damage * 0.5;
        player.damage(damage);
        player.sendMessage(ChatColor.GREEN+"Reduced damage");
        event.setCancelled(true);
    }

    // quiver refill in clock class
    public static void Every5Seconds() {
        PlayerStatus.StatusMap.forEach((player, status) -> {
            Player plr = Bukkit.getPlayer(player);
            if (plr == null) {return;}
            if ((plr.hasMetadata("Quiver Refill"))) {
                plr.getInventory().addItem(new ItemStack(Material.ARROW, 1));
            }
            if ((plr.hasMetadata("Cleric"))) {
                if (!plr.hasPotionEffect(PotionEffectType.REGENERATION)) {
                    plr.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 0, true, false), true);
                }
            }
        });
    }

    @EventHandler
    public void onChestBreak2(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasMetadata("Apple Finder")) {return;}
        if (!(event.getBlock().getState() instanceof Chest)) {return;}

        // 20% check
        int n = new Random().nextInt(100);
        if (n>40) {return;}

        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
    }

    @EventHandler
    public void onMobHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Monster)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.hasMetadata("Pacify")) {
            return;
        }

        // pacify
        double damage = event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE);
        damage = damage * 0.2;
        player.damage(damage);
        player.sendMessage(ChatColor.GREEN+"Reduced damage");
        event.setCancelled(true);
    }

    @EventHandler
    public void onLandPearl(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (!(event.getEntity().getType() == EntityType.ENDER_PEARL)) {
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (!player.hasMetadata("Pearl Absorption")) {
            return;
        }

        // pearl absorption
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*20, 1), true);
    }

    @EventHandler
    public void onEatGapple(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.GOLDEN_APPLE) {
            return;
        }
        Player player = (Player) event.getPlayer();
        if (!player.hasMetadata("Sugar Rush")) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 1));
    }

    @EventHandler
    public void onAnyHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!player.hasMetadata("Lifesteal")) {
            return;
        }

        // lifesteal
        double damage = event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE);
        damage = damage * 0.2;
        double health = player.getHealth() + damage;
        if (health < player.getMaxHealth()) {
            player.setHealth(health);
        } else {
            player.setHealth(player.getMaxHealth());
        }
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        Monster monster = (Monster) event.getEntity();
        if (!(monster.getKiller() instanceof Player)) {
            return;
        }
        Player killer = monster.getKiller();
        if (!killer.hasMetadata("Monster Hunter")) {
            return;
        }
        ItemStack item = killer.getItemInHand();
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        int level = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
        int level2 = meta.getEnchantLevel(Enchantment.FIRE_ASPECT);
        if (level >= 3 && level2 >= 2) {
            return;
        }

        if (level >= 3) {
            level2 = level2 + 1;
            meta.removeEnchant(Enchantment.FIRE_ASPECT);
            meta.addEnchant(Enchantment.FIRE_ASPECT, level2, true);
            item.setItemMeta(meta);
            killer.setItemInHand(item);
        } else {
            level = level + 1;
            meta.removeEnchant(Enchantment.DAMAGE_ALL);
            meta.addEnchant(Enchantment.DAMAGE_ALL, level, true);
            item.setItemMeta(meta);
            killer.setItemInHand(item);
        }  
    }

    @EventHandler
    public void onHit2(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!player.hasMetadata("Hit and Run")) {
            return;
        }

        // Hit and Run
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3*20, 0, true, false));
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
        if (!(targetBlock.getState() instanceof Chest)) {
            return;
        }
        if (!player.hasMetadata("Scout")) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 1, false, true), true); 

 
    }

    @EventHandler
    public void onOpenChest2(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
        if (!(targetBlock.getState() instanceof Chest)) {
            return;
        }

        Chest chest = (Chest) targetBlock.getState();
        if (chest.getBlockInventory().containsAtLeast(new ItemStack(Material.SLIME_BLOCK), 1)) {
            return;
        }
        if (!player.hasMetadata("Jumpman")) {
            return;
        } 

        int n = new Random().nextInt(100);
        if (n>50) {return;}
        chest.getBlockInventory().addItem(new ItemStack(Material.SLIME_BLOCK, 3));
    }

    @EventHandler
    public void onClickWatch(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME) || !player.hasMetadata("Jester")) {
            return;
        }
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (player.getItemInHand().getType() == Material.AIR || !player.getItemInHand().hasItemMeta()|| !player.getItemInHand().getItemMeta().hasDisplayName() || !player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED+"Deck of Cards")) {
            return;
        }
        if(Cooldown.isCooling(player.getName(), "DrawCard")) {
            player.sendMessage(ChatColor.GRAY + "Card Cooldown: " + ChatColor.AQUA + Cooldown.getRemaining(player.getName(), "DrawCard") + " Seconds");
            return;
        }

        String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"};
        String suit = suits[new Random().nextInt(4)];
        Object number = new Random().nextInt(13)+1;

        new DrawCard(player, suit, (int) number);
        switch ((int)number) {
            case 1:number = "ace";break;
            case 11:number = "jack";break;
            case 12:number = "queen";break;
            case 13:number = "king";break;
        }
        player.sendMessage(ChatColor.YELLOW+"You have drawn a "+number+" of "+suit);        

        Cooldown.add(player.getName(), "DrawCard", 15, System.currentTimeMillis());
    }

    // Prevent dropping temp item
    @EventHandler
    public void onDropTempItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME) || !player.hasMetadata("Jester")) {
            return;
        }
        if (event.getItemDrop().getItemStack().hasItemMeta() && event.getItemDrop().getItemStack().getItemMeta().getLore().get(0).equals("Temporary")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMoveTempItem(InventoryClickEvent event) {
        if (!playerstatus.PlayerEqualsStatus(event.getWhoClicked().getName(), PlayerState.INGAME)) {
            return;
        }
        InventoryType invtype = event.getWhoClicked().getOpenInventory().getTopInventory().getType();
        if (!(invtype == InventoryType.CHEST || invtype == InventoryType.ENDER_CHEST || invtype == InventoryType.WORKBENCH)) {
            return;
        }
        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore() && event.getCurrentItem().getItemMeta().getLore().get(0).equals("Temporary")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onReaperHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME) || !player.hasMetadata("Grim_Reaper")) {
            return;
        }
        
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player hurtPlayer = (Player) event.getEntity();
        if (player.getItemInHand().getType() != Material.SHEARS || !player.getItemInHand().hasItemMeta()|| !player.getItemInHand().getItemMeta().hasDisplayName() || !player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED+"Death Scythe")) {
            return;
        }
        
        // Grim reaper
        player.sendMessage(ChatColor.GREEN+"Marked "+hurtPlayer.getName()+" for doom");
        player.setItemInHand(new ItemStack(Material.AIR));
        player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 0.8f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*30, 1));
        player.damage(player.getHealth() / 2);

        hurtPlayer.sendMessage(ChatColor.RED+"You have beem marked for doom by "+player.getName()+"\nYou must kill them in 1 minute otherwise you will instantly die");
        hurtPlayer.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 0.8f);
        hurtPlayer.setMetadata("Doomed", new FixedMetadataValue(main.plugin, player.getName()));

        new Scheduler().scheduleTask(()->{
            killTaggedPlayers();
        }, 60*20);
    }

    private void killTaggedPlayers() {
        PlayerStatus.StatusMap.forEach((player, state) -> {
            Player player2 = Bukkit.getPlayerExact(player);
            if (new PlayerStatus().PlayerEqualsStatus(player, PlayerState.INGAME) && player2.hasMetadata("Doomed")) {
                player2.damage(player2.getHealth());
            }
        });
    }

    @EventHandler
    public void onReaperDied(PlayerDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player reaper = (Player) event.getEntity();
        if (!playerstatus.PlayerEqualsStatus(reaper, PlayerState.INGAME) || !reaper.hasMetadata("Grim_Reaper")) {
            return;
        }
        
        PlayerStatus.StatusMap.forEach((name, state) -> {
            Player markedPlayer = Bukkit.getPlayerExact(name);
            if (new PlayerStatus().PlayerEqualsStatus(name, PlayerState.INGAME) && markedPlayer.hasMetadata("Doomed") && markedPlayer.getMetadata("Doomed").get(0) == new FixedMetadataValue(main.plugin, reaper.getName())) {
                markedPlayer.removeMetadata("Doomed", main.plugin);
                markedPlayer.sendMessage("You have successfully removed your mark");
            }
        });
    }
}