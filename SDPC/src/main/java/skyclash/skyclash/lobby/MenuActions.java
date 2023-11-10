package skyclash.skyclash.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import skyclash.skyclash.Scheduler;
import skyclash.skyclash.main;
import skyclash.skyclash.WorldManager.Multiverse;
import skyclash.skyclash.WorldManager.SCWorlds;
import skyclash.skyclash.fileIO.DataFiles;
import skyclash.skyclash.fileIO.PlayerData;
import skyclash.skyclash.gameManager.PlayerStatus;
import skyclash.skyclash.gameManager.PlayerStatus.PlayerState;
import skyclash.skyclash.kitscards.Cards;

public class MenuActions {
    public static void CheckReady(Player player) {
        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.data;

        PlayerStatus pStatus = new PlayerStatus();

        player.closeInventory();
        if (pStatus.PlayerEqualsStatus(player, PlayerState.LOBBY) && !main.isGameActive) {
            player.sendMessage(ChatColor.YELLOW+"You are now ready");
            pStatus.SetStatus(player, PlayerState.READY);
            data.Autoready = false;
            LobbyControls.CheckStartGame(true);
        } 
        else if (pStatus.PlayerEqualsStatus(player, PlayerState.READY) && !main.isGameActive){
            if (data.Autoready == false) {
                player.sendMessage(ChatColor.YELLOW+"You will now automatically ready up");
                data.Autoready = true;
            } 
            else {
                player.sendMessage(ChatColor.YELLOW+"You are no longer ready");
                pStatus.SetStatus(player, PlayerState.LOBBY);
                data.Autoready = false;
                LobbyControls.CheckStartGame(false);
            }
        } 
        else if (main.isGameActive) {
            player.sendMessage(ChatColor.RED+"Spectating current game, use /lobby to return to lobby");
            Location spawnloc = new Location(new Multiverse().GetBukkitWorld(SCWorlds.INGAME_MAP), 0, 70, 0);
            pStatus.SetStatus(player, PlayerState.SPECTATOR);
            player.setScoreboard(Scheduler.board);
            player.teleport(spawnloc);
            player.getInventory().clear();
            new Scheduler().scheduleTask(()->player.setGameMode(GameMode.SPECTATOR), 3);
        } 
        else {
            player.sendMessage("Please use /lobby to go back to skyclash");
        }

        datafiles.SetData(data);
    }

    public static void VoteForMap(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        player.closeInventory();

        VoteMap votemap = new VoteMap();
        int slot = event.getSlot() + 1;
        Integer currentMapValue = votemap.getMapValue(slot);

        if (!(0 <= event.getRawSlot() && event.getRawSlot() <= (votemap.mapSize()-1))) {
            return;
        }

        if (VoteMap.playerVote.containsKey(player.getName())) {
            int oldmap = VoteMap.playerVote.get(player.getName());
            if (oldmap == slot) {
                player.sendMessage(ChatColor.RED + "You have already voted for this map!");
                return;
            }
            Integer num = votemap.getMapValue(oldmap);
            num--;
            votemap.setMapValue(oldmap, num);
            votemap.addMap(num);
        }

        currentMapValue++;
        votemap.setMapValue(slot, currentMapValue);
        VoteMap.playerVote.put(player.getName(), slot);
        String[] display = event.getCurrentItem().getItemMeta().getDisplayName().split(":");
        player.sendMessage(ChatColor.GREEN + "You have voted for "+ display[0]);
    }

    public static void SelectKit(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (0 <= event.getRawSlot() && event.getRawSlot() <= 26) {
            player.closeInventory();
            DataFiles datafiles = new DataFiles(player);
            PlayerData data = datafiles.data;
            data.Kit = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            player.sendMessage(ChatColor.YELLOW + "You have chosen " + data.Kit + " kit");
            datafiles.SetData(data);
        }
     }

    @SuppressWarnings("unchecked")
    public static void SelectCard(Player player, InventoryClickEvent event) {
        event.setCancelled(true);

        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.data;

        String selectedCard = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
        if (event.getInventory().getItem(32).getItemMeta().getDisplayName().equals(ChatColor.BLUE+"Click for Buy Mode")) {
            if (0 <= event.getRawSlot() && event.getRawSlot() <= 8) {
                player.closeInventory();
                
                if (data.Coins<5) {
                    player.sendMessage(ChatColor.YELLOW + "You don't have enough money to use this card");
                    data.Card = "No Card";
                    datafiles.SetData(data);
                    return;
                }
                data.Card = selectedCard;
                player.sendMessage(ChatColor.YELLOW + "You have chosen " + data.Card + " card");
                datafiles.SetData(data);
            }
            if (9 <= event.getRawSlot() && event.getRawSlot() <= 17) {
                player.closeInventory();
                if (data.Coins<8) {
                    player.sendMessage(ChatColor.YELLOW + "You don't have enough money to use this card");
                    data.Card = "No Card";
                    datafiles.SetData(data);
                    return;
                }
                data.Card = selectedCard;
                player.sendMessage(ChatColor.YELLOW + "You have chosen " + data.Card + " card");
                datafiles.SetData(data);
            }
            if (event.getRawSlot() == 31) {
                player.closeInventory();
                data.Card = selectedCard;
                player.sendMessage(ChatColor.YELLOW + "You have chosen " + data.Card);
                datafiles.SetData(data);
            }
            if (event.getRawSlot() == 32) {
                ItemStack item = new Wool(DyeColor.GREEN).toItemStack(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.BLUE + "Click for Select Mode");
                    item.setItemMeta(meta);
                }
                event.getInventory().setItem(32, item);

                for (int i = 0; i <= 17; i++) {
                    if (event.getInventory().getItem(i) != null && event.getInventory().getItem(i).hasItemMeta()) {
                        if (datafiles.data.Owned.contains(ChatColor.stripColor(event.getInventory().getItem(i).getItemMeta().getDisplayName()))) {
                            event.getInventory().getItem(i).setType(Material.WOOL);
                            event.getInventory().getItem(i).setDurability((short)5);
                            ItemMeta meta1 = event.getInventory().getItem(i).getItemMeta();
                            List<String> Lore = new ArrayList<>();
                            Lore.add("Bought");
                            meta1.setLore(Lore);
                            event.getInventory().getItem(i).setItemMeta(meta1);
                        }
                    }
                }
            }
        } 
        else {
            if (0 <= event.getRawSlot() && event.getRawSlot() <= 8) {
                player.closeInventory();
                if (data.Owned.contains(selectedCard)) {
                    player.sendMessage(ChatColor.YELLOW + "You already own this card");
                    return;
                }
                if (data.Coins<Cards.CardCost1) {
                    player.sendMessage(ChatColor.RED + "You don't have enough money to buy this card");
                    return;
                }
                data.Owned.add(selectedCard);
                player.sendMessage(ChatColor.YELLOW + "You have bought " + selectedCard + " card for "+Cards.CardCost1+" coins\nIt has been selected");
                data.Coins = data.Coins - Cards.CardCost1;
                data.Card = selectedCard;
                datafiles.SetData(data);
            }
            if (9 <= event.getRawSlot() && event.getRawSlot() <= 17) {
                player.closeInventory();
                if (data.Owned.contains(selectedCard)) {
                    player.sendMessage(ChatColor.YELLOW + "You already own this card");
                    return;
                }
                if (data.Coins<Cards.CardCost2) {
                    player.sendMessage(ChatColor.RED + "You don't have enough money to buy this card");
                    return;
                }
                data.Owned.add(selectedCard);
                player.sendMessage(ChatColor.YELLOW + "You have bought " + selectedCard + " card for "+Cards.CardCost2+" coins\nIt has been selected");
                data.Coins = data.Coins - Cards.CardCost2;
                data.Card = selectedCard;
                datafiles.SetData(data);
            }
            if (event.getRawSlot() == 32) {
                ItemStack item = new Wool(DyeColor.GRAY).toItemStack(1);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.BLUE + "Click for Buy Mode");
                    item.setItemMeta(meta);
                }
                player.openInventory(Inventories.cardsInventory(player));
                player.setMetadata("OpenedMenu4", new FixedMetadataValue(main.plugin, "Card Selection"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void BuyItemFromShop(Player player, InventoryClickEvent event) {
        event.setCancelled(true);

        DataFiles datafiles = new DataFiles(player);
        PlayerData data = datafiles.data;

        if (event.getInventory().getItem(event.getSlot()).getItemMeta().getLore().get(0).equals("Already bought")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "You already bought this!");
            return;
        }

        if (0 <= event.getRawSlot() && event.getRawSlot() <= 17 && !(event.getInventory().getItem(event.getSlot()).getItemMeta().getLore().get(0).equals("Already bought"))) {
            player.closeInventory();
            int Cost = Integer.valueOf(ChatColor.stripColor(event.getInventory().getItem(event.getSlot()).getItemMeta().getLore().get(0)));
            String currentSelection = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if (data.Coins<Cost) {player.sendMessage(ChatColor.YELLOW + "You don't have enough money to buy this");return;}
            
            data.Owned.add(currentSelection);
            player.sendMessage(ChatColor.YELLOW + "You have bought " + currentSelection + " for " + Cost + " coins");
            data.Coins = data.Coins - Cost;
            datafiles.SetData(data);
        }
    }

    public static void TeleportToMap(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (0 <= event.getRawSlot() && event.getRawSlot() <= (new VoteMap().mapSize()-1)) {
            player.closeInventory();
            String mapName = event.getCurrentItem().getItemMeta().getDisplayName();
            player.sendMessage(ChatColor.GREEN + "You will be teleported to "+mapName+"\nUse /lobby to teleport back");
            if (!new Multiverse().LoadWorld(mapName)) {
                player.sendMessage(ChatColor.RED+"The world is not available");
                return;
            }

            player.teleport(new Multiverse().GetWorld(mapName).getSpawnLocation());
            if (player.getGameMode() != GameMode.CREATIVE || !player.isOp()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, () -> {
                    player.setGameMode(GameMode.SPECTATOR);
                }, 3);
            }
        } else if (event.getRawSlot() == 14) {
            player.closeInventory();
            Bukkit.dispatchCommand(player, "lobby");
        }
    }
}
