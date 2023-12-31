package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import skyclash.skyclash.lobby.VoteMap;

public class adjust_votes implements CommandExecutor {
    private VoteMap votemap = new VoteMap();
    
    /*
     Returns true if command is successful

     use /setvotes <map index> <value>
     <map index> references to the hashmap of all the maps and the order its in, which is also taken from the map.json file
     <value> is any integer to set votes to
    */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for arguments
        if (args.length == 0) { //Sender only typed '/setvotes' and nothing else
            sender.sendMessage(ChatColor.RED + "Please add arguments for map index AND its value\nUse /setvotes <map index> <value>");
            return true;
        }
        if (args.length == 1) { //Sender only typed '/setvotes <map index>' and nothing else
            sender.sendMessage(ChatColor.RED + "Use /setvotes <map index> <value>");
            return true;
        }
        if (args.length > 2) { //Sender typed '/setvotes <map> <value>' + more
            sender.sendMessage(ChatColor.RED + "do not have more than required arguments\nUse /setvotes <map index> <value>");
            return true;
        }
        String map = args[0];
        String value = args[1];

        int mapIndex;
        try {
            mapIndex = Integer.parseInt(map);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED+"Map index must be an integer");
            return true;
        }
        if (mapIndex > votemap.mapSize()) {
            sender.sendMessage(ChatColor.RED+"Map index must be valid");
            return true;
        }

        int voteAmount;
        try {
            voteAmount = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED+"Value must be an integer");
            return true;
        }

        // adjust value
        votemap.setMapValue(mapIndex, voteAmount);
        sender.sendMessage(ChatColor.YELLOW+ "You have set the map with id "+mapIndex+" to have "+voteAmount+" votes");
        return true;
    }
}
