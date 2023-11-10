package skyclash.skyclash.WorldManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.lobby.VoteMap;

@SuppressWarnings("unchecked")
public class ModifyWorlds {
    private Multiverse multiverse = new Multiverse();

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

        Mapsfile maps = new Mapsfile();
        maps.readFile(true, true);
        new VoteMap().addMap(new VoteMap().mapSize()+1);
    }

    public void modifySetting(String world, String setting, String value, CommandSender sender) {        
        Mapsfile maps1 = new Mapsfile();
        maps1.readFile(true, true);

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

    public void setDefault(String world, CommandSender sender) {
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        JSONObject newdata = new JSONObject();
        maps.jsonObject.forEach((map, data) -> {
            String mapName = (String) map;
            JSONObject data1 = (JSONObject) data;
            if (mapName.equals(world)) {
                data1.put("isdefault", true);
                sender.sendMessage(ChatColor.GREEN+"You have successfully set "+world+" as the lobby");
                multiverse.setSpawnWorld(world);
            } else {
                data1.put("isdefault", false);
            }
            newdata.put(map, data1);
        });

        maps.jsonObject = newdata;
        maps.writeFile();
    }

    public void setIcon(String world, String value, CommandSender sender) {
        if (value == null) {
            sender.sendMessage(ChatColor.RED+"Please add the block you want as the icon for the world\n/scworld modify <world> setIcon <material>");
            return;
        }
        if (Material.getMaterial(value.toUpperCase()) == null) {
            sender.sendMessage(ChatColor.RED+"Please use a valid material (block/item)\n/scworld modify <world> setIcon <material>");
            return;
        }
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        JSONObject newdata = new JSONObject();
        maps.jsonObject.forEach((map, data) -> {
            String mapName = (String) map;
            JSONObject data1 = (JSONObject) data;
            if (mapName.equals(world)) {
                data1.put("icon", value.toUpperCase());
                sender.sendMessage(ChatColor.GREEN+"You have successfully set "+world+" icon to "+value.toUpperCase());
            }
            newdata.put(map, data1);
        });
        maps.jsonObject = newdata;
        maps.writeFile();
    }

    public void setIgnore(String world, String value, CommandSender sender) {
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        Boolean isIgnore;
        if ("true".equals(value)) {
            isIgnore = false;
        } else if ("false".equals(value)) {
            isIgnore = true;
        } else {
            sender.sendMessage(ChatColor.RED+"Value must be boolean");
            return;
        }

        JSONObject newdata = new JSONObject();
        maps.jsonObject.forEach((map, data) -> {
            String mapName = (String) map;
            JSONObject data1 = (JSONObject) data;
            if (mapName.equals(world)) {
                data1.put("ignore", isIgnore);
                sender.sendMessage(ChatColor.GREEN+"You have successfully changed "+world+" visibility");
            }
            newdata.put(map, data1);
        });

        maps.jsonObject = newdata;
        maps.writeFile();
    }

    public void listSCWorlds(CommandSender sender) {
        Mapsfile maps1 = new Mapsfile();
        maps1.readFile(false, false);
        sender.sendMessage(ChatColor.YELLOW+"Maps:");
        maps1.jsonObject.forEach((map, value1) -> {
            String map1 = (String) map;
            sender.sendMessage(ChatColor.GRAY+" "+map1);
        });
    }
}
