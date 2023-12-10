package skyclash.skyclash.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Cooldown {
    public static HashMap<String, AbilityCooldown> cooldownPlayers = new HashMap<>();

    public static void add(String player, String ability, long seconds, long systime) {
        if (!cooldownPlayers.containsKey(player))
            cooldownPlayers.put(player, new AbilityCooldown(player));
        if (isCooling(player, ability))
            return;
        cooldownPlayers.get(player).cooldownMap.put(ability,
                new AbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
    }

    public static boolean isCooling(String player, String ability) {
        if (!cooldownPlayers.containsKey(player))
            return false;
        return cooldownPlayers.get(player).cooldownMap.containsKey(ability);
    }

    public static double getRemaining(String player, String ability) {
        if (!cooldownPlayers.containsKey(player))
            return 0.0;
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability))
            return 0.0;
        long cooltime = (cooldownPlayers.get(player).cooldownMap.get(ability).seconds + cooldownPlayers.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis();

        return trim(cooltime / 1000.0D, 1);
    }

    public static double trim(double untrimmeded, int decimal) {
        StringBuilder format = new StringBuilder("#.#");

        for(int i = 1; i < decimal; i++) {
            format.append("#");
        }
        DecimalFormat twoDec = new DecimalFormat(format.toString());
        return Double.parseDouble(twoDec.format(untrimmeded));
    }

    public static void coolDurMessage(Player player, String ability) {
        if (player == null) {
            return;
        }
        if (!isCooling(player.getName(), ability)) {
            return;
        }
    }

    public static void removeCooldown(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) {
            return;
        }
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);
    }

    public static void handleCooldowns() {
        if (cooldownPlayers.isEmpty()) {
            return;
        }
        try {
            for (String key : cooldownPlayers.keySet()) {
                for (String name : cooldownPlayers.get(key).cooldownMap.keySet()) {
                    if (getRemaining(key, name) <= 0.0) {
                        removeCooldown(key, name);
                    }
                }
            }    
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage("Concurrent Modification error thing in Cooldown.java ln 65");
        }
        
    }
}
