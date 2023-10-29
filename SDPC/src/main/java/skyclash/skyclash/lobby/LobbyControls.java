package skyclash.skyclash.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import skyclash.skyclash.Clock;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.gameManager.StartGame;
import skyclash.skyclash.gameManager.StatsManager;
import skyclash.skyclash.main;
import skyclash.skyclash.cooldowns.Cooldown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyControls implements Listener {
    public LobbyControls(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
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
            data.Kit = "Swordsman";
            data.Card = "Damage Potion";
            data.Name = player.getName();
        }
        datafiles.SetData(data);

        if (data.Autoready == true) {
            main.playerStatus.put(player.getName(), "ready");
        }

        StatsManager.changeStat(player, "Joins", 1);
        
    }

    @EventHandler
    public void onGamemodeSwitch(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (!main.playerStatus.containsKey(player.getName())) {
            return;
        }
        if (main.playerStatus.get(player.getName()).equals("lobby") || main.playerStatus.get(player.getName()).equals("ready")) {
            if (!player.getInventory().contains(Material.NETHER_STAR)) {
                GiveItem(player);
            }
        }
    }

    @EventHandler
    public  void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (main.playerStatus.get(player.getName()).equals("lobby") || main.playerStatus.get(player.getName()).equals("ready")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.LoadData();
        if (event.getItemDrop() == null) {
            return;
        }
        if ((event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) && data.hasJoined && (player.getGameMode() != GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    // Main menu item
    public static void GiveItem(Player player) {
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

    // Pearl Cooldown
    @EventHandler
    public void onUsePearl(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ENDER_PEARL) {return;}
        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof Player)) {return;}
        Player player = (Player) shooter;
        if(Cooldown.isCooling(player.getName(), "Pearl")) {
            player.sendMessage(ChatColor.GRAY + "Pearl Cooldown: " + ChatColor.AQUA + Cooldown.getRemaining(player.getName(), "Pearl") + " Seconds");
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            event.setCancelled(true);
            return;
        }
        Cooldown.add(player.getName(), "Pearl", 2, System.currentTimeMillis());
    }

    public static void CheckStartGame(boolean started) {
        AtomicInteger people_ready = new AtomicInteger(0);
        main.playerStatus.forEach((key, value) -> {
            if (value.equals("ready")) {
                people_ready.getAndIncrement();
            }
        });
        if (started && Integer.parseInt(String.valueOf(people_ready)) == 2) {
            new StartGame(false);
        }
        if (!started && Integer.parseInt(String.valueOf(people_ready)) == 1) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game cancelled due to insufficient people ready");
            if (StartGame.task1 != null) {
                StartGame.EndTasks();
            }
        }
    }
}
