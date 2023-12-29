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

import net.md_5.bungee.api.ChatColor;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;

public class DrawCard {
    public DrawCard(Player player, String suit, int number) {
        switch (suit) {
            case "Spades": Spades(player, number);break;
            case "Hearts": Hearts(player, number);break;
            case "Clubs": Clubs(player, number);break;
            case "Diamonds": Diamonds(player, number);break;
        }
        new Scheduler().scheduleTask(()->ClearTempItems(player), 13*20);
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

    private void Hearts(Player player, int num) {
        ItemStack item;
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
                potmeta.addCustomEffect(new PotionEffect(PotionEffectType.SATURATION, 15*20, 0), true);
                potmeta.setDisplayName(ChatColor.WHITE+"Saturation Potion");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY+"Saturation 3 (0:15)");
                potmeta.setLore(lore);
                item.setItemMeta(potmeta);
                player.getInventory().addItem(addLore(item));
            break;
            case 7:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 15*20, 2));
            break;
            case 8:
                Potion item1 = new Potion(PotionType.INSTANT_HEAL);
                item1.setLevel(1);
                item1.setSplash(true);
                item = item1.toItemStack(1);
                player.getInventory().addItem(item);
            break;
            case 9:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15*20, 1));         
                break;
            case 10:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15*20, 1));         
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 15*20, 1));         
            break;
            case 11:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15*20, 2));         
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 15*20, 2));     
            break;
            case 12:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15*20, 3));         
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 15*20, 3));     
            break;
            case 13:
                item = new ItemStack(Material.GOLDEN_APPLE, 10);
                player.getInventory().addItem(addLore(item));
            break;
        }
    }

    private void Clubs(Player player, int num) {
        ItemStack item;
        switch (num) {
            case 1:
                item = new ItemStack(Material.DIAMOND_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
                item.addEnchantment(Enchantment.FIRE_ASPECT, 1);
                player.getInventory().addItem(addLore(item));
                item = new ItemStack(Material.ENDER_PEARL);
                player.getInventory().addItem(addLore(item));
            break;
            case 2:
                item = new ItemStack(Material.STICK);
                player.getInventory().addItem(addLore(item));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*20, 1));
            break;
            case 3:
                item = new ItemStack(Material.WOOD_SWORD);
                player.getInventory().addItem(addLore(item));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*15, 0));
            break;
            case 4:
                item = new ItemStack(Material.WOOD_SWORD);
                player.getInventory().addItem(addLore(item));
            break;
            case 5:
                item = new ItemStack(Material.STONE_SWORD);
                player.getInventory().addItem(addLore(item));
            break;
            case 6:
                item = new ItemStack(Material.WOOD_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 7:
                item = new ItemStack(Material.STONE_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 8:
                item = new ItemStack(Material.IRON_SWORD);
                player.getInventory().addItem(addLore(item));
            break;
            case 9:
                item = new ItemStack(Material.DIAMOND_SWORD);
                player.getInventory().addItem(addLore(item));
            break;
            case 10:
                item = new ItemStack(Material.DIAMOND_SWORD);
                item.addEnchantment(Enchantment.KNOCKBACK, 2);
                player.getInventory().addItem(addLore(item));
            break;
            case 11:
                item = new ItemStack(Material.DIAMOND_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 12:
                item = new ItemStack(Material.DIAMOND_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                item.addEnchantment(Enchantment.FIRE_ASPECT, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 13:
                item = new ItemStack(Material.DIAMOND_SWORD);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 3);
                item.addEnchantment(Enchantment.FIRE_ASPECT, 1);
                player.getInventory().addItem(addLore(item));
                item = new ItemStack(Material.ENDER_PEARL);
                player.getInventory().addItem(addLore(item));
            break;
        }
    }

    private void Diamonds(Player player, int num) {
        ItemStack item;
        switch (num) {
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*15, 4));
            break;
            case 2:
                item = new ItemStack(Material.LEATHER_BOOTS);
                player.getInventory().addItem(addLore(item));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*20, 1));
            break;
            case 3:
                item = new ItemStack(Material.LEATHER_HELMET);
                player.getInventory().addItem(addLore(item));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*15, 0));
            break;
            case 4:
                item = new ItemStack(Material.CHAINMAIL_HELMET);
                player.getInventory().addItem(addLore(item));
            break;
            case 5:
                item = new ItemStack(Material.IRON_HELMET);
                player.getInventory().addItem(addLore(item));
            break;
            case 6:
                item = new ItemStack(Material.IRON_CHESTPLATE);
                player.getInventory().addItem(addLore(item));
            break;
            case 7:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*15, 0));                
            break;
            case 8:
                item = new ItemStack(Material.DIAMOND_LEGGINGS);
                player.getInventory().addItem(addLore(item));
            break;
            case 9:
                item = new ItemStack(Material.DIAMOND_CHESTPLATE);
                player.getInventory().addItem(addLore(item));
            break;
            case 10:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*15, 1));
            break;
            case 11:
                item = new ItemStack(Material.DIAMOND_CHESTPLATE);
                item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().addItem(addLore(item));
            break;
            case 12:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*15, 2));
            break;
            case 13:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*15, 3));
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
        if (!(new PlayerStatus().PlayerEqualsStatus(player, PlayerState.INGAME))) {return;}

        player.closeInventory();
        player.sendMessage(ChatColor.YELLOW+"Your temporary items have been cleared");
        ItemStack[] items = player.getInventory().getContents();
        for (ItemStack item: items) {
            if (item != null && item.hasItemMeta()&& item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals("Temporary")) {
                player.getInventory().remove(item);
            }
        }
        
        // clearing armour
        ItemStack item = player.getInventory().getHelmet();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals("Temporary")) {
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
        }
        item = player.getInventory().getChestplate();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals("Temporary")) {
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
        }
        item = player.getInventory().getLeggings();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals("Temporary")) {
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
        item = player.getInventory().getBoots();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals("Temporary")) {
            player.getInventory().setBoots(new ItemStack(Material.AIR));
        }
    }
}
