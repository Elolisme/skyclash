package skyclash.skyclash.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

    public Kits(String kit, Player player) {
        this.kit = kit;
        this.player = player;
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

    }

    public void Swordsman() {
        ItemStack item1 = new ItemStack(Material.DIAMOND_SWORD);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.IRON_HELMET);
        item1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        this.player.getInventory().setHelmet(item1);
        item1 = new ItemStack(Material.LEATHER_CHESTPLATE);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.LEATHER_LEGGINGS);
        this.player.getInventory().setLeggings(item1);
        item1 = new ItemStack(Material.LEATHER_BOOTS);
        this.player.getInventory().setBoots(item1);
        Potion item = new Potion(PotionType.STRENGTH);
        item.setLevel(2);
        item.setSplash(true);
        item1 = item.toItemStack(3);
        this.player.getInventory().addItem(item1);
        player.setMetadata("Swordsman", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Assassin() {
        ItemStack item1 = new ItemStack(Material.DIAMOND_SWORD);
        item1.addEnchantment(Enchantment.DURABILITY, 1);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.LEATHER_HELMET);
        this.player.getInventory().setHelmet(item1);
        item1 = new ItemStack(Material.LEATHER_CHESTPLATE);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.LEATHER_LEGGINGS);
        this.player.getInventory().setLeggings(item1);
        item1 = new ItemStack(Material.LEATHER_BOOTS);
        this.player.getInventory().setBoots(item1);
        item1 = new ItemStack(Material.ENDER_PEARL, 3);
        this.player.getInventory().addItem(item1);
        Potion item = new Potion(PotionType.INSTANT_DAMAGE);
        item.setLevel(2);
        item.setSplash(true);
        item1 = item.toItemStack(2);
        this.player.getInventory().addItem(item1);
        item = new Potion(PotionType.INVISIBILITY);
        item.setLevel(1);
        item.setSplash(true);
        item1 = item.toItemStack(2);
        this.player.getInventory().addItem(item1);
        player.setMetadata("Assassin", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
    public void Beserker() {
        ItemStack item1 = new ItemStack(Material.DIAMOND_AXE);
        this.player.getInventory().addItem(item1);
        item1 = new ItemStack(Material.IRON_HELMET);
        item1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        this.player.getInventory().setHelmet(item1);
        item1 = new ItemStack(Material.IRON_CHESTPLATE);
        item1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        this.player.getInventory().setChestplate(item1);
        item1 = new ItemStack(Material.COOKED_BEEF);
        this.player.getInventory().addItem(item1);
        Potion item = new Potion(PotionType.WATER);
        item.setSplash(true);
        item1 = item.toItemStack(3);
        PotionMeta meta = (PotionMeta) item1.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*60, 2), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60, 1), true);
        meta.setDisplayName(ChatColor.RED+"Beserker Potion");
        List<String> lore = new ArrayList<>();
        lore.add("§6Effect:§e Regeneration 2");
        lore.add("§6Effect:§e Resistance 1");
        lore.add("§6Duration:§e 1 minute");
        meta.setLore(lore);
        item1.setItemMeta(meta);
        this.player.getInventory().addItem(item1);
        player.setMetadata("Beserker", new FixedMetadataValue(main.getPlugin(main.class), "kit"));
    }
}