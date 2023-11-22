package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import skyclash.skyclash.WorldManager.ModifyWorlds;
import skyclash.skyclash.fileIO.MapsFile;

public class scworld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ModifyWorlds modify = new ModifyWorlds(new MapsFile());

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please add more arguments\nUse /scworld [add | modify | list] <args>");
            return true;
        }
        String subcommand = args[0];
        
        switch (subcommand) {
            case "add":
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Please use correct number of arguments\nUse /scworld add <world name>");
                    return true;
                }
                String name = args[1];
                modify.createWorld(name, sender);
                break;
            
            case "modify":
                if (args.length < 3 || args.length > 5) {
                    sender.sendMessage(ChatColor.RED + "Please use correct number of arguments\nUse /scworld modify <world> <setting> [value]");
                    return true;
                }
                String value = null;
                if (args.length == 4) {value = args[3];}
                modify.modifySetting(args[1], args[2], value, sender);
                break;
            
            case "list":
                modify.listSCWorlds(sender);
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Please use an appropriate subcommand\nUse /scworld [add | modify | list]");
                break;
        }

        return true;
    }
}
