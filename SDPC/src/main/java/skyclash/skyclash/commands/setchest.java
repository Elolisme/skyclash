package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyclash.skyclash.fileIO.MapData;
import skyclash.skyclash.fileIO.MapsFile;
import skyclash.skyclash.kitscards.Abilities;
import skyclash.skyclash.main;

import java.util.ArrayList;
import java.util.logging.Level;

public class setchest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.plugin.getLogger().log(Level.INFO, "This command is player only");
            return true;
        }

        Player player = (Player) sender;
        Block targetBlock = Abilities.getTargetBlockNoError(player);

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
        MapsFile maps = new MapsFile();
        maps.loadFileYML();
        MapData mapdata = maps.data.get(world);
        ArrayList<Integer> newloc = new ArrayList<>();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY());
        newloc.add((int)block.getLocation().getZ());

        if (mapdata.getChests().contains(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Location already stored");
            maps.saveFileYML();
            return;
        }

        mapdata.getChests().add(newloc);
        maps.data.put(world, mapdata);
        maps.saveFileYML();
        player.sendMessage(ChatColor.GREEN+"Added location");
    }

    private void removeChest(Player player, Block block) {
        String worldname = player.getWorld().getName();
        MapsFile maps = new MapsFile();
        maps.loadFileYML();
        MapData mapdata = maps.data.get(worldname);
        ArrayList<Integer> newloc = new ArrayList<>();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY() + 1);
        newloc.add((int)block.getLocation().getZ());

        if (mapdata.getChests().remove(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Removed location");
        } else {
            player.sendMessage(ChatColor.RED+"location was not found");
            maps.saveFileYML();
            return;
        }

        maps.data.put(worldname, mapdata);
        maps.saveFileYML();
    }

    private void listChests(Player player) {
        String world = player.getWorld().getName();
        MapsFile maps = new MapsFile();
        maps.loadFileYML();
        MapData mapdata = maps.data.get(world);
        player.sendMessage(ChatColor.GOLD+"<-- Locations for " +ChatColor.YELLOW+world+ChatColor.GOLD+" -->");
        mapdata.getChests().forEach((loc) -> {
            Block worldBlock = player.getWorld().getBlockAt(loc.get(0), loc.get(1), loc.get(2));
            if ((worldBlock.getState() instanceof Chest) || worldBlock.getType() == Material.ENDER_CHEST) {
                player.sendMessage(ChatColor.YELLOW+ "   "+loc.get(0)+" "+loc.get(1)+" "+loc.get(2));
            } else {
                player.sendMessage(ChatColor.RED+ "   "+loc.get(0)+" "+loc.get(1)+" "+loc.get(2)+"     No chest at these coords");
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
