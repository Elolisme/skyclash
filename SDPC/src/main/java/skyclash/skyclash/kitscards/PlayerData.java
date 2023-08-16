package skyclash.skyclash.kitscards;

public class PlayerData {
    public String Name;
    public String Card;
    public String Kit;
    public boolean hasJoined;


    public PlayerData(String name, String card, String kit, boolean hasJoined) {
        this.Name = name;
        this.Card = card;
        this.Kit = kit;
        this.hasJoined = hasJoined;
    }
}
