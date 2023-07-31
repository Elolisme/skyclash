package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyclash.skyclash.main;

public class adjust_votes implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Must be Player to send command");
            return true;
        }
        Player player = (Player) sender;

        // Check for arguments
        if (args.length == 0) { //Sender only typed '/setvotes' and nothing else
            sender.sendMessage(ChatColor.RED + "Use /setvotes <map name> <value>");
            return true;
        }
        if (args.length == 1) { //Sender only typed '/setvotes <map>' and nothing else
            sender.sendMessage(ChatColor.RED+"Fucking idiot");
            sender.sendMessage(ChatColor.RED + "Use /setvotes <map name> <value>");
            return true;
        }
        if (args.length > 2) { //Sender typed '/setvotes <map> <value>' + more
            sender.sendMessage(ChatColor.RED + "Use /setvotes <map name> <value>");
            return true;
        }
        String map = args[0];
        String value = args[1];
        int value1;
        try {
            value1 = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED+"Value must be an integer");
            return true;
        }
        if (!main.mapselection.containsKey(map)) {
            player.sendMessage(ChatColor.RED+"Map name must be valid");
            return true;
        }

        // adjust value
        main.mapselection.put(map, value1);




        return true;
    }
}