package skyclash.skyclash.chestgen;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LootItem {
    private transient ItemStack itemStack;
    private final Material type;
    private final byte data;
    private final int amount;
    private final byte probability;

    public LootItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.probability = 100;
        if (itemStack != null) {
            this.type = itemStack.getType();
            this.data = 0;
            this.amount = itemStack.getAmount();
        } else {
            this.type = null;
            this.data = 0;
            this.amount = 1;
        }
    }

    public ItemStack getItemStack() {
        if (itemStack != null) {return itemStack;};
        itemStack = new ItemStack(type, amount);

        // custom modifications to certain items based on data
        if (type == Material.INK_SACK) {
            itemStack = new ItemStack(type, amount, (short) data);
        }
        if ((type == Material.IRON_CHESTPLATE || type == Material.IRON_LEGGINGS) && data == 1) {
            itemStack = new ItemStack(type, amount);
            ItemMeta meta = itemStack.getItemMeta();
            
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
            itemStack.setItemMeta(meta);
        }
        if (type == Material.IRON_SWORD && data == 1) {
            itemStack = new ItemStack(type, amount);
            ItemMeta meta = itemStack.getItemMeta();
            
            meta.addEnchant(Enchantment.DAMAGE_ALL, 2, false);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public byte getProbability() {
        return probability;
    }
}
