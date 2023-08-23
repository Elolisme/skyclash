package skyclash.skyclash.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import skyclash.skyclash.main;

public class Delayed implements Listener {
    private static final Plugin plugin = main.getPlugin(main.class);
    private int id = -1;

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
