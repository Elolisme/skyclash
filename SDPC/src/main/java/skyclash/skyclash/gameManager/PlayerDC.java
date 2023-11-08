package skyclash.skyclash.gameManager;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.lobby.LobbyControls;
import skyclash.skyclash.lobby.VoteMap;
import skyclash.skyclash.main;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerDC implements Listener {
    public PlayerDC(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        switch (PlayerStatus.StatusMap.get(player.getName())) {
            case INGAME: ingame(player); break;
            case READY: ready(player); break;
            case SPECTATOR: spec(player); break;
            case LOBBY: ready(player); break;
        }
    }

    @SuppressWarnings("unchecked")
    private void ingame(Player player) {
        // drops items hopefully
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            }
        }

        Scheduler.playersLeft--;
        Bukkit.broadcastMessage(ChatColor.RED+player.getName()+" has died due to disconnecting");
        
        new StatsManager().changeStat(player, "deaths", 1);
        new StatsManager().changeStat(player, "Disconnect deaths", 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, () -> {
            if (Scheduler.playersLeft <= 1) {
                new EndGame(false);
            }
        }, 5);
        // get lobby spawn location
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        AtomicReference<String> default_world = new AtomicReference<>();
        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        AtomicInteger z = new AtomicInteger();
        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean isdefault = (boolean) info1.get("isdefault");
            String name1 = (String) name;
            JSONArray spawnsarray = StringToJSON.convert((String) info1.get("spawns"));
            JSONArray lists = (JSONArray) spawnsarray.get(0);
            if (isdefault) {
                default_world.set(name1);
                x.set(Integer.valueOf(String.valueOf(lists.get(0))));
                y.set(Integer.valueOf(String.valueOf(lists.get(1))));
                z.set(Integer.valueOf(String.valueOf(lists.get(2))));
            }
        });
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        worldManager.loadWorld(default_world.get());
        World w = Bukkit.getWorld(default_world.get());
        Location spawnloc = new Location(w, x.get(), y.get(), z.get());

        player.setScoreboard(Scheduler.emptyboard);
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setSaturation(20);
        player.setLevel(0);
        player.setExp(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        PlayerStatus.StatusMap.remove(player.getName());
        VoteMap.playerVote.remove(player.getName());
        if (player.hasMetadata("NoMovement")) {
            player.removeMetadata("NoMovement", main.plugin);
        }
        player.teleport(spawnloc);
        new RemoveTags(player);
    }

    private void ready(Player player) {
        PlayerStatus.StatusMap.remove(player.getName());
        LobbyControls.CheckStartGame(false);
    }

    @SuppressWarnings("unchecked")
    private void spec(Player player) {
        // get lobby spawn location
        Mapsfile maps = new Mapsfile();
        maps.readFile(false, false);
        AtomicReference<String> default_world = new AtomicReference<>();
        AtomicInteger x = new AtomicInteger();
        AtomicInteger y = new AtomicInteger();
        AtomicInteger z = new AtomicInteger();
        maps.jsonObject.forEach((name, info) -> {
            JSONObject info1 = (JSONObject) info;
            boolean isdefault = (boolean) info1.get("isdefault");
            String name1 = (String) name;
            JSONArray spawnsarray = StringToJSON.convert((String) info1.get("spawns"));
            JSONArray lists = (JSONArray) spawnsarray.get(0);
            if (isdefault) {
                default_world.set(name1);
                x.set(Integer.valueOf(String.valueOf(lists.get(0))));
                y.set(Integer.valueOf(String.valueOf(lists.get(1))));
                z.set(Integer.valueOf(String.valueOf(lists.get(2))));
            }
        });
        MVWorldManager worldManager = main.mvcore.getMVWorldManager();
        worldManager.loadWorld(default_world.get());
        World w = Bukkit.getWorld(default_world.get());
        Location spawnloc = new Location(w, x.get(), y.get(), z.get());

        // player setup
        player.setScoreboard(Scheduler.emptyboard);
        player.getInventory().clear();
        player.getEquipment().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setSaturation(20);
        PlayerStatus.StatusMap.remove(player.getName());
        LobbyControls.GiveItem(player);
        player.teleport(spawnloc);
    }
}
