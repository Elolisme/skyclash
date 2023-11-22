package skyclash.skyclash.fileIO;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData {
    public String name;
    public String card;
    public String kit;
    public boolean hasJoined;
    public HashMap<String, Integer> stats;
    public int coins;
    public Boolean autoready;
    public ArrayList<String> owned;

    public PlayerData(String name, String card, String kit, boolean hasJoined, HashMap<String, Integer> stats, int coins, Boolean autoready, ArrayList<String> owned) {
        this.name = name;
        this.card = card;
        this.kit = kit;
        this.hasJoined = hasJoined;
        this.stats = stats;
        this.coins = coins;
        this.autoready = autoready;
        this.owned = owned;
    }

    public PlayerData(String name) {
        this.name = name;
        this.card = "No Card";
        this.kit = "Swordsman";
        this.hasJoined = false;
        this.stats = new HashMap<>();
        this.coins = 0;
        this.autoready = false;
        this.owned = new ArrayList<>();
    }
}
