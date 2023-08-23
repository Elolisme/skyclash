package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.main;

import java.util.Set;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class gamespawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getPlugin(main.class).getLogger().log(Level.INFO, "This command is player only");
            return true;
        }

        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (args.length == 0 ^ args.length > 1) {
            player.sendMessage("§cPlease specify the correct arguments\nUse /gamespawn [add|remove|list]");
            return true;
        }

        switch (args[0]) {
            case "add":
                if (targetBlock.isEmpty()) {
                    player.sendMessage("§cPlease look at a block below spawnpoint while performing this command!");
                    return true;
                }
                addSpawn(player, targetBlock);
                break;
            case "remove":
                if (targetBlock.isEmpty()) {
                    player.sendMessage("§cPlease look at a block below spawnpoint while performing this command!");
                    return true;
                }
                removeSpawn(player, targetBlock);
                break;
            case "list":
                listSpawns(player);
                break;
        }


        return true;
    }

    private void addSpawn(Player player, Block block) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.read_file(false, false);
        JSONObject info = (JSONObject) maps.jsonObject.get(world);
        JSONArray chestsarray = StringToJSON.convert((String) info.get("spawns"));

        JSONArray newloc = new JSONArray();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY() + 1);
        newloc.add((int)block.getLocation().getZ());

        if (chestsarray.contains(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Location already stored");
            maps.write_file();
            return;
        }

        chestsarray.add(newloc);

        String coords = String.valueOf(chestsarray);
        info.remove("spawns");
        info.put("spawns", coords);
        maps.jsonObject.remove(world);
        maps.jsonObject.put(world, info);
        player.sendMessage(ChatColor.GREEN+"Added location");
        maps.write_file();
    }

    private void removeSpawn(Player player, Block block) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.read_file(false, false);
        JSONObject info = (JSONObject) maps.jsonObject.get(world);
        JSONArray chestsarray = StringToJSON.convert((String) info.get("spawns"));

        JSONArray newloc = new JSONArray();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY() + 1);
        newloc.add((int)block.getLocation().getZ());

        if (chestsarray.remove(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Removed location");
        } else {
            player.sendMessage(ChatColor.RED+"location was not found");
        }

        String coords = String.valueOf(chestsarray);
        info.remove("spawns");
        info.put("spawns", coords);
        maps.jsonObject.remove(world);
        maps.jsonObject.put(world, info);

        maps.write_file();
    }

    private void listSpawns(Player player) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.read_file(false, false);
        JSONObject info = (JSONObject) maps.jsonObject.get(world);
        JSONArray chestsarray = StringToJSON.convert((String) info.get("spawns"));
        player.sendMessage(ChatColor.GOLD+"<-- Locations for " +ChatColor.YELLOW+world+ChatColor.GOLD+" -->");
        chestsarray.forEach((loc) -> {
            JSONArray array = (JSONArray) loc;
            player.sendMessage(ChatColor.YELLOW+ "   "+array.get(0)+" "+array.get(1)+" "+array.get(2));
        });
    }
}
