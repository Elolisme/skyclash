package skyclash.skyclash.lobby;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.World;
import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.fileIO.Mapsfile;

public class VoteMap {
    private static HashMap<Integer, Integer> mapVotes = new HashMap<>();
    public static HashMap<String, Integer> playerVote = new HashMap<>();
    private Mapsfile maps = new Mapsfile();

    public void addMap(Integer mapIndex) {
        mapVotes.put(mapIndex, 0);
    }
    
    public void resetMaps() {
        mapVotes.forEach((key1, value1) -> {
            mapVotes.put(key1, 0);
        });
    }

    public void initialiseMaps() {
        maps.loadFileYML();
        maps.saveFileYML();
        for (int i = 1; i <= maps.getPlayableMaps(); i++) {
            addMap(i);
        }
    }

    public void setMapValue(Integer index, Integer value) {
        mapVotes.put(index, value);
    }

    public Integer mapSize() {
        return mapVotes.size();
    }

    public Integer getMapValue(Integer index) {
        return mapVotes.get(index);
    }


    private Integer getHighestVotedMapIndex() {
        AtomicInteger max = new AtomicInteger(-1);
        AtomicInteger max_map = new AtomicInteger();
        mapVotes.forEach((mapid, votes) -> {
            if(votes > max.get()) {
                max.set(votes);
                max_map.set(mapid);
            } else if (votes == max.get()) {
                Random rd = new Random();
                if (rd.nextBoolean()) {
                    max.set(votes);
                    max_map.set(mapid);
                }
            }
        });
        return max_map.get();
    }

    public World getHighestVotedWorld() {
        maps.loadFileYML();
        Integer maxMap = getHighestVotedMapIndex();
        AtomicReference<String> VotedMap = new AtomicReference<>("");
        AtomicInteger count = new AtomicInteger(1);
        
        maps.data.forEach((name, info) -> {
            if (!info.getIgnore()) {
                if (maxMap == count.get()) {
                    VotedMap.set(name);
                }
                count.getAndIncrement();
            }
        });
        return new Multiverse().GetBukkitWorld(VotedMap.get());
    }
}
