package skyclash.skyclash.kitscards;

import org.json.simple.JSONArray;

public class PlayerData {
    public String Name;
    public String Card;
    public String Kit;
    public boolean hasJoined;
    public JSONArray Stats;
    public int Coins;


    public PlayerData(String name, String card, String kit, boolean hasJoined, JSONArray stats, int coins) {
        this.Name = name;
        this.Card = card;
        this.Kit = kit;
        this.hasJoined = hasJoined;
        this.Stats = stats;
        this.Coins = coins;
    }
}
