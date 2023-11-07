package skyclash.skyclash.customitems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveItem {
    public void GiveCustomItem(Player player, String itemInput) {
        ItemStack item;
        ItemMeta meta;
        String arg_full = itemInput;

        // custom item master spark (definitely not a Touhou reference)
        item = new ItemStack(Material.FIREWORK_CHARGE);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Master Spark");
            List<String> lore = new ArrayList<>();
            lore.add("Use when you need it the most");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        if (arg_full.equals("ms")) {
            player.getInventory().addItem(item);
            return;
        }

        // custom item chicken bow
        item = new ItemStack(Material.BOW);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Jockey Bow");
            List<String> lore2 = new ArrayList<>();
            lore2.add("Shoots jockeys out of arrows");
            meta.setLore(lore2);
            item.setItemMeta(meta);
        }
        short uses = 374;
        item.setDurability(uses);

        if (arg_full.equals("chicken bow")) {
            player.getInventory().addItem(item);
            return;
        }


        // custom item explosive bow
        item = new ItemStack(Material.BOW);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Explosive Bow");
            List<String> lore3 = new ArrayList<>();
            lore3.add("Shoots tnt instead of arrows");
            meta.setLore(lore3);
            item.setItemMeta(meta);
        }
        uses = 379;
        item.setDurability(uses);

        if (arg_full.equals("explosive bow")) {
            player.getInventory().addItem(item);
            return;
        }


        // custom item winged boots
        item = new ItemStack(Material.LEATHER_BOOTS);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Winged Boots");
            List<String> lore4 = new ArrayList<>();
            lore4.add("Redbull gives you wiiiiiings");
            meta.setLore(lore4);
            meta.addEnchant(Enchantment.DURABILITY, 3, false);
            meta.addEnchant(Enchantment.PROTECTION_FALL, 4, false);
            item.setItemMeta(meta);
        }

        if (arg_full.equals("winged boots")) {
            player.getInventory().addItem(item);
            return;
        }


        // custom item fireball
        item = new ItemStack(Material.FIREBALL);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Fireball");
            List<String> lore5 = new ArrayList<>();
            lore5.add("Shoot a fireball in direction facing");
            meta.setLore(lore5);
            item.setItemMeta(meta);
        }

        if (arg_full.equals("fireball")) {
            player.getInventory().addItem(item);
            return;
        }

        // custom item sword of justice
        item = new ItemStack(Material.IRON_SWORD);
        meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Sword Of Justice");
            List<String> lore6 = new ArrayList<>();
            lore6.add("Summons lightning every 2 seconds");
            meta.setLore(lore6);
            item.setItemMeta(meta);
        }

        if (arg_full.equals("sword of justice")) {
            player.getInventory().addItem(item);
            return;
        }
    }
}
