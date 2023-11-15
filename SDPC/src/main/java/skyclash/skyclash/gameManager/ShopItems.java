package skyclash.skyclash.gameManager;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import skyclash.skyclash.main;
import skyclash.skyclash.customitems.GiveItem;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.lobby.Inventories;

public class ShopItems {
    private Player player;

    public ShopItems(Player player) {
        this.player = player;
        shopEffects();
    }

    private void shopEffects() {
        DataFiles dataFiles = new DataFiles(player);
        PlayerData data = dataFiles.data;
        ArrayList<String> itemsToRemove = new ArrayList<>();
        for (Object item: data.Owned) {
            if (item.equals("+1 Golden Apple")) {
                gapple(); 
                itemsToRemove.add((String) item);
            }
            if (item.equals("Jockey Bow")) {
                customItem("chicken bow"); 
                itemsToRemove.add((String) item);
            }
            if (item.equals("explosive bow")) {
                customItem((String) item); 
                itemsToRemove.add((String) item);
            }
            if (item.equals("winged boots")) {
                customItem((String) item); 
                itemsToRemove.add((String) item);
            }
            if (item.equals("fireball")) {
                customItem((String) item); 
                itemsToRemove.add((String) item);
            }
            if (item.equals("sword of justice")) {
                customItem((String) item); 
                itemsToRemove.add((String) item);
            }
            if (item.equals("No pearl cooldown")) {
                player.setMetadata("nopearlcooldown", new FixedMetadataValue(main.plugin, "true"));
                itemsToRemove.add((String) item);
            }
            if (item.equals("Resistance Potion")) {
                respotion();
                itemsToRemove.add((String) item);
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

    private void customItem(String item) {
        new GiveItem().GiveCustomItem(player, item);
    }

    private void respotion() {
        this.player.getInventory().addItem(Inventories.CustomPotion(new PotionEffectType[] {PotionEffectType.DAMAGE_RESISTANCE}, new int[] {15}, new int[] {0}));
    }
}
