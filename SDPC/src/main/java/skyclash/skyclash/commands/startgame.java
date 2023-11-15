package skyclash.skyclash.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import skyclash.skyclash.main;
import skyclash.skyclash.gameManager.StartGame;

public class startgame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (main.isGameActive) {
            sender.sendMessage("A game is already active");
            return true;
        }
        new StartGame().AllReadyCommand();
        return true;
    }
}
