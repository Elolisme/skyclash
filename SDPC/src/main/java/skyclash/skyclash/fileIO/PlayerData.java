package skyclash.skyclash.fileIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PlayerData {
    public String Name;
    public String Card;
    public String Kit;
    public boolean hasJoined;
    public JSONObject Stats;
    public int Coins;
    public Boolean Autoready;
    public JSONArray Owned;


    public PlayerData(String name, String card, String kit, boolean hasJoined, JSONObject stats, int coins, Boolean autoready, JSONArray owned) {
        this.Name = name;
        this.Card = card;
        this.Kit = kit;
        this.hasJoined = hasJoined;
        this.Stats = stats;
        this.Coins = coins;
        this.Autoready = autoready;
        this.Owned = owned;
    }
}
