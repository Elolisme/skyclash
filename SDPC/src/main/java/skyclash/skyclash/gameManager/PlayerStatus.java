package skyclash.skyclash.gameManager;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;

import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;

public class PlayerStatus {
    public enum PlayerState {LOBBY, READY, INGAME, SPECTATOR};
    public static HashMap<String, PlayerState> StatusMap = new HashMap<>();

    public void SetStatus(String playername, PlayerState state) {
        StatusMap.put(playername, state);
    }

    public void SetStatus(Player player, PlayerState state) {
        StatusMap.put(player.getName(), state);
    }

    public Boolean PlayerEqualsStatus(String playername, PlayerState state) {
        if (StatusMap.get(playername) == state) {
            return true;
        }
        return false;
    }

    public Boolean PlayerEqualsStatus(Player player, PlayerState state) {
        if (StatusMap.get(player.getName()) == state) {
            return true;
        }
        return false;
    }

    public Boolean ContainsName(String playername) {
        if (StatusMap.containsKey(playername)) {
            return true;
        }
        return false;
    }

    public Boolean ContainsName(Player player) {
        if (StatusMap.containsKey(player.getName())) {
            return true;
        }
        return false;
    }

    public void SetLobbyOrReady(Player player) {
        new PlayerStatus().SetStatus(player, PlayerState.LOBBY);
        DataFiles datafiles = new DataFiles(player);
        PlayerData playerdata = datafiles.data;
        if (playerdata.Autoready == true) {
            SetStatus(player, PlayerState.READY);
        }
    }

    public int CountPeopleWithStatus(PlayerState state) {
        AtomicInteger count = new AtomicInteger(0);
        PlayerStatus.StatusMap.forEach((key, value) -> {
            if (value == state) {
                count.getAndIncrement();
            }
        });

        return count.get();
    }
}
