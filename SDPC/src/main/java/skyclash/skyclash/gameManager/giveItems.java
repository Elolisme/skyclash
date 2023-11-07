package skyclash.skyclash.gameManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;

import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;

public class giveItems {
    private Player player;

    public giveItems(Player player) {
        this.player = player;
        shopEffects();
    }

    @SuppressWarnings("all")
    private void shopEffects() {
        DataFiles dataFiles = new DataFiles(player);
        PlayerData data = dataFiles.data;
        JSONArray itemsToRemove = new JSONArray();
        for (Object item: data.Owned) {
            if (item.equals("+1 Golden Apple")) {
                gapple(); 
                itemsToRemove.add(item);
            }
        }

        for (Object item: itemsToRemove) {
            data.Owned.remove(item);
        }
        
        dataFiles.SetData(data);
    }

    private void gapple() {
        this.player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
    }
}
