package skyclash.skyclash.chestgen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.EnderChest;
import org.json.simple.JSONArray;
import skyclash.skyclash.fileIO.LootChestIO;

@SuppressWarnings({"UnnecessaryBoxing", "unchecked"})
public class ChestManager {
    JSONArray locList;
    World world;
    LootChest lootChest;
    String chestName1;
    String chestName2;
    public Chest chest;

    public ChestManager(JSONArray locations, World world1, String name) {
        locList = locations;
        world = world1;
        chestName1 = name;
    }
    public ChestManager(JSONArray locations, World world1, String name1, String name2) {
        locList = locations;
        world = world1;
        chestName1 = name1;
        chestName2 = name2;
    }

    public void loadChestLoot() {
        // loop over each location in the location list
        locList.forEach((location) -> {
            JSONArray location1 = (JSONArray) location;
            // get coordinates
            int x = Integer.valueOf(String.valueOf(location1.get(0)));
            int y = Integer.valueOf(String.valueOf(location1.get(1)));
            int z = Integer.valueOf(String.valueOf(location1.get(2)));
            Location loc = new Location(world, x, y, z);
            Block targetBlock = loc.getBlock();
            if (targetBlock.getState() instanceof Chest) {
                chest = (Chest) targetBlock.getState();
                lootChest = LootChestIO.loadChest(chestName1);
                chest.getBlockInventory().setContents(lootChest.generate());
            } else if (targetBlock.getState() instanceof EnderChest) {
//                EnderChest echest = (EnderChest) targetBlock.getState();
//                lootChest = LootChestIO.loadChest(chestName1);
//                chest.getBlockInventory().setContents(lootChest.generate());
            }
            else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"There is no chest at specified location:    "+x+" "+y+" "+z);
            }
        });
    }

    public void createChestLoot() {
        lootChest = new LootChest(chestName1);
        for (ItemStack itemStack : chest.getBlockInventory().getContents()) {
            lootChest.addLootItem(itemStack);
        }
        LootChestIO.saveChest(lootChest);

    }
}
