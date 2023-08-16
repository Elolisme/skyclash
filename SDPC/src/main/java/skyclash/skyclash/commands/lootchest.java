package skyclash.skyclash.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skyclash.skyclash.main;
import skyclash.skyclash.chestgen.ChestManager;

import java.util.Set;
import java.util.logging.Level;

public class lootchest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getPlugin(main.class).getLogger().log(Level.INFO, "This command is player only");
            return true;
        }

        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);

        if (!(targetBlock.getState() instanceof Chest)) {
            player.sendMessage("§cPlease look at a chest while performing this command!");
            return true;
        }

        ChestManager chestManager = new ChestManager(null, null, args[0]);
        chestManager.chest = (Chest) targetBlock.getState();

        chestManager.createChestLoot();
        player.sendMessage("The LootChest: '§5" + args[0] + "§r' was §2successfully§r saved!");
        return true;
    }
}
