package skyclash.skyclash.chestgen;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class LootChest {

    private final String name;
    private boolean shuffle = false;
    private final ArrayList<LootItem> lootItems;

    public LootChest(String name) {
        this.name = name;
        lootItems = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    private ItemStack[] shuffle(ItemStack[] itemStacks) {
        Random random = new Random();

        ItemStack[] temp = new ItemStack[itemStacks.length];
        for (ItemStack itemStack : itemStacks) {
            int randomInt = random.nextInt(itemStacks.length);

            while (temp[randomInt] != null) {
                randomInt = random.nextInt(itemStacks.length);
            }
            temp[randomInt] = itemStack;
        }

        return temp;
    }

    public ItemStack[] generate() {
        ItemStack[] chestItems = new ItemStack[3*9];
        Random random = new Random();

        for (int i = 0; i < lootItems.size(); i++) {
            if (lootItems.get(i).getProbability() >= random.nextInt(100))
                chestItems[i] = lootItems.get(i).getItemStack();
            else
                chestItems[i] = null;
        }

        if (shuffle)
            return shuffle(chestItems);
        else
            return chestItems;
    }
}
