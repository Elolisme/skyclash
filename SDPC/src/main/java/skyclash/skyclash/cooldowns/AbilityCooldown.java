package skyclash.skyclash.cooldowns;

import java.util.HashMap;

public class AbilityCooldown {
    public HashMap<String, AbilityCooldown> cooldownMap = new HashMap<>();
    public String ability = "";
    public String player = "";
    public long seconds;
    public long systime;

    public AbilityCooldown(String player, long seconds, long systime) {
        this.player = player;
        this.seconds = seconds;
        this.systime = systime;
    }

    public AbilityCooldown(String player) {
        this.player = player;
    }



}
