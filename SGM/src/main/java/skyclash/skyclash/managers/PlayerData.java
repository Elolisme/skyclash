package skyclash.skyclash.managers;

public class PlayerData {
    public String Name;
    public String Card;
    public String Kit;
    public String State;


    public PlayerData(String name, String card, String kit, String state) {
        this.Name = name;
        this.Card = card;
        this.Kit = kit;
        this.State = state;
    }
    public PlayerData(String name, String state) {
        this.Name = name;
        this.Card = "null";
        this.Kit = "null";
        this.State = state;
    }
}
