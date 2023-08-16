package skyclash.skyclash.lobbymenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyclash.skyclash.Clock;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.kitscards.PlayerData;
import skyclash.skyclash.main;

import java.util.ArrayList;
import java.util.List;

public class LobbyControls implements Listener {
    public LobbyControls(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals("agentlab")) {
            player.sendMessage(ChatColor.DARK_PURPLE + "Check dc, dont build in agentlab world");
        }

        if (!main.playerStatus.containsKey(player.getName())) {
            main.playerStatus.put(player.getName(), "lobby");
        }

        // if they in lobby
        if (main.playerStatus.get(player.getName()).equals("lobby")  && player.getGameMode() != GameMode.CREATIVE) {
            if (!player.getInventory().contains(Material.NETHER_STAR)) {
                GiveItem(player);
                player.setScoreboard(Clock.emptyboard);
            }
        }
        // if they have joined
        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.LoadData();
        if (!(data.hasJoined)) {
            main.playerStatus.put(player.getName(), "lobby");
            player.sendMessage(ChatColor.GREEN+"Welcome to skyclash!\nClick the nether star to access the menu\n\nGLHF (plugin by TitanPlayz)");
            data.hasJoined = true;
            data.Name = player.getName();
        }
        datafiles.SetData(data);

        if (main.playerStatus.get(player.getName()).equals("lobby") && player.getGameMode() != GameMode.CREATIVE) {
            GiveItem(player);
        }
    }

    @EventHandler
    public void onGamemodeSwitch(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.containsKey(player.getName())) {
            return;
        }
        if (main.playerStatus.get(player.getName()).equals("lobby")  && event.getNewGameMode() != GameMode.CREATIVE) {
            if (!player.getInventory().contains(Material.NETHER_STAR)) {
                GiveItem(player);
            }
        }
    }

    @EventHandler
    public  void onDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (main.playerStatus.get(player.getName()).equals("lobby") ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        DataFiles dataFiles = new DataFiles(player);
        PlayerData data4 = dataFiles.LoadData();
        if (event.getItemDrop() == null) {
            return;
        }
        if ((event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) && data4.hasJoined && (player.getGameMode() != GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    public void GiveItem(Player player) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Skyclash Menu");
            List<String> lore = new ArrayList<>();
            lore.add("Click to access the menu");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        player.getInventory().setItem(8, item);
    }
}