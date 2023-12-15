package skyclash.skyclash.kitscards;

import java.util.ArrayList;

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
        Boolean didBuy = false;
        // give cards
        if (this.card.equals("Creeper")) {
            if (BuyCard(CardRent2)) {didBuy = true;}
            ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + "Creeper");
                item.setItemMeta(meta);
            }
            this.player.getInventory().addItem(item);
            new GiveItem().GiveCustomItem(player, "explosive bow");
        }
        if (this.card.equals("Bigger Bangs")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
            ItemStack item1 = new ItemStack(Material.FLINT_AND_STEEL);
            this.player.getInventory().addItem(item1);
            item1 = new ItemStack(Material.TNT);
            item1.setAmount(2);
            this.player.getInventory().addItem(item1);
        }
        if (this.card.equals("Damage Potion")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
            Potion item = new Potion(PotionType.INSTANT_DAMAGE);
            item.setLevel(2);
            item.setSplash(true);
            ItemStack item1 = item.toItemStack(2);
            this.player.getInventory().addItem(item1);
        }
        if (this.card.equals("Blast Protection")) {
            if (BuyCard(CardRent2)) {didBuy = true;}
            for (int i = 0; i < 2; i++) {new GiveItem().GiveCustomItem(player, "fireball");}
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 0, true, true));
        }
        if (this.card.equals("Elven Archer")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
        }
        if (this.card.equals("Quiver Refill")) {
            if (BuyCard(CardRent2)) {didBuy = true;}
        }
        if (this.card.equals("Apple Finder")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
        }
        if (this.card.equals("Hit and Run")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
        }
        if (this.card.equals("Pacify")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
        }
        if (this.card.equals("Pearl Absorption")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
        }
        if (this.card.equals("Sugar Rush")) {
            if (BuyCard(CardRent1)) {didBuy = true;}
        }
        if (this.card.equals("Lifesteal")) {
            if (BuyCard(CardRent2)) {didBuy = true;}
        }
        if (this.card.equals("Monster Hunter")) {
            if (BuyCard(CardRent2)) {didBuy = true;}
        }

        if (didBuy) {player.setMetadata(this.card, new FixedMetadataValue(main.plugin, "card"));}
    }

    private Boolean BuyCard(int Cost) {
        DataFiles datafiles = new DataFiles(this.player);
        PlayerData data = datafiles.data;
        ArrayList<String> owned = data.owned;
        if (owned == null || this.card == null) {
            return false;
        }

        if (!owned.contains(this.card)) {
            if (Cost>data.coins) {
                player.sendMessage(ChatColor.RED+"You do not have enough money to use your card");
                data.card = "No Card";
                datafiles.SetData(data);
                return false;
            } else {
                data.coins = data.coins - Cost;
                datafiles.SetData(data);
            }
        }
        return true;
    }

}
