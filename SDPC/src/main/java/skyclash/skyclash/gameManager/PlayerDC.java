package skyclash.skyclash.gameManager;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.lobby.LobbyListeners;
import skyclash.skyclash.lobby.PlayerControls;
import skyclash.skyclash.lobby.VoteMap;
import skyclash.skyclash.main;

public class PlayerDC implements Listener {
    public PlayerDC(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private PlayerControls playercontrols = new PlayerControls();
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        switch (PlayerStatus.StatusMap.get(player.getName())) {
            case INGAME: ingame(player); break;
            case READY: ready(player); break;
            case SPECTATOR: spec(player); break;
            case LOBBY: ready(player); break;
        }
    }

    private void ingame(Player player) {
        Bukkit.broadcastMessage(ChatColor.RED+player.getName()+" has died due to disconnecting");
        Scheduler.playersLeft--;
        
        // Stats
        new StatsManager().changeStat(player, "deaths", 1);
        new StatsManager().changeStat(player, "Disconnect deaths", 1);
        
        // naturally drop items
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            }
        }
        
        playercontrols.resetPlayer(player);
        playercontrols.toLobby(player);
        PlayerStatus.StatusMap.remove(player.getName());
        VoteMap.playerVote.remove(player.getName());
        EndGame.RemoveTags(player);
        EndGame.CheckGameEnded();
    }

    private void ready(Player player) {
        PlayerStatus.StatusMap.remove(player.getName());
        LobbyListeners.CheckStartGame(false);
    }

    private void spec(Player player) {
        playercontrols.resetPlayer(player);
        playercontrols.toLobby(player);
        PlayerStatus.StatusMap.remove(player.getName());
    }
}