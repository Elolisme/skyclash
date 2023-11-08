package skyclash.skyclash.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import skyclash.skyclash.main;

public class Delayed implements Listener {
    private final Plugin plugin = main.plugin;
    public Delayed(Runnable runnable) {
        this(runnable, 0);
    }

    public Delayed(Runnable runnable, long delay) {
        if (plugin.isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
        } else {
            runnable.run();
        }

    }
}
