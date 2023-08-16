package skyclash.skyclash.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Delayed implements Listener {
    private static Plugin plugin = null;

    public Delayed(Plugin instance) {
        plugin = instance;
    }

    public Delayed(Runnable runnable) {
        this(runnable, 0);
    }

    public Delayed(Runnable runnable, long delay) {
        if (plugin.isEnabled()) {
            int id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
        } else {
            runnable.run();
        }

    }
}
