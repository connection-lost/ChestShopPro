package me.crafter.mc.chestshoppro;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopOffer {

	private boolean valid = false;
	public boolean canbuy = false;
	public boolean cansell = false;
	public int amount = 1;
	public double buyprice = 0;
	public double sellprice = 0;
	public Material material = null;
	public ItemStack itemstack = null;
	public String playername = null;
	
	public boolean adminshop = false;
	Block chest = null;
	
	private ShopOffer(Block block){
		if (ChestShopProAPI.isSign(block)){
			Sign sign = (Sign)block.getState();
			
			// 2nd line is amount
			String line1 = sign.getLine(1);
			try {
				amount = Integer.parseInt(line1);
				if (amount > 64 || amount < 1) return;
			} catch (Exception ex){ return; }
			
			// 3rd line is offer
			String line2 = sign.getLine(2);
			if (line2.contains(":")){
				// buy and sell
				String[] splitted = line2.split(":", 2);
				buyprice = buyInfo(splitted[0]);
				if (buyprice > 0.0001D) canbuy = true;
				sellprice = sellInfo(splitted[1]);
				if (sellprice > 0.0001D) cansell = true;
				if (!(canbuy && cansell)) return;
			} else {
				buyprice = buyInfo(line2);
				if (buyprice > 0.0001D) canbuy = true;
				sellprice = sellInfo(line2);
				if (sellprice > 0.0001D) cansell = true;
				if (!canbuy && !cansell) return;
			}
			
			// 4th line is item
			String line3 = sign.getLine(3);
			if (line3 == null || line3.equals("")) return;
			itemstack = Utils.shopStringToItem(line3);
			if (itemstack == null) return;
			material = itemstack.getType();
			
			// 1st line is name
			String line0 = sign.getLine(0);
			if (line0 == null || line0.equals("") || line0.length() < 3 || line0.length() > 16) return;
			playername = line0;
			if (line0.equals("admin-shop")){
				adminshop = true;
			}
			
			// Not admin shop, find chest
			if (!adminshop){
				for (BlockFace blockface : Utils.allowedfaces){
					Block suspect = block.getRelative(blockface);
					if (suspect != null){
						switch (suspect.getType()){
						case CHEST:
						case TRAPPED_CHEST:
							chest = suspect;
							break;
						default:
							break;
						}
					}
					if (chest != null) break;
				}
				// No chest found
				if (chest == null) return;
			}
			
			// Get here means ok
			valid = true;
		}
	}
	
	public static double buyInfo(String input){
		if (input.startsWith("B ") && input.length() > 2){
			try {
				return Double.parseDouble(input.substring(2));
			} catch (Exception ex) {return 0;}
		}
		if (input.endsWith(" B") && input.length() > 2){
			try {
				return Double.parseDouble(input.substring(0, input.length() - 2));
			} catch (Exception ex) {return 0;}
		}
		return 0;
	}
	
	public static double sellInfo(String input){
		if (input.startsWith("S ") && input.length() > 2){
			try {
				return Double.parseDouble(input.substring(2));
			} catch (Exception ex) {return 0;}
		}
		if (input.endsWith(" S") && input.length() > 2){
			try {
				return Double.parseDouble(input.substring(0, input.length() - 2));
			} catch (Exception ex) {return 0;}
		}
		return 0;
	}
	
	public boolean buy(Player player){
		if (!valid) return false;
		// Check player money
		return false;
	}
	
	public boolean sell(Player player){
		if (!valid) return false;
		
		
		return false;
	}
	
	@Override
	public String toString(){
		String ret = "ChestShopPro";
		ret += "\nvalid:" + valid;
		ret += "\ncanbuy:" + canbuy;
		ret += "\ncansell:" + cansell;
		ret += "\namount:" + amount;
		ret += "\nbuyprice:" + buyprice;
		ret += "\nsellprice:" + sellprice;
		ret += "\nmaterial:" + material;
		ret += "\nitemstack:" + itemstack;
		return ret;
	}
	
	public static ShopOffer fromBlock(Block block){
		ShopOffer shopoffer = new ShopOffer(block);
		if (shopoffer.isValid()) return shopoffer;
		else return null;
	}
	
	public boolean isValid(){
		return valid;
	}
	
}
