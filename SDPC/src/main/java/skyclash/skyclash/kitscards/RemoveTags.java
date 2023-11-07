package skyclash.skyclash.kitscards;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import skyclash.skyclash.main;

public class RemoveTags {
    public RemoveTags(Player player) {
        Plugin p = main.getPlugin(main.class);
        player.removeMetadata("Assassin", p);
        player.removeMetadata("Swordsman", p);
        player.removeMetadata("Berserker", p);
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
        player.removeMetadata("Blast Protection", p);
        player.removeMetadata("Elven Archer", p);
        player.removeMetadata("Quiver Refill", p);
        player.removeMetadata("Apple Finder", p);
        player.removeMetadata("Hit and Run", p);
        player.removeMetadata("Pacify", p);
        player.removeMetadata("Pearl Absorption", p);
        player.removeMetadata("Sugar Rush", p);
        player.removeMetadata("Lifesteal", p);
        player.removeMetadata("Monster Hunter", p);  
        player.removeMetadata("Jester", p);      
    }
}
