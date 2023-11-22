package skyclash.skyclash.lobby;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import skyclash.skyclash.Scheduler;
import skyclash.skyclash.main;
import skyclash.skyclash.WorldManager.SCWorlds;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;

public class PlayerControls {
    public static HashMap<Player, ItemStack[]> EnderChestItems = new HashMap<>();

    public void resetPlayer(Player player) {
        player.spigot().respawn();
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setHealth(20);
        player.setFoodLevel(20);
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
        Location spawnloc = new SCWorlds().getLobbySpawnLocation();
        player.teleport(spawnloc);
        Inventories.GiveSCMenu(player);
        Inventories.GiveMapNavItem(player);
        if (EnderChestItems.containsKey(player)) {
            player.getEnderChest().setContents(EnderChestItems.get(player));
            EnderChestItems.remove(player);
        }
    }

    public void GiveLobbyItems(Player player) {
        PlayerStatus playerstatus = new PlayerStatus();
        if (!playerstatus.ContainsName(player)) {
            return;
        }
        if (playerstatus.PlayerEqualsStatus(player, PlayerState.LOBBY) || playerstatus.PlayerEqualsStatus(player, PlayerState.READY)) {
            if (!player.getInventory().contains(Material.NETHER_STAR)) {
                Inventories.GiveSCMenu(player);
            }
            if (!player.getInventory().contains(Material.EMERALD)) {
                Inventories.GiveMapNavItem(player);
            }
        }
        player.setScoreboard(Scheduler.emptyboard);
    }

    public void FirstTimeJoin(Player player) {
        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.data;
        if (!(data.hasJoined)) {
            new PlayerStatus().SetStatus(player, PlayerState.LOBBY);
            player.sendMessage(ChatColor.GREEN+"Welcome to skyclash!\nClick the nether star to access the menu\n\nGLHF (plugin by TitanPlayz)");
            data.hasJoined = true;
            data.kit = "Swordsman";
            data.card = "Damage Potion";
            data.name = player.getName();
        }
        datafiles.SetData(data);
    }
}
