package skyclash.skyclash.kitscards;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import skyclash.skyclash.main;

public class RemoveTags {
    public RemoveTags(Player player) {
        Plugin p = main.getPlugin(main.class);
        player.removeMetadata("Assassin", p);
        player.removeMetadata("Swordsman", p);
        player.removeMetadata("Beserker", p);
        player.removeMetadata("Bigger Bangs", p);
    }
}
