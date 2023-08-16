package skyclash.skyclash.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class giveitem implements CommandExecutor{
    /*
     Returns true if command is successful

     use /giveitem <item>
     <item> is any item in the list below
    */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args1) {
        // Check for arguments
        if (args1.length == 0) { //Sender only typed '/giveitem' and nothing else
            sender.sendMessage(ChatColor.RED + "Specify more arguments\nUse /giveitem <player> <item>");
            return true;
        }
        if (args1.length == 1) { //Sender only typed '/giveitem <player>' and nothing else
            sender.sendMessage(ChatColor.RED+"Specify an item");
            sender.sendMessage(ChatColor.RED + "Use /giveitem <player> <item>");
            return true;
        }
        if (args1.length > 2) { //Sender typed '/giveitem <player> <item>' + more
            sender.sendMessage(ChatColor.RED + "Too many arguments\nUse /giveitem <player> <item>");
            return true;
        }
        AtomicBoolean isOnline = new AtomicBoolean(false);
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        players.forEach((player) -> isOnline.set(player.getName().equals(args1[1])));
        if (isOnline.get()) {
            sender.sendMessage(ChatColor.RED + "Player is not online");
            return true;
        }

        Player player = Bukkit.getPlayer(args1[1]);
        String[] args = Arrays.copyOfRange(args1, 1, args1.length);
        String arg_full = String.join(" ", args);

        // custom item master spark (definitely not a Touhou reference)
        ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Master Spark");
            List<String> lore = new ArrayList<>();
            lore.add("Use when you need it the most");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        if (arg_full.equals("ms")) {
            player.getInventory().addItem(item);
            return true;
        }

        // custom item chicken bow
        ItemStack item2 = new ItemStack(Material.BOW);
        ItemMeta meta2 = item2.getItemMeta();
        if (meta2 != null) {
            meta2.setDisplayName(ChatColor.RED + "Jockey Bow");
            List<String> lore2 = new ArrayList<>();
            lore2.add("Shoots jockeys out of arrows");
            meta2.setLore(lore2);
            item2.setItemMeta(meta2);
        }
        short uses = 374;
        item2.setDurability(uses);

        if (arg_full.equals("chicken bow")) {
            player.getInventory().addItem(item2);
            return true;
        }


        // custom item explosive bow
        ItemStack item3 = new ItemStack(Material.BOW);
        ItemMeta meta3 = item3.getItemMeta();
        if (meta3 != null) {
            meta3.setDisplayName(ChatColor.RED + "Explosive Bow");
            List<String> lore3 = new ArrayList<>();
            lore3.add("Shoots tnt instead of arrows");
            meta3.setLore(lore3);
            item3.setItemMeta(meta3);
        }
        uses = 379;
        item3.setDurability(uses);

        if (arg_full.equals("explosive bow")) {
            player.getInventory().addItem(item3);
            return true;
        }


        // custom item winged boots
        ItemStack item4 = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta4 = item4.getItemMeta();
        if (meta4 != null) {
            meta4.setDisplayName(ChatColor.RED + "Winged Boots");
            List<String> lore4 = new ArrayList<>();
            lore4.add("Redbull gives you wiiiiiings");
            meta4.setLore(lore4);
            meta4.addEnchant(Enchantment.DURABILITY, 3, false);
            meta4.addEnchant(Enchantment.PROTECTION_FALL, 4, false);
            item4.setItemMeta(meta4);
        }

        if (arg_full.equals("winged boots")) {
            player.getInventory().addItem(item4);
            return true;
        }


        // custom item fireball
        ItemStack item5 = new ItemStack(Material.FIREBALL);
        ItemMeta meta5 = item5.getItemMeta();
        if (meta5 != null) {
            meta5.setDisplayName(ChatColor.RED + "Fireball");
            List<String> lore5 = new ArrayList<>();
            lore5.add("Shoot a fireball in direction facing");
            meta5.setLore(lore5);
            item5.setItemMeta(meta5);
        }

        if (arg_full.equals("fireball")) {
            player.getInventory().addItem(item5);
            return true;
        }

        // custom item sword of justice
        ItemStack item6 = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta6 = item6.getItemMeta();
        if (meta6 != null) {
            meta6.setDisplayName(ChatColor.RED + "Sword Of Justice");
            List<String> lore6 = new ArrayList<>();
            lore6.add("Summons lightning every 2 seconds");
            meta6.setLore(lore6);
            item6.setItemMeta(meta6);
        }

        if (arg_full.equals("sword of justice")) {
            player.getInventory().addItem(item6);
            return true;
        }

        return true;
    }
}
