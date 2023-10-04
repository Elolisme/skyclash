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
        player.removeMetadata("Archer", p);
        player.removeMetadata("Frost_Knight", p);
        player.removeMetadata("Cleric", p);
        player.removeMetadata("Guardian", p);
        player.removeMetadata("Jumpman", p);
        player.removeMetadata("Necromancer", p);
        player.removeMetadata("Treasure_hunter", p);
        player.removeMetadata("Scout", p);
        player.removeMetadata("Bigger Bangs", p);
        player.removeMetadata("Creeper", p);
    }
}
