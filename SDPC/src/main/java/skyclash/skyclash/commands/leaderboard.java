package skyclash.skyclash.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;

public class leaderboard implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for arguments
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please add arguments for the stat, or ? for a list of all stats\nUse /leaderboard <stat>");
            return true;
        }
        String stat = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        
        String PlayerName = "null";
        if (sender instanceof Player) {PlayerName = sender.getName();}

        if (stat.equals("?")) {
            sender.sendMessage(ChatColor.YELLOW+"The current stats are:\n- kills\n- deaths\n- wins\n- Games\n- Joins\n- Disconnect deaths\n- Void deaths\n- 30s Deaths\n- coins");
            return true;
        }

        HashMap<String, Integer> statList = PlayersWithStat(stat);

        if (statList.size() == 0) {
            sender.sendMessage("No players have this stat");
            return true;
        }

        sender.sendMessage(ChatColor.DARK_RED+">-----"+stat+"-----<");
        ArrayList<Integer> sortStrings = new ArrayList<>();
        statList.forEach((name, value) -> {sortStrings.add(value);});
        List<Integer> sortStrings2 = sortStrings.stream().distinct().collect(Collectors.toList());
        Collections.sort(sortStrings2);
        Collections.reverse(sortStrings2);
        int count = 1;
        for (Integer value: sortStrings2) {
            for (Map.Entry<String, Integer> entry: statList.entrySet()) {
                String name = entry.getKey();
                if (value.equals(entry.getValue())) {
                    if (count <= 10) {
                        if (name.equals(PlayerName)) {
                            sender.sendMessage(ChatColor.GOLD+""+count+". "+name+ChatColor.YELLOW+" ("+value+")");
                        } else {
                            sender.sendMessage(ChatColor.RED+""+count+". "+name+ChatColor.YELLOW+" ("+value+")");
                        }
                    } else {
                        if (name.equals(PlayerName)) {
                            sender.sendMessage(ChatColor.GOLD+""+count+". "+name+ChatColor.YELLOW+" ("+value+")");
                        }
                    }
                }
            }
            count++;
        }
        return true;
    }

    private int getStat(String player, String stat) {
        DataFiles dfiles = new DataFiles(player);
        PlayerData pdata = dfiles.data;
        if (stat.equals("coins")) {
            return pdata.coins;
        }
        else if (pdata.stats.containsKey(stat)) {
            return pdata.stats.get(stat);
        }
        else {
            return Integer.MIN_VALUE;
        }
    }

    private HashMap<String, Integer> PlayersWithStat(String stat) {
        HashMap<String, Integer> statList = new HashMap<>();
        File dir = new File("plugins"+File.separator+"SDPC"+File.separator+"players");
        File[] dirList = dir.listFiles();
        for (File file: dirList){
            if (file.isFile()) {
                String nameList = file.getName();
                String name = nameList.substring(0,nameList.indexOf("."));
                int value = getStat(name, stat);
                if (value != Integer.MIN_VALUE) {
                    statList.put(name, value);
                } 
            }
        }
        return statList;
    }
}
