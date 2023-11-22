package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;
import skyclash.skyclash.lobby.LobbyListeners;
import skyclash.skyclash.lobby.PlayerControls;

public class tolobby implements CommandExecutor {
    private PlayerStatus playerstatus = new PlayerStatus();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for arguments
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"Must be a player");
            return true;
        }
        Player player = (Player) sender;

        // If player is still playing game
        if (playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
            player.sendMessage(ChatColor.RED + "You cannot give up!");
            return true;
        }

        new PlayerControls().resetPlayer(player);
        new PlayerControls().toLobby(player);
        new PlayerStatus().SetLobbyOrReady(player);     
        LobbyListeners.CheckStartGame(true);
        return true;
    }
}
