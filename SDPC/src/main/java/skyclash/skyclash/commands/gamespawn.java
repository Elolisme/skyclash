package skyclash.skyclash.commands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyclash.skyclash.fileIO.MapData;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.kitscards.Abilities;
import skyclash.skyclash.main;

import java.util.ArrayList;
import java.util.logging.Level;

public class gamespawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.plugin.getLogger().log(Level.INFO, "This command is player only");
            return true;
        }

        Player player = (Player) sender;
        Block targetBlock = Abilities.getTargetBlockNoError(player);

        if (args.length == 0 || args.length > 1) {
            player.sendMessage("§cPlease specify the correct arguments\nUse /spawn [add|remove|list]");
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
        String worldname = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.loadFileYML();
        MapData mapdata = maps.data.get(worldname);
        ArrayList<Integer> newloc = new ArrayList<>();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY() + 1);
        newloc.add((int)block.getLocation().getZ());

        if (mapdata.getSpawns().contains(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Location already stored");
            maps.saveFileYML();
            return;
        }

        mapdata.getSpawns().add(newloc);
        maps.data.put(worldname, mapdata);
        maps.saveFileYML();
        player.sendMessage(ChatColor.GREEN+"Added location");
    }

    private void removeSpawn(Player player, Block block) {
        String worldname = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.loadFileYML();
        MapData mapdata = maps.data.get(worldname);
        ArrayList<Integer> newloc = new ArrayList<>();
        newloc.add((int)block.getLocation().getX());
        newloc.add((int)block.getLocation().getY() + 1);
        newloc.add((int)block.getLocation().getZ());

        if (mapdata.getSpawns().remove(newloc)) {
            player.sendMessage(ChatColor.GREEN+"Removed location");
        } else {
            player.sendMessage(ChatColor.RED+"location was not found");
            maps.saveFileYML();
            return;
        }

        maps.data.put(worldname, mapdata);
        maps.saveFileYML();
    }

    private void listSpawns(Player player) {
        String world = player.getWorld().getName();
        Mapsfile maps = new Mapsfile();
        maps.loadFileYML();
        player.sendMessage(ChatColor.GOLD+"<-- Locations for " +ChatColor.YELLOW+world+ChatColor.GOLD+" -->");
        maps.data.get(world).getSpawns().forEach((loc) -> {
            player.sendMessage(ChatColor.YELLOW+ "   "+loc.get(0)+" "+loc.get(1)+" "+loc.get(2));
        });
    }
}
