package skyclash.skyclash.gameManager;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.Clock;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.kitscards.RemoveTags;
import skyclash.skyclash.lobby.LobbyControls;
import skyclash.skyclash.main;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({"unchecked", "UnnecessaryBoxing"})
public class EndGame {
    public EndGame(boolean isCommand) {
        // reset all map votes
        main.mapVotes.forEach((key1, value1) -> main.mapVotes.put(key1, 0));
        AtomicReference<String> winner = new AtomicReference<>("no one");

        if (!isCommand) {
            // check for winner
            main.playerStatus.forEach((key, value) -> {
                if (value.equals("ingame")) {
                    winner.set(key);
                }
            });
        }

        // get lobby spawn location
        Mapsfile maps = new Mapsfile();
        maps.read_file(false, false);
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

        // loop for every player
        main.playerStatus.forEach((key, value) -> {
            if (value.equals("spectator") ^ value.equals("ingame")) {
                Player player = Bukkit.getServer().getPlayer(key);
                if (isCommand) {
                    player.sendMessage("The game has abruptly ended");
                } else {
                    player.sendMessage(ChatColor.GREEN + "The winner is " + winner);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
                player.setScoreboard(Clock.emptyboard);
                player.getInventory().clear();
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
                player.getInventory().setBoots(new ItemStack(Material.AIR));
                player.setGameMode(GameMode.ADVENTURE);
                for (PotionEffect effect : player.getActivePotionEffects())
                    player.removePotionEffect(effect.getType());
                player.setHealth(20);
                player.setSaturation(20);
                player.setLevel(0);
                player.setExp(0);
                main.playerStatus.put(player.getName(), "lobby");
                main.playerVote.remove(player.getName());
                LobbyControls.GiveItem(player);
                if (player.hasMetadata("NoMovement")) {
                    player.removeMetadata("NoMovement", main.getPlugin(main.class));
                }
                player.teleport(spawnloc);
                new RemoveTags(player);
            }
        });
        String ingamemap = main.mvcore.getMVWorldManager().getMVWorld("ingame_map").getName();
        if (ingamemap != null) {
            main.mvcore.getMVWorldManager().deleteWorld(ingamemap, true, true);
            main.activeWorld = null;
        }
        main.isGameActive = false;
        Clock.playersLeft = 0;
        Clock.timer = 0;
    }
}
