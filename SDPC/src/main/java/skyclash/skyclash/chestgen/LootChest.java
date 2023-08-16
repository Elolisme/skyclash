package skyclash.skyclash.chestgen;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class LootChest {

    private final String name;
    private final LootChestType type = LootChestType.DEFAULT;
    private int lootPoints = 0;
    private boolean shuffle = false;
    private final ArrayList<LootItem> lootItems;

    public LootChest(String name) {
        this.name = name;
        lootItems = new ArrayList<>();

    }

    public void addLootItem(LootItem lootItem) {
        if (lootItem.getType() != null)
            lootItems.add(lootItem);
    }

    public void addLootItem(ItemStack itemStack) {
        addLootItem(new LootItem(itemStack));
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

        switch (type) {
            case DEFAULT:
                for (int i = 0; i < lootItems.size(); i++) {
                    if (lootItems.get(i).getProbability() >= random.nextInt(100))
                        chestItems[i] = lootItems.get(i).getItemStack();
                    else
                        chestItems[i] = null;
                }
                break;
            case LOOT_POINT:
                //ArrayList that represents the inventory of the Chest
                ArrayList<ItemStack> chestItemsList = new ArrayList<>();

                //Determining the minimal LootPoints that can be subtracted
                int minLootPoints = Integer.MAX_VALUE;
                for (LootItem lootItem : lootItems) {
                    if (lootItem.getLootPoints() < minLootPoints)
                        minLootPoints = lootItem.getLootPoints();
                }

                while (lootPoints > 0) {
                    for (LootItem lootItem : lootItems) {
                        //breaking condition for the number of lootPoints
                        if (lootPoints <= 0 || lootPoints - lootItem.getLootPoints() < 0)
                            break;

                        // Chest points / Item points
                        if (lootPoints / lootItem.getLootPoints() >= random.nextInt(lootPoints)) {
                            boolean containsItem = false;

                            //searching for Item in chestItemsList
                            //if the Item is already in the list and the maxAmount is not reached, then the amount is incremented
                            for (ItemStack chestItem : chestItemsList) {
                                if (chestItem.getType() == lootItem.getType()) {
                                    if (chestItem.getAmount() + lootItem.getAmount() <= chestItem.getType().getMaxStackSize()) {
                                        chestItem.setAmount(chestItem.getAmount() + lootItem.getAmount());

                                        lootPoints = lootPoints - lootItem.getLootPoints();
                                        containsItem = true;
                                        break;
                                    }
                                }
                            }

                            //if the List is not 'full' and the item is not in the List then it is added
                            if (chestItemsList.size() < 27 && !containsItem) {
                                chestItemsList.add(lootItem.getItemStack().clone());
                                lootPoints = lootPoints - lootItem.getLootPoints();
                            }

                        }
                    }
                    //additional break for the while loop
                    if (lootPoints <= 0 || lootPoints < minLootPoints)
                        break;
                } //end of while-loop
                //converting the ArrayList to the array that represents the chests inventory
                chestItems = chestItemsList.toArray(chestItems);
                break;
        }

        if (shuffle)
            return shuffle(chestItems);
        else
            return chestItems;
    }
}
