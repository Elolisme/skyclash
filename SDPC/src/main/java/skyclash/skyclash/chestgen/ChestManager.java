package skyclash.skyclash.chestgen;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import skyclash.skyclash.fileIO.LootChestIO;

@SuppressWarnings({"UnnecessaryBoxing", "unchecked"})
public class ChestManager {
    private JSONArray locList;
    private World world;
    private LootChest lootChest;
    private String chestName1;
    private String chestName2;
    private Chest chest;

    public ChestManager(JSONArray locations, World world1, String name1, String name2, Boolean loadLoot) {
        locList = locations;
        world = world1;
        chestName1 = name1;
        chestName2 = name2;
        if (loadLoot) {loadChestLoot();}
    }

    private void loadChestLoot() {
        locList.forEach((location) -> {
            JSONArray location1 = (JSONArray) location;
            int x = Integer.valueOf(String.valueOf(location1.get(0)));
            int y = Integer.valueOf(String.valueOf(location1.get(1)));
            int z = Integer.valueOf(String.valueOf(location1.get(2)));
            Location loc = new Location(world, x, y, z);
            Block targetBlock = loc.getBlock();
            if (targetBlock.getState() instanceof Chest) {
                chest = (Chest) targetBlock.getState();
                lootChest = LootChestIO.loadChest(chestName1);
                chest.getBlockInventory().setContents(lootChest.generate());
            } else if (targetBlock.getType() == Material.ENDER_CHEST) {
                Location newloc = targetBlock.getLocation().add(new Vector(0, 100, 0));
                newloc.getBlock().setType(Material.CHEST);
                chest = (Chest) newloc.getBlock().getState();
                lootChest = LootChestIO.loadChest(chestName2);
                chest.getBlockInventory().setContents(lootChest.generate());
            }
            else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"There is no chest at specified location:    "+x+" "+y+" "+z);
            }
        });
    }
}
