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
import org.bukkit.util.Vector;
import skyclash.skyclash.main;

import java.util.Set;

public class OpenEChest implements Listener {
    public OpenEChest(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onOpenEChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.get(player.getName()).equals("ingame")) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {return;}

        ItemStack[] Echestitems = player.getEnderChest().getContents();
        if (!main.EnderChestItems.containsKey(player)) {
            main.EnderChestItems.put(player, Echestitems);
            player.getEnderChest().clear();
        }
        
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (targetBlock.getState() instanceof Chest) {
            return;
        }

        if (targetBlock.getState().getType() != Material.ENDER_CHEST) {
            return;
        }

        Location chestloc = targetBlock.getLocation().add(new Vector(0, 100, 0));
        if (!(chestloc.getBlock().getState() instanceof Chest)) {
            return;
        }

        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        Chest chest = (Chest) chestloc.getBlock().getState();
        player.openInventory(chest.getInventory());
        event.setCancelled(true);
    }
}