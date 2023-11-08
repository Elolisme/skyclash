package skyclash.skyclash.kitscards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import skyclash.skyclash.main;

public class DrawCard {
    public DrawCard(Player player, String suit, int number) {
        switch (suit) {
            case "Spades": Spades(player, number);break;
            case "Hearts": Hearts(player, number);break;
            case "Clubs": Clubs(player, number);break;
            case "Diamonds": Diamonds(player, number);break;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ClearTempItems(player);
            }
        }.runTaskLater(main.plugin, 20*15);
    }

    private void Spades(Player player, int num) {
        ItemStack item;
        switch (num) {
            case 1:
                item = new ItemStack(Material.BOW);
                item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                item.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
                item.addEnchantment(Enchantment.ARROW_FIRE, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 2:
                player.getInventory().addItem(addLore(new ItemStack(Material.EGG)));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*20, 1, false, true), true);         
            break;
            case 3:
                player.getInventory().addItem(addLore(new ItemStack(Material.SNOW_BALL, 5)));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 15*20, 1, false, true), true);               
            break;
            case 4:
                player.getInventory().addItem(addLore(new ItemStack(Material.BOW, 1)));
                player.getInventory().addItem(addLore(new ItemStack(Material.ARROW, 3)));            
            break;
            case 5:
                player.getInventory().addItem(addLore(new ItemStack(Material.ARROW,5))); 
                player.getInventory().addItem(addLore(new ItemStack(Material.BOW, 1)));
            break;
            case 6:
                player.getInventory().addItem(addLore(new ItemStack(Material.ARROW, 6)));
                player.getInventory().addItem(addLore(new ItemStack(Material.BOW, 1)));
            break;
            case 7:
                player.getInventory().addItem(addLore(new ItemStack(Material.ARROW, 7)));
                player.getInventory().addItem(addLore(new ItemStack(Material.BOW, 1)));  
            break;
            case 8:
                player.getInventory().addItem(addLore(new ItemStack(Material.ARROW, 10)));  
                player.getInventory().addItem(addLore(new ItemStack(Material.BOW, 1)));
            break;
            case 9:
                item = new ItemStack(Material.BOW);
                item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 10:
                item = new ItemStack(Material.BOW);
                item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                item.addEnchantment(Enchantment.ARROW_FIRE, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 11:
                item = new ItemStack(Material.BOW);
                item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 12:
                item = new ItemStack(Material.BOW);
                item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
                item.addEnchantment(Enchantment.ARROW_FIRE, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 13:
                item = new ItemStack(Material.BOW);
                item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                item.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
                item.addEnchantment(Enchantment.ARROW_FIRE, 1);
                player.getInventory().addItem(addLore(item));
            break;
        }
    }

    // TODO:
    @SuppressWarnings("Depreciated")
    private void Hearts(Player player, int num) {
        ItemStack item;
        ItemMeta meta;
        switch (num) {
            case 1:
                item = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
                player.getInventory().addItem(addLore(item));
            break;
            case 2:
                item = new ItemStack(Material.POISONOUS_POTATO);
                player.getInventory().addItem(addLore(item));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*20, 1));
            break;
            case 3:
                item = new ItemStack(Material.ROTTEN_FLESH, 5);
                player.getInventory().addItem(addLore(item));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 15*20, 0));
            break;
            case 4:
                item = new ItemStack(Material.RAW_CHICKEN, 5);
                player.getInventory().addItem(addLore(item));
            break;
            case 5:
                item = new ItemStack(Material.COOKED_FISH, 5);
                player.getInventory().addItem(addLore(item));
            break;
            case 6:
                item = new Potion(PotionType.WATER).toItemStack(1);
                PotionMeta potmeta = (PotionMeta) item.getItemMeta();
                potmeta.addCustomEffect(new PotionEffect(PotionEffectType.SATURATION, 15*20, 2), true);
                potmeta.setDisplayName(ChatColor.WHITE+"Saturation Potion");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY+"Saturation 3 (0:15)");
                potmeta.setLore(lore);
                item.setItemMeta(potmeta);
                player.getInventory().addItem(addLore(item));
            break;
            case 7:
                // saturation
            break;
            case 8:
                // instant health 1 pot
            break;
            case 9:
                // regen
            break;
            case 10:
                // regen, abs
            break;
            case 11:
                // regen, abs 2
            break;
            case 12:
                // regen 2, abs 3
            break;
            case 13:
                // golden apples
            break;
        }
    }

    // TODO:
    private void Clubs(Player player, int num) {
        switch (num) {
            case 1:
                // Sharp V, Fire Aspect, Pearl
            break;
            case 2:
                // Mining fatigue 2 20s, Wooden Stick
            break;
            case 3:
                // Mining Fatigue, Wooden Sword
            break;
            case 4:
                // wood sword
            break;
            case 5:
                // stone sword
            break;
            case 6:
                // sharpenss wood sword
            break;
            case 7:
                // sharpenss stone sword
            break;
            case 8:
                // iron sword
            break;
            case 9:
                // diamond sword
            break;
            case 10:
                // ds knockback 2
            break;
            case 11:
                // ds sharpness
            break;
            case 12:
                // sharp, fire
            break;
            case 13:
                // sharp 3, fire, pearl
            break;
        }
    }

    // TODO:
    private void Diamonds(Player player, int num) {
        switch (num) {
            case 1:
                // resistance 5
            break;
            case 2:
                // leather boots, slowness 2
            break;
            case 3:
                // leather helmet, slowness 1
            break;
            case 4:
                // chain helmet
            break;
            case 5:
                // iron helmet
            break;
            case 6:
                // iron chestplate
            break;
            case 7:
                // resistance 1
            break;
            case 8:
                // diamond leggings
            break;
            case 9:
                // diamond chestplate
            break;
            case 10:
                // resistance 2
            break;
            case 11:
                // Diamond chestplate prot 2
            break;
            case 12:
                // resistance 3
            break;
            case 13:
                // resistance 4
            break;
        }
    }

    private ItemStack addLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ArrayList<String> lore = new ArrayList<String>();
            lore.add("Temporary");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void ClearTempItems(Player player) {
        player.sendMessage(ChatColor.YELLOW+"Your temporary items have been cleared");
        ItemStack[] items = player.getInventory().getContents();
        for (ItemStack item: items) {
            if (item != null && item.hasItemMeta()&& item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals("Temporary")) {
                player.getInventory().remove(item);
            }
        }
        if (player.getItemOnCursor() != null && player.getItemOnCursor().hasItemMeta() && player.getItemOnCursor().getItemMeta().getLore().get(0).equals("Temporary")) {
            player.getItemOnCursor().setType(Material.AIR);
        }
    }
}
