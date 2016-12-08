package me.crafter.mc.chestshoppro;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class PlayerShoppingListener implements Listener {
	
	@EventHandler
	public void onPlayerBuy(PlayerInteractEvent event){
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && ChestShopProAPI.isShopSign(event.getClickedBlock())){
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			ShopOffer shopoffer = ShopOffer.fromBlock(block);
			// Check can buy
			if (!shopoffer.canBuy()){
				player.sendMessage(ChatColor.RED + "ERROR: You cannot buy here!");
				return;
			}
			// Check shop content ok
			if (!shopoffer.adminshop){
				if (!Utils.inventoryHasItem(shopoffer.chestinventory, shopoffer.itemstack, shopoffer.amount)){
					player.sendMessage(ChatColor.RED + "ERROR: This shop is out of stock!");
					return;
				}
			}
			// Check money ok
			if (!Utils.playerHasMoney(player, shopoffer.amount * shopoffer.buyprice)){
				player.sendMessage(ChatColor.RED + "ERROR: You don't have enough money!" );
				return;
			}
			// TODO check player empty space
			
			// Remove money from player
			if (!Utils.playerRemoveMoney(player, shopoffer.amount * shopoffer.buyprice)){
				player.sendMessage(ChatColor.RED + "ERROR: Unknown error!" );
				return;
			}
			// Remove item from shop
			if (!shopoffer.adminshop){
				Utils.inventoryRemoveItem(shopoffer.chestinventory, shopoffer.itemstack, shopoffer.amount);
			}
			// Give player item
			ItemStack give = shopoffer.itemstack.clone();
			give.setAmount(shopoffer.amount);
			player.getInventory().addItem(give);
			// Give shop money
			if (!shopoffer.adminshop){
				Utils.playerAddMoney(shopoffer.getPlayer(), shopoffer.amount * shopoffer.buyprice);
			}

			player.sendMessage(ChatColor.GREEN + "You bought " + Utils.toTitleCase(shopoffer.itemstack.getType().name()) + "*" + shopoffer.amount + " from " + shopoffer.getPlayer().getName());
			if (shopoffer.getPlayer().isOnline()){
				((Player)shopoffer.getPlayer()).sendMessage(ChatColor.GREEN + player.getName() + " bought " + Utils.toTitleCase(shopoffer.itemstack.getType().name()) + "*" + shopoffer.amount + " from you.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerSell(PlayerInteractEvent event){
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && ChestShopProAPI.isShopSign(event.getClickedBlock())){
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			ShopOffer shopoffer = ShopOffer.fromBlock(block);
			// Check can sell
			if (!shopoffer.canSell()){
				player.sendMessage(ChatColor.RED + "ERROR: You cannot sell here!");
				return;
			}
			// Check shop content ok
			if (!Utils.inventoryHasItem(player.getInventory(), shopoffer.itemstack, shopoffer.amount)){
				player.sendMessage(ChatColor.RED + "ERROR: You don't have enough item to sell!");
				return;
			}
			// Check money ok
			if (!shopoffer.adminshop){
				if (!Utils.playerHasMoney(shopoffer.getPlayer(), shopoffer.amount * shopoffer.sellprice)){
					player.sendMessage(ChatColor.RED + "ERROR: The seller doesn't have enough money!" );
					return;
				}
			}
			// TODO check shop empty space
			
			// Remove item from player
			Utils.inventoryRemoveItem(player.getInventory(), shopoffer.itemstack, shopoffer.amount);
			// Remove money from shop
			if (!shopoffer.adminshop){
				Utils.playerRemoveMoney(shopoffer.getPlayer(), shopoffer.amount * shopoffer.sellprice);
			}
			// Give player money
			Utils.playerAddMoney(player, shopoffer.amount * shopoffer.sellprice);
			// Give shop item
			if (!shopoffer.adminshop){
				ItemStack give = shopoffer.itemstack.clone();
				give.setAmount(shopoffer.amount);
				shopoffer.chestinventory.addItem(give);
			}

			player.sendMessage(ChatColor.GREEN + "You sold " + Utils.toTitleCase(shopoffer.itemstack.getType().name()) + "*" + shopoffer.amount + " to " + shopoffer.getPlayer().getName());
			if (shopoffer.getPlayer().isOnline()){
				((Player)shopoffer.getPlayer()).sendMessage(ChatColor.GREEN + player.getName() + " sold " + Utils.toTitleCase(shopoffer.itemstack.getType().name()) + "*" + shopoffer.amount + " to you.");
			}
		}
	}

}
