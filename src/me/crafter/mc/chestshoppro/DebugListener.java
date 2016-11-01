package me.crafter.mc.chestshoppro;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DebugListener implements Listener{
	
	@EventHandler
	public void onDebugClickSign(PlayerInteractEvent event){
		if (event.getPlayer().isSneaking()){
			if (event.getAction() == Action.LEFT_CLICK_BLOCK){
				Block b = event.getClickedBlock();
				if (ChestShopProAPI.isSign(b)){
					Sign sign = (Sign)b.getState();
					sign.setLine(3, Utils.itemToShopString(event.getPlayer().getInventory().getItemInMainHand()));
					sign.update();
				}				
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
				Block b = event.getClickedBlock();
				if (ChestShopProAPI.isSign(b)){
					event.getPlayer().getInventory().setItemInMainHand(Utils.shopStringToItem(((Sign)b.getState()).getLine(3)));
					ShopOffer offer = new ShopOffer(event.getClickedBlock());
					event.getPlayer().sendMessage(offer.toString());
				}
			}
		}
	}
	
	
	

}
