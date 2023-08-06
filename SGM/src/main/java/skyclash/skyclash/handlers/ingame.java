package skyclash.skyclash.handlers;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import skyclash.skyclash.main;
import skyclash.skyclash.managers.DataFiles;
import skyclash.skyclash.managers.PlayerData;
import skyclash.skyclash.managers.Mapsfile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ingame implements Listener {
    public ingame(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // event handler
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("NoMovement")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        player.updateInventory();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!main.playertracker.containsKey(player.getName())) {
            main.playertracker.put(player.getName(), "lobby");
        }

        if (!main.playertracker.get(player.getName()).equals("ingame")) {
            return;
        }


        // code
        main.playertracker.put(player.getName(), "spectator");
        player.setGameMode(GameMode.SPECTATOR);
        Location loc = event.getEntity().getLocation();
        player.getWorld().strikeLightningEffect(loc);
        Bukkit.getScheduler().scheduleSyncDelayedTask(main.getPlugin(main.class), () -> {
            player.spigot().respawn();
            player.teleport(loc);
            if (player.getLocation().getY() < 0) {
                Location loc2 = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 100, player.getLocation().getZ());
                player.teleport(loc2);
            }
            }, 2);
        player.getInventory().clear();
        main.PlayersLeft--;
        player.sendMessage(ChatColor.RED+"Better luck next time");


        Bukkit.getScheduler().scheduleSyncDelayedTask(main.getPlugin(main.class), () -> {
            if (main.PlayersLeft <= 1) {
                Endgame();
            }
        }, 5);



    }

    @SuppressWarnings("unchecked")
    public void Endgame() {
        // reset all map votes
        main.mapselection.forEach((key1, value1) -> main.mapselection.put(key1, 0));

        AtomicReference<String> winner = new AtomicReference<>("No one");

        // check for winner
        main.playertracker.forEach((key, value) -> {
            if (value.equals("ingame")) {
                winner.set(key);
            }
        });

        // loop for every player
        main.playertracker.forEach((key, value) -> {
            if (value.equals("spectator") ^ value.equals("ingame")) {
                Player player = Bukkit.getServer().getPlayer(key);

                DataFiles datafiles = new DataFiles(player);
                PlayerData data = datafiles.LoadData();

                player.sendMessage(ChatColor.GREEN + "The winner is " + winner);
                player.setScoreboard(main.emptyboard);
                player.getInventory().clear();
                player.setGameMode(GameMode.ADVENTURE);
                player.setHealth(20);
                player.setSaturation(20);
                data.State = "Lobby";
                main.playertracker.put(player.getName(), "lobby");
                main.playermap.remove(player.getName());

                // give item
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

                datafiles.SetData(data);

                Mapsfile maps = new Mapsfile();
                maps.read_file(false, false);
                AtomicReference<String> default_world = new AtomicReference<>();
                maps.jsonObject.forEach((name, info) -> {
                    JSONObject info1 = (JSONObject) info;
                    boolean isdefault = (boolean) info1.get("isdefault");
                    String name1 = (String) name;
                    if (isdefault) {
                        default_world.set(name1);
                    }
                });

                MVWorldManager worldManager = main.mvcore.getMVWorldManager();
                worldManager.loadWorld(default_world.get());
                World w = Bukkit.getWorld("world");
                Location spawnloc = new Location(w, 0, 100, 0);
                player.teleport(spawnloc);
            }
        });
        main.mvcore.getMVWorldManager().deleteWorld(main.current_world.getName(), true, true);
        main.current_world = null;


    }
}