package skyclash.skyclash.kitscards;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import skyclash.skyclash.main;

import java.util.ArrayList;
import java.util.List;

public class Kits {
    String kit;
    Player player;

    public Kits(String kit, Player player1) {
        this.kit = kit;
        this.player = player1;
    }

    public void GiveKit() {
        if (this.kit.equals("Swordsman")) {
            Swordsman();
        }
        if (this.kit.equals("Beserker")) {
            Beserker();
        }
        if (this.kit.equals("Assassin")) {
            Assassin();
        }
        if (this.kit.equals("Archer")) {
            Archer();
        }
        if (this.kit.equals("Cleric")) {
            Cleric();
        }
        if (this.kit.equals("Frost_Knight")) {
            Frost_Knight();
        }
        if (this.kit.equals("Guardian")) {
            Guardian();
        }
        // if (this.kit.equals("Jumpman")) {
        //     Jumpman();
        // }
        // if (this.kit.equals("Necromancer")) {
        //     Necromancer();
        // }
        // if (this.kit.equals("Treasure_Hunter")) {
        //     Treasure_Hunter();
        // }
        // if (this.kit.equals("Scout")) {
        //     Scout();
        // }

    }

    public void Swordsman() {
        ItemStack item1 = new ItemStack(Material.IRON_SWORD);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.LEATHER_HELMET);
        item1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        this.player.getInventory().setHelmet(item1);
        Potion item = new Potion(PotionType.STRENGTH);
        item.setLevel(1);
        item.setSplash(true);
        item1 = item.toItemStack(2);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Swordsman", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Assassin() {
        ItemStack item1 = new ItemStack(Material.STONE_SWORD);
        item1.addEnchantment(Enchantment.DURABILITY, 1);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.LEATHER_HELMET);
        this.player.getInventory().setHelmet(item1);
        item1 = new ItemStack(Material.ENDER_PEARL, 1);
        this.player.getInventory().addItem(item1);
        Potion item = new Potion(PotionType.INSTANT_DAMAGE);
        item.setLevel(2);
        item.setSplash(true);
        item1 = item.toItemStack(1);
        this.player.getInventory().addItem(item1);
        item = new Potion(PotionType.INVISIBILITY);
        item.setLevel(1);
        item.setSplash(true);
        item1 = item.toItemStack(1);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Assassin", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Beserker() {
        ItemStack item1 = new ItemStack(Material.IRON_AXE);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        item1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.COOKED_BEEF, 1);
        this.player.getInventory().addItem(item1);
        Potion item = new Potion(PotionType.WATER);
        item.setSplash(true);
        item1 = item.toItemStack(1);
        PotionMeta meta = (PotionMeta) item1.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*6, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*6, 0), true);
        meta.setDisplayName(ChatColor.RED+"Beserker Potion");
        List<String> lore = new ArrayList<>();
        lore.add("§6Effect:§e Regeneration 2");
        lore.add("§6Effect:§e Resistance 1");
        lore.add("§6Duration:§e 6 seconds");
        meta.setLore(lore);
        item1.setItemMeta(meta);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Beserker", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Archer() {
        ItemStack item1;
        item1 = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.CHAINMAIL_BOOTS);
        this.player.getInventory().setBoots(item1);
        item1 = new ItemStack(Material.BOW, 1);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.ARROW, 16);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Archer", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Cleric() {
        ItemStack item1;
        item1 = new ItemStack(Material.GOLD_SWORD, 1);
        item1.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
        this.player.getInventory().addItem(item1);
        Potion item = new Potion(PotionType.INSTANT_HEAL); // CHECK POTION;
        item.setLevel(1);
        item.setSplash(true);
        item1 = item.toItemStack(1);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.GOLDEN_APPLE, 1);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Cleric", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 0, true, true), false);
    }
    public void Frost_Knight() {
        ItemStack item1;
        item1 = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) item1.getItemMeta();
        itemmeta.setColor(Color.WHITE);
        item1.setItemMeta(itemmeta);
        this.player.getInventory().setHelmet(item1);
        item1 = new ItemStack(Material.LEATHER_CHESTPLATE);
        itemmeta = (LeatherArmorMeta) item1.getItemMeta();
        itemmeta.setColor(Color.WHITE);
        item1.setItemMeta(itemmeta);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.LEATHER_LEGGINGS);
        itemmeta = (LeatherArmorMeta) item1.getItemMeta();
        itemmeta.setColor(Color.WHITE);
        item1.setItemMeta(itemmeta);
        this.player.getInventory().setLeggings(item1);
        item1 = new ItemStack(Material.DIAMOND_SPADE, 1);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.SNOW_BALL, 12);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.SNOW_BLOCK, 6);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.PUMPKIN, 2);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Frost_Knight", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Guardian() {
        ItemStack item1;
        item1 = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.LEATHER_LEGGINGS);
        this.player.getInventory().setLeggings(item1);
        item1 = new ItemStack(Material.LEATHER_BOOTS);
        this.player.getInventory().setBoots(item1);
        Potion item = new Potion(PotionType.WATER);
        item.setSplash(true);
        item1 = item.toItemStack(1);
        PotionMeta meta = (PotionMeta) item1.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*15, 1), true);
        meta.setDisplayName(ChatColor.RED+"Absorption Potion");
        List<String> lore = new ArrayList<>();
        lore.add("§6Effect:§e Absorption 2");
        lore.add("§6Duration:§e 15 seconds");
        meta.setLore(lore);
        item1.setItemMeta(meta);
        this.player.getInventory().addItem(item1);
        this.player.setMetadata("Guardian", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
}