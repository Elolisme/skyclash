package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import skyclash.skyclash.main;
import skyclash.skyclash.gameManager.EndGame;

public class end_game implements CommandExecutor {
    // use /abort or /endgame
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!main.isGameActive) {
            sender.sendMessage(ChatColor.RED+"No game is currently active");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW+"You have successfully ended the current game");
        new EndGame(true);
        return true;
    }
}

