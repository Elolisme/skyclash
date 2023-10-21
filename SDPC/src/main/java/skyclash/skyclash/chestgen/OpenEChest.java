package skyclash.skyclash.chestgen;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import skyclash.skyclash.main;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class OpenEChest implements Listener {
    public static HashMap<Player, ItemStack[]> EnderChestItems = new HashMap<>();

    public OpenEChest(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.get(player.getName()).equals("ingame")) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {return;}


        ItemStack[] Echestitems = player.getEnderChest().getContents();
        if (!EnderChestItems.containsKey(player)) {
            EnderChestItems.put(player, Echestitems);
            player.getEnderChest().clear();
        }
        

        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (targetBlock.getState() instanceof Chest) {
            if (player.hasMetadata("Scout")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 1, false, true), true); 
                return;
            }
            Chest chest = (Chest) targetBlock.getState();
            if (chest.getBlockInventory().containsAtLeast(new ItemStack(Material.SLIME_BLOCK), 1)) {return;}
            if (!player.hasMetadata("Jumpman")) {return;}
            int n = new Random().nextInt(100);
            if (n>50) {return;}
            chest.getBlockInventory().addItem(new ItemStack(Material.SLIME_BLOCK, 3));
            return;
        }

        if (targetBlock.getState().getType() != Material.ENDER_CHEST) {
            return;
        }

        Location chestloc = targetBlock.getLocation().add(new Vector(0, 100, 0));
        if (!(chestloc.getBlock().getState() instanceof Chest)) {
            return;
        }
        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        Chest chest = (Chest) chestloc.getBlock().getState();
        player.openInventory(chest.getInventory());
    }
}