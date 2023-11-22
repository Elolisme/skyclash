package skyclash.skyclash.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.StartGame;
import skyclash.skyclash.gameManager.StatsManager;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;
import skyclash.skyclash.main;
import skyclash.skyclash.cooldowns.Cooldown;

public class LobbyListeners implements Listener {
    // Ignore these
    private PlayerStatus playerstatus = new PlayerStatus();
    private PlayerControls playercontrols = new PlayerControls();
    public LobbyListeners(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Whenever player joins the server
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerstatus.SetLobbyOrReady(player);
        playercontrols.GiveLobbyItems(player);
        playercontrols.FirstTimeJoin(player);
        new StatsManager().changeStat(player, "Joins", 1);
    }

    @EventHandler
    public void onGamemodeSwitch(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        playercontrols.GiveLobbyItems(player);
    }

    // Stop players hitting each other in lobby
    @EventHandler
    public  void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (playerstatus.PlayerEqualsStatus(player, PlayerState.LOBBY) || playerstatus.PlayerEqualsStatus(player, PlayerState.READY)) {
            event.setCancelled(true);
        }
    }

    // Stop items from being dropped
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.data;
        if (event.getItemDrop() == null) {
            return;
        }
        if ((event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) && data.hasJoined && (player.getGameMode() != GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
        if ((event.getItemDrop().getItemStack().getType() == Material.EMERALD) && data.hasJoined && (player.getGameMode() != GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    // Pearl Cooldown
    @EventHandler
    public void onUsePearl(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ENDER_PEARL) {return;}
        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof Player)) {return;}
        Player player = (Player) shooter;

        // If they still have cooldown
        if (player.hasMetadata("nopearlcooldown")) {return;}
        if(Cooldown.isCooling(player.getName(), "Pearl")) {
            player.sendMessage(ChatColor.GRAY + "Pearl Cooldown: " + ChatColor.AQUA + Cooldown.getRemaining(player.getName(), "Pearl") + " Seconds");
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            event.setCancelled(true);
            return;
        }

        // Give cooldown if they dont have it
        Cooldown.add(player.getName(), "Pearl", 2, System.currentTimeMillis());
    }

    // If countUp is true, it will start a game when there are enough players, and stop a game for the opposite
    public static void CheckStartGame(boolean countUp) {
        int people_ready = new PlayerStatus().CountPeopleWithStatus(PlayerState.READY);
        if (countUp && people_ready == 2) {
            new StartGame().AllReady();
        }
        if (!countUp && people_ready == 1) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game cancelled due to insufficient people ready");
            new StartGame().CancelEvents();
        }
    }
}
