package skyclash.skyclash.lobby;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import skyclash.skyclash.Scheduler;
import skyclash.skyclash.main;
import skyclash.skyclash.WorldManager.SCWorlds;

public class PlayerControls {
    public void resetPlayer(Player player) {
        player.spigot().respawn();
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setHealth(20);
        player.setSaturation(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setLevel(0);
        player.setExp(0);

        if (player.hasMetadata("NoMovement")) {
            player.removeMetadata("NoMovement", main.plugin);
        }
    }

    public void toLobby(Player player) {
        player.setScoreboard(Scheduler.emptyboard);
        Location spawnloc = new SCWorlds().getLobbyLocation();
        player.teleport(spawnloc);
        LobbyControls.GiveItem(player);
        LobbyControls.GiveMapNavItem(player);
        if (main.EnderChestItems.containsKey(player)) {
            player.getEnderChest().setContents(main.EnderChestItems.get(player));
            main.EnderChestItems.remove(player);
        }
    }
}
