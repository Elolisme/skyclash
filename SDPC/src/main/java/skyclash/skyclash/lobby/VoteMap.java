package skyclash.skyclash.lobby;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.World;
import org.json.simple.JSONObject;

import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.fileIO.Mapsfile;

public class VoteMap {
    private static HashMap<Integer, Integer> mapVotes = new HashMap<>();
    public static HashMap<String, Integer> playerVote = new HashMap<>();
    private Mapsfile maps = new Mapsfile();

    public void addMap(Integer mapIndex) {
        mapVotes.put(mapIndex, 0);
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

    public void resetMaps() {
        mapVotes.forEach((key1, value1) -> {
            mapVotes.put(key1, 0);
        });
    }

    private Integer getHighestVotedMapIndex() {
        // find map with the highest vote
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

    @SuppressWarnings("unchecked")
    public World getHighestVotedWorld() {
        maps.readFile(false, false);

        Integer maxMap = getHighestVotedMapIndex();
        AtomicReference<String> VotedMap = new AtomicReference<>("");
        AtomicInteger count = new AtomicInteger(1);
        
        maps.jsonObject.forEach((name, info) -> {
            String name1 = (String) name;
            JSONObject info1 = (JSONObject) info;
            boolean ignore = (boolean) info1.get("ignore");
            if (!ignore) {
                if (maxMap == count.get()) {
                    VotedMap.set(name1);
                }
                count.getAndIncrement();
            }
        });

        return new Multiverse().GetBukkitWorld(VotedMap.get());
    }
}
