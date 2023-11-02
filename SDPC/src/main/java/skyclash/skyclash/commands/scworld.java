package skyclash.skyclash.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import skyclash.skyclash.main;
import skyclash.skyclash.fileIO.Mapsfile;

public class scworld implements CommandExecutor {
    @Override
    @SuppressWarnings("unchecked")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for arguments
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
                add(name, sender);
                break;
            
            case "modify":
                if (args.length < 3 || args.length > 5) {
                    sender.sendMessage(ChatColor.RED + "Please use correct number of arguments\nUse /scworld modify <world> <setting> [value]");
                    return true;
                }
                String world = args[1];
                String setting = args[2];
                String value = null;
                if (args.length == 4) {value = args[3];}
                modify(world, setting, value, sender);
                break;
            
            case "list":
                Mapsfile maps1 = new Mapsfile();
                maps1.readFile(false, false);
                sender.sendMessage(ChatColor.YELLOW+"Maps:");
                maps1.jsonObject.forEach((map, value1) -> {
                    String map1 = (String) map;
                    sender.sendMessage(ChatColor.GRAY+" "+map1);
                });
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Please use an appropriate subcommand\nUse /scworld [add | modify | list]");
                break;
        }
        return true;
    }

    public void add(String name, CommandSender sender) {
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        if (worldManager.getMVWorld(name) != null) {
            sender.sendMessage(ChatColor.RED+"A world with this name already exists");
            return;
        }
        worldManager.addWorld(name, Environment.NORMAL, null, WorldType.FLAT, false, "SDPC");
        World newWorld = Bukkit.getWorld(name);
        newWorld.getBlockAt(0, 64, 0).setType(Material.GLASS);
        MultiverseWorld newMVworld = worldManager.getMVWorld(newWorld);
        newMVworld.setSpawnLocation(new Location(newWorld, 0, 65, 0));
        newMVworld.setAllowMonsterSpawn(false);
        newMVworld.setBedRespawn(false);
        newWorld.setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");
        newWorld.setTime(6000);
        newMVworld.setGameMode(GameMode.CREATIVE);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.teleport(new Location(newWorld, 0, 65, 0));
        }

        Mapsfile maps = new Mapsfile();
        maps.readFile(true, true);
        for (int i = 1; i <= maps.get_size(); i++) {
            main.mapVotes.put(i, 0);
        }
    }

    public void modify(String world, String setting, String value, CommandSender sender) {
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        if (worldManager.getUnloadedWorlds().contains(world)) {
            worldManager.loadWorld(world);
        }
        MultiverseWorld MVWorld = worldManager.getMVWorld(world);
        Mapsfile maps1 = new Mapsfile();
        maps1.readFile(true, true);

        if (MVWorld == null) {
            sender.sendMessage(ChatColor.RED+"No multiverse world with that name exists");
            return;
        }
        switch (setting) {
            case "setLobby":
                setDefault(world, sender);
                break;
            case "setIcon":
                setIcon(world, value, sender);

                break;
            case "isVisible":
                setIgnore(world, value, sender);

                break;
            default:
                sender.sendMessage(ChatColor.RED+"That setting does not exist"+ChatColor.GRAY+"\nCurrent settings are:\n setLobby\n setIcon <material>\n isVisible <true/false>");
                break;  
        }
    }

    @SuppressWarnings("unchecked")
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
                main.mvcore.getMVWorldManager().setFirstSpawnWorld(world);
            } else {
                data1.put("isdefault", false);
            }
            newdata.put(map, data1);
        });

        maps.jsonObject = newdata;
        maps.writeFile();
    }

    @SuppressWarnings("unchecked")
    public void setIcon(String world, String value, CommandSender sender) {
        if (value == null) {
            sender.sendMessage(ChatColor.RED+"Please add the block you want as the icon for the world\n/scworld modify <world> setIcon <material>");
        }
        if (Material.getMaterial(value.toUpperCase()) == null) {
            sender.sendMessage(ChatColor.RED+"Please use a valid material (block/item)\n/scworld modify <world> setIcon <material>");
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

    @SuppressWarnings("unchecked")
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
}
