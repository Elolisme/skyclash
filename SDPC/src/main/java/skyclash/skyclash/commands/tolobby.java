package skyclash.skyclash.commands;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.chestgen.StringToJSON;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.Mapsfile;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;
import skyclash.skyclash.lobby.LobbyControls;
import skyclash.skyclash.main;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class tolobby implements CommandExecutor {
    private PlayerStatus playerstatus = new PlayerStatus();

    @SuppressWarnings({"UnnecessaryBoxing", "unchecked"})
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for arguments
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"Must be a player");
            return true;
        }
        Player player = (Player) sender;

        // If player is still playing game
        if (playerstatus.PlayerEqualsStatus(player, PlayerState.INGAME)) {
            player.sendMessage(ChatColor.RED + "You cannot give up!");
            return true;
        }

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
        playerstatus.SetStatus(player, PlayerState.LOBBY);
        DataFiles datafiles = new DataFiles(player);
        PlayerData playerdata = datafiles.data;
        if (playerdata.Autoready == true) {
            playerstatus.SetStatus(player, PlayerState.READY);
        }        
        LobbyControls.CheckStartGame(true);
        LobbyControls.GiveItem(player);
        LobbyControls.GiveMapNavItem(player);
        if (player.hasMetadata("NoMovement")) {
            player.removeMetadata("NoMovement", main.plugin);
        }

        player.teleport(spawnloc);
        return true;
    }
}
