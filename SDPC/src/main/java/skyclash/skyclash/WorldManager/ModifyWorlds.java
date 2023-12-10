package skyclash.skyclash.WorldManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.lobby.VoteMap;

public class ModifyWorlds {
    private Multiverse multiverse = new Multiverse();
    private Mapsfile mapsfile;

    public ModifyWorlds(Mapsfile mapsfile) {
        this.mapsfile = mapsfile;
        mapsfile.loadFileYML();
        mapsfile.saveFileYML();
    }

    // Create a new multiverse world with default map settings
    public void createWorld(String name, CommandSender sender) {
        Boolean created = multiverse.createNewSCWorld(name);
        if (!created) {
            sender.sendMessage(ChatColor.RED+"A world with this name already exists");
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.teleport(new Location(multiverse.GetBukkitWorld(name), 0, 65, 0));
        }

        new VoteMap().addMap(new VoteMap().mapSize()+1);
    }

    // change world setting in maps.json
    public void modifySetting(String world, String setting, String value, CommandSender sender) {  
        mapsfile.loadFileYML();
        mapsfile.saveFileYML();      
        if (multiverse.GetWorld(world) == null) {
            sender.sendMessage(ChatColor.RED+"No multiverse world with that name exists");
            return;
        }

        switch (setting) {
            case "setLobby": setDefault(world, sender);break;
            case "setIcon": setIcon(world, value, sender);break;
            case "isVisible": setIgnore(world, value, sender);break;
            default: sender.sendMessage(ChatColor.RED+"That setting does not exist"+ChatColor.GRAY+"\nCurrent settings are:\n setLobby\n setIcon <material>\n isVisible <true/false>");break;  
        }
    }

    // set world which is default lobby
    private void setDefault(String world, CommandSender sender) {
        mapsfile.data.forEach((name, data) -> {
            if (name.equals(world)) {
                data.isdefault = true;
                sender.sendMessage(ChatColor.GREEN+"You have successfully set "+world+" as the lobby");
                multiverse.setSpawnWorld(world);
            } else {
                data.isdefault = false;
            }
        });
        mapsfile.saveFileYML();
    }

    private void setIcon(String world, String value, CommandSender sender) {
        if (value == null) {
            sender.sendMessage(ChatColor.RED+"Please add the block you want as the icon for the world\n/scworld modify <world> setIcon <material>");
            return;
        }
        if (Material.getMaterial(value.toUpperCase()) == null) {
            sender.sendMessage(ChatColor.RED+"Please use a valid material (block/item)\n/scworld modify <world> setIcon <material>");
            return;
        }
        
        mapsfile.data.forEach((name, data) -> {
            if (name.equals(world)) {
                data.icon = value.toUpperCase();
                sender.sendMessage(ChatColor.GREEN+"You have successfully set "+world+" icon to "+value.toUpperCase());
            }
        });
        mapsfile.saveFileYML();
    }

    private void setIgnore(String world, String value, CommandSender sender) {
        Boolean isIgnore;
        if ("true".equals(value)) {
            isIgnore = false;
        } else if ("false".equals(value)) {
            isIgnore = true;
        } else {
            sender.sendMessage(ChatColor.RED+"Value must be boolean");
            return;
        }

        mapsfile.data.forEach((name, data) -> {
            if (name.equals(world)) {
                data.ignore = isIgnore;
                sender.sendMessage(ChatColor.GREEN+"You have successfully changed "+world+" visibility");
            }
        });
        mapsfile.saveFileYML();
    }

    public void listSCWorlds(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW+"Maps:");
        mapsfile.data.forEach((name, value1) -> {
            sender.sendMessage(ChatColor.GREEN+" "+name);
        });
    }
}