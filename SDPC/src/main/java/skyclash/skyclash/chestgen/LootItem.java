package skyclash.skyclash.chestgen;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootItem {

    //transient: see comment below
    private transient ItemStack itemStack;

    //For exporting with Gson -
    // In the future: fix the exception "class net.minecraft.server.v1_8_R3.EntityItemFrame declares multiple JSON fields named c"
    // when Exporting an Item Stack with Gson -> using a TypeAdapter
    // https://www.spigotmc.org/threads/converting-player-inventory-to-json-using-gson-is-throwing-illegalargumentexception.73522/#post-811962
    private final Material type;
    private final byte data;
    private final int amount;

    private final int lootPoints;
    private final byte probability;

    @SuppressWarnings("deprecation")
    public LootItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.probability = 100;
        this.lootPoints = 0;
        if (itemStack != null) {
            this.type = itemStack.getType();
            this.data = itemStack.getData().getData();
            this.amount = itemStack.getAmount();
        } else {
            this.type = null;
            this.data = 0;
            this.amount = 1;
        }
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemStack() {
        //Temporary solution
        if (itemStack == null)
            itemStack = new ItemStack(type, amount, (short) 0, data);
        //-

        return itemStack;
    }

    public byte getProbability() {
        return probability;
    }

    public int getLootPoints() {
        return lootPoints;
    }

    public Material getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
