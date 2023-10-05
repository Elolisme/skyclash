package skyclash.skyclash.kitscards;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import skyclash.skyclash.main;
import skyclash.skyclash.customitems.GiveItem;

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
            GiveItem.GiveCustomItem(player, "explosive bow");
            player.setMetadata("Creeper", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Bigger Bangs")) {
            player.setMetadata("Bigger Bangs", new FixedMetadataValue(main.getPlugin(main.class), "card"));
            ItemStack item1 = new ItemStack(Material.FLINT_AND_STEEL);
            this.player.getInventory().addItem(item1);
            item1 = new ItemStack(Material.TNT);
            item1.setAmount(2);
            this.player.getInventory().addItem(item1);
        }
        if (this.card.equals("Damage Potion")) {
            Potion item = new Potion(PotionType.INSTANT_DAMAGE);
            item.setLevel(2);
            item.setSplash(true);
            ItemStack item1 = item.toItemStack(2);
            this.player.getInventory().addItem(item1);
        }

        if (this.card.equals("Blast Protection")) {
            for (int i = 0; i < 2; i++) {GiveItem.GiveCustomItem(player, "fireball");}
            player.setMetadata("Blast Protection", new FixedMetadataValue(main.getPlugin(main.class), "card"));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 0, true, false));
        }
        if (this.card.equals("Elven Archer")) {
            player.setMetadata("Elven Archer", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Quiver Refill")) {
            player.setMetadata("Quiver Refill", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Apple Finder")) {
            player.setMetadata("Apple Finder", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Hit and Run")) {
            player.setMetadata("Hit and Run", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Pacify")) {
            player.setMetadata("Pacify", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Pearl Absorption")) {
            player.setMetadata("Pearl Absorption", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Sugar Rush")) {
            player.setMetadata("Sugar Rush", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Lifesteal")) {
            player.setMetadata("Lifesteal", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }
        if (this.card.equals("Monster Hunter")) {
            player.setMetadata("Monster Hunter", new FixedMetadataValue(main.getPlugin(main.class), "card"));
        }

    }

}
