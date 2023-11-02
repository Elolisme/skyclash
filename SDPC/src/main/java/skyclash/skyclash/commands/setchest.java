package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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
public class setchest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getPlugin(main.class).getLogger().log(Level.INFO, "This command is player only");
            return true;
        }

        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (args.length == 0 || args.length > 3) {
            player.sendMessage("§cPlease specify the correct arguments\nUse /chests [add|remove|list|scan]");
            return true;
        }

        switch (args[0]) {
            case "add":
                if (!(targetBlock.getState() instanceof Chest) && targetBlock.getType() != Material.ENDER_CHEST) {
                    player.sendMessage("§cPlease look at a chest while performing this command!");
                    return true;
                }
                addChest(player, targetBlock);
                break;
            case "remove":
                if (!(targetBlock.getState() instanceof Chest) && targetBlock.getType() != Material.ENDER_CHEST) {
                    player.sendMessage("§cPlease look at a chest while performing this command!");
                    return true;
                }
                removeChest(player, targetBlock);
                break;
            case "list":listChests(player);break;
            case "scan":
                if (args.length < 3) {
                    player.sendMessage("§cPlease specify the correct arguments\nUse /chests scan <radius> <add found chests (true/false)>");
                    return true;
                }
                int value1;
                try {
                    value1 = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED+"Radius must be an integer");
                    return true;
                }
                String value2 = args[2];
                Boolean isAppending;
                if ("true".equals(value2)) {
                    isAppending = true;
                } else if ("false".equals(value2)) {
                    isAppending = false;
                } else {
                    sender.sendMessage(ChatColor.RED+"Decision must be boolean");
                    return true;
                }

                scanChests(player, value1, isAppending);
                break;
        }


        return true;
    }

    private void addChest(Player player, Block block) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        JSONObject info = (JSONObject) maps.jsonObject.get(world);
        JSONArray chestsarray = StringToJSON.convert((String) info.get("chests"));

        JSONArray newloc = new JSONArray();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY());
        newloc.add((int)block.getLocation().getZ());

        if (chestsarray.contains(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Location already stored");
            maps.writeFile();
            return;
        }

        chestsarray.add(newloc);

        String coords = String.valueOf(chestsarray);
        info.remove("chests");
        info.put("chests", coords);
        maps.jsonObject.remove(world);
        maps.jsonObject.put(world, info);
        player.sendMessage(ChatColor.GREEN+"Added location");
        maps.writeFile();
    }

    private void removeChest(Player player, Block block) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        JSONObject info = (JSONObject) maps.jsonObject.get(world);
        JSONArray chestsarray = StringToJSON.convert((String) info.get("chests"));

        JSONArray newloc = new JSONArray();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY());
        newloc.add((int)block.getLocation().getZ());

        if (chestsarray.remove(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Removed location");
        } else {
            player.sendMessage(ChatColor.RED+"location was not found");
        }

        String coords = String.valueOf(chestsarray);
        info.remove("chests");
        info.put("chests", coords);
        maps.jsonObject.remove(world);
        maps.jsonObject.put(world, info);

        maps.writeFile();
    }

    private void listChests(Player player) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        JSONObject info = (JSONObject) maps.jsonObject.get(world);
        JSONArray chestsarray = StringToJSON.convert((String) info.get("chests"));
        player.sendMessage(ChatColor.GOLD+"<-- Locations for " +ChatColor.YELLOW+world+ChatColor.GOLD+" -->");
        chestsarray.forEach((loc) -> {
            JSONArray array = (JSONArray) loc;
            int x = Integer.valueOf(String.valueOf(array.get(0)));
            int y = Integer.valueOf(String.valueOf(array.get(1)));
            int z = Integer.valueOf(String.valueOf(array.get(2)));
            Block worldBlock = player.getWorld().getBlockAt(x, y, z);
            if ((worldBlock.getState() instanceof Chest) || worldBlock.getType() == Material.ENDER_CHEST) {
                player.sendMessage(ChatColor.YELLOW+ "   "+array.get(0)+" "+array.get(1)+" "+array.get(2));
            } else {
                player.sendMessage(ChatColor.RED+ "   "+array.get(0)+" "+array.get(1)+" "+array.get(2)+"     No chest at these coords");
            }
        });
    }

    private void scanChests(Player player, int radius, Boolean appending) {
        String world = player.getWorld().getName();
        player.sendMessage(ChatColor.GOLD+"<-- Potential chests in " +ChatColor.YELLOW+world+ChatColor.GOLD+" -->");
        if (radius > 30) {player.sendMessage(ChatColor.RED+"/chests scan may cause significant lag...");}
        for (int i = 0; i < radius*2; i++) {
            for (int j = 0; j < radius*2; j++) {
                for (int k = 0; k < radius*2; k++) {
                    Block currentBlock = player.getLocation().subtract(radius, radius, radius).add(i, j, k).getBlock();
                    int x = (int)currentBlock.getX();
                    int y = (int)currentBlock.getY();
                    int z = (int)currentBlock.getZ();
                    if ((player.getLocation().subtract(radius, radius, radius).add(i, j, k).getBlock().getState() instanceof Chest) || player.getLocation().subtract(radius, radius, radius).add(i, j, k).getBlock().getType() == Material.ENDER_CHEST) {
                        player.sendMessage(ChatColor.YELLOW+ "   Chest found at: "+x+" "+y+" "+z);
                        if (appending) {addChest(player, currentBlock);}
                    }
                } 
            }
        }
        player.sendMessage(ChatColor.GREEN+"<-- Finished Scan -->");
    }
}
