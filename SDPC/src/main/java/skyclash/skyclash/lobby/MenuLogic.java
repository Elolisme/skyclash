package skyclash.skyclash.lobby;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import skyclash.skyclash.main;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;

public class MenuLogic implements Listener {
    public MenuLogic(main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private PlayerStatus playerstatus = new PlayerStatus();

    // Open main menu
    @EventHandler
    public void onUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!playerstatus.ContainsName(player)) {
            return;
        }
        if (!playerstatus.PlayerEqualsStatus(player, PlayerState.LOBBY) && !playerstatus.PlayerEqualsStatus(player, PlayerState.READY)) {
            return;
        }
        if (!(player.getItemInHand().getType() == Material.NETHER_STAR || player.getItemInHand().getType() == Material.EMERALD)) {
            return;
        }
        if (!player.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Skyclash Menu")) {
            if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "View and edit maps")) {
                player.openInventory(Inventories.ViewAndEditMaps(player));
                player.setMetadata("OpenedMapMenu", new FixedMetadataValue(main.plugin, "Skyclash Menu"));
            }
            return;
        }
        
        player.openInventory(Inventories.mainInventory(player));
        player.setMetadata("OpenedMenu", new FixedMetadataValue(main.plugin, "Skyclash Menu"));
    }

    // click an item in menu
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!playerstatus.ContainsName(player) || event.getCurrentItem() == null) {
            return;
        }
        if ((event.getCurrentItem().getType() == Material.AIR) || !(playerstatus.PlayerEqualsStatus(player, PlayerState.LOBBY) || playerstatus.PlayerEqualsStatus(player, PlayerState.READY))) {
            return;
        }

        // Ensure player cant get rid of item
        if (((event.getCurrentItem().getType() == Material.NETHER_STAR) || (event.getCurrentItem().getType() == Material.EMERALD)) && (player.getGameMode() != GameMode.CREATIVE)) {
            event.setCancelled(true);
        }

        // inside skyclash menu
        if (player.hasMetadata("OpenedMenu")){
            event.setCancelled(true);
            switch (event.getSlot()) {
                case 9: player.openInventory(Inventories.mapsInventory(player)); player.setMetadata("OpenedMenu2", new FixedMetadataValue(main.plugin, "Map Selection"));break;
                case 11: MenuActions.CheckReady(player);break;
                case 13: player.openInventory(Inventories.shopInventory(player)); player.setMetadata("OpenedMenu5", new FixedMetadataValue(main.plugin, "Shop Selection"));break;
                case 15: player.openInventory(Inventories.kitsInventory(player)); player.setMetadata("OpenedMenu3", new FixedMetadataValue(main.plugin, "Kit Selection"));break;
                case 17: player.openInventory(Inventories.cardsInventory(player)); player.setMetadata("OpenedMenu4", new FixedMetadataValue(main.plugin, "Card Selection"));break;
            }
        } else if (player.hasMetadata("OpenedMenu2")) {
            MenuActions.VoteForMap(player, event);
        } else if (player.hasMetadata("OpenedMenu3")) {
            MenuActions.SelectKit(player, event);
        } else if (player.hasMetadata("OpenedMenu4")) {
            MenuActions.SelectCard(player, event);
        } else if (player.hasMetadata("OpenedMenu5")) {
            MenuActions.BuyItemFromShop(player, event);
        }

        // inside map menu
        if (player.hasMetadata("OpenedMapMenu")) {
            MenuActions.TeleportToMap(player, event);
        }
    }

    @EventHandler
    public void onCloseMenu(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        ClearTags(player);
    } 
    
    public static void ClearTags(Player player) {
        if (player.hasMetadata("OpenedMenu")) {
            player.removeMetadata("OpenedMenu", main.plugin);
        }
        if (player.hasMetadata("OpenedMenu2")) {
            player.removeMetadata("OpenedMenu2", main.plugin);
        }
        if (player.hasMetadata("OpenedMenu3")) {
            player.removeMetadata("OpenedMenu3", main.plugin);
        }
        if (player.hasMetadata("OpenedMenu4")) {
            player.removeMetadata("OpenedMenu4", main.plugin);
        }
        if (player.hasMetadata("OpenedMenu5")) {
            player.removeMetadata("OpenedMenu5", main.plugin);
        }
        if (player.hasMetadata("OpenedMapMenu")) {
            player.removeMetadata("OpenedMapMenu", main.plugin);
        }
    }
}