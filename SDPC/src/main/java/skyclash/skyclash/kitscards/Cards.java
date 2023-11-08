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
import org.json.simple.JSONArray;

import skyclash.skyclash.main;
import skyclash.skyclash.customitems.GiveItem;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;

public class Cards {
    private String card;
    private Player player;

    public static final int CardRent1 = 10;
    public static final int CardRent2 = 16;

    public static final int CardCost1 = 100;
    public static final int CardCost2 = 160;


    public Cards(String Card, Player player) {
        this.card = Card;
        this.player = player;
    }

    public void GiveCard() {
        // give cards
        if (this.card.equals("Creeper")) {
            if (!CanBuy(CardRent2)) {return;}
            ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + "Creeper");
                item.setItemMeta(meta);
            }
            this.player.getInventory().addItem(item);
            new GiveItem().GiveCustomItem(player, "explosive bow");
            player.setMetadata("Creeper", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Bigger Bangs")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Bigger Bangs", new FixedMetadataValue(main.plugin, "card"));
            ItemStack item1 = new ItemStack(Material.FLINT_AND_STEEL);
            this.player.getInventory().addItem(item1);
            item1 = new ItemStack(Material.TNT);
            item1.setAmount(2);
            this.player.getInventory().addItem(item1);
        }
        if (this.card.equals("Damage Potion")) {
            if (!CanBuy(CardRent1)) {return;}
            Potion item = new Potion(PotionType.INSTANT_DAMAGE);
            item.setLevel(2);
            item.setSplash(true);
            ItemStack item1 = item.toItemStack(2);
            this.player.getInventory().addItem(item1);
        }
        if (this.card.equals("Blast Protection")) {
            if (!CanBuy(CardRent2)) {return;}
            for (int i = 0; i < 2; i++) {new GiveItem().GiveCustomItem(player, "fireball");}
            player.setMetadata("Blast Protection", new FixedMetadataValue(main.plugin, "card"));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 0, true, false));
        }
        if (this.card.equals("Elven Archer")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Elven Archer", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Quiver Refill")) {
            if (!CanBuy(CardRent2)) {return;}
            player.setMetadata("Quiver Refill", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Apple Finder")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Apple Finder", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Hit and Run")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Hit and Run", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Pacify")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Pacify", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Pearl Absorption")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Pearl Absorption", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Sugar Rush")) {
            if (!CanBuy(CardRent1)) {return;}
            player.setMetadata("Sugar Rush", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Lifesteal")) {
            if (!CanBuy(CardRent2)) {return;}
            player.setMetadata("Lifesteal", new FixedMetadataValue(main.plugin, "card"));
        }
        if (this.card.equals("Monster Hunter")) {
            if (!CanBuy(CardRent2)) {return;}
            player.setMetadata("Monster Hunter", new FixedMetadataValue(main.plugin, "card"));
        }

    }

    private Boolean CanBuy(int Cost) {
        // coin system
        DataFiles datafiles = new DataFiles(this.player);
        PlayerData data = datafiles.data;
        JSONArray owned = data.Owned;
        if (!owned.contains(this.card)) {
            if (Cost>data.Coins) {
                player.sendMessage(ChatColor.RED+"You do not have enough money to use your card");
                data.Card = "No Card";
                datafiles.SetData(data);
                return false;
            } else {
                data.Coins = data.Coins - Cost;
                datafiles.SetData(data);
            }
        }
        return true;
    }

}
