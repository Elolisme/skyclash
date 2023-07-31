package skyclash.skyclash.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import skyclash.skyclash.main;

public class Cards {
    String card;
    Player player;

    public Cards(String kit, Player player) {
        this.card = kit;
        this.player = player;
    }

    public void GiveCard() {
        if (this.card.equals("Creeper")) {
            ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + "Creeper");
                item.setItemMeta(meta);
            }
            this.player.getInventory().addItem(item);


        }
        if (this.card.equals("Bigger Bangs")) {
            player.setMetadata("Bigger Bangs", new FixedMetadataValue(main.getPlugin(main.class), "card"));

        }
        if (this.card.equals("Damage Potion")) {
            Potion item = new Potion(PotionType.INSTANT_DAMAGE);
            item.setLevel(2);
            item.setSplash(true);
            ItemStack item1 = item.toItemStack(2);
            this.player.getInventory().addItem(item1);

        }

    }

}
