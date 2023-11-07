package skyclash.skyclash.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyclash.skyclash.customitems.GiveItem;

import java.util.Arrays;

public class giveitem implements CommandExecutor{
    /*
     Returns true if command is successful

     use /giveitem <item>
     <item> is any item in the list below
    */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for arguments
        if (args.length == 0) { //Sender only typed '/giveitem' and nothing else
            sender.sendMessage(ChatColor.RED + "Specify more arguments\nUse /giveitem <player> <item>");
            return true;
        }
        if (args.length == 1) { //Sender only typed '/giveitem <player>' and nothing else
            sender.sendMessage(ChatColor.RED+"Specify an item");
            sender.sendMessage(ChatColor.RED + "Use /giveitem <player> <item>");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online\nUse /giveitem <player> <item>");
            return true;
        }

        String arg_full = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        new GiveItem().GiveCustomItem(player, arg_full);

        return true;
    }
}
