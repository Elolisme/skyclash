package skyclash.skyclash.chestgen;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;
import skyclash.skyclash.fileIO.LootChestIO;

public class ChestManager {
    private ArrayList<ArrayList<Integer>> locList;
    private World world;
    private LootChest lootChest;
    private String chestName1 = "spawn";
    private String chestName2 = "mid";
    private Chest chest;

    public ChestManager(ArrayList<ArrayList<Integer>> locations, World world1) {
        locList = locations;
        world = world1;
        loadChestLoot();
    }

    private void loadChestLoot() {
        locList.forEach((location) -> {
            Location coords = new Location(world, location.get(0), location.get(1), location.get(2));
            Block targetBlock = coords.getBlock();
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
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"There is no chest at specified location:       "+location.get(0)+" "+location.get(1)+" "+location.get(2));
            }
        });
    }
}
