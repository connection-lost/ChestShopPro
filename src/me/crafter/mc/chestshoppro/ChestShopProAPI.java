package me.crafter.mc.chestshoppro;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class ChestShopProAPI {
	
	public static BlockFace[] newsfaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	public static BlockFace[] allfaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
	
	public static boolean isLocked(Block block){
		switch (block.getType()){
		case CHEST:
		case TRAPPED_CHEST:
			// Check first chest sign
			if (isLockedSingleBlock(block, null)) return true; 
			// Check second chest sign
			for (BlockFace chestface : newsfaces){
				Block relativechest = block.getRelative(chestface);
				if (relativechest.getType() == block.getType()){
					if (isLockedSingleBlock(relativechest, chestface.getOppositeFace())) return true;
				}
			}
		default:
			break;
		}
		return false;
	}
	
	public static boolean isOwner(Block block, Player player){
		switch (block.getType()){
		case CHEST:
		case TRAPPED_CHEST:
			// Check first chest sign
			if (isOwnerSingleBlock(block, null, player)) return true; 
			// Check second chest sign
			for (BlockFace chestface : newsfaces){
				Block relativechest = block.getRelative(chestface);
				if (relativechest.getType() == block.getType()){
					if (isOwnerSingleBlock(relativechest, chestface.getOppositeFace(), player)) return true;
				}
			}
		default:
			break;
		}
		return false;
	}
	
	public static boolean isLockedSingleBlock(Block block, BlockFace exempt){
		for (BlockFace blockface : newsfaces){
			if (blockface == exempt) continue;
			Block relativeblock = block.getRelative(blockface);
			if (isShopSign(relativeblock)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isOwnerSingleBlock(Block block, BlockFace exempt, Player player){ // Requires isLocked
		for (BlockFace blockface : newsfaces){
			if (blockface == exempt) continue;
			Block relativeblock = block.getRelative(blockface);
			if (isShopSign(relativeblock)){
				if (isOwnerOnSign(relativeblock, player)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static boolean isSign(Block block){
		switch (block.getType()){
		case SIGN:
		case SIGN_POST:
		case WALL_SIGN:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isShopSign(Block block){
		if (!isSign(block)) return false;
		ShopOffer shopoffer = ShopOffer.fromBlock(block);
		return shopoffer != null;
	}
	
	public static boolean isBuySign(Block block){
		
		return false;
	}
	
	public static boolean isOwnerOnSign(Block block, Player player){
		return isSign(block) && ((Sign)block.getState()).getLine(0).equals(player.getName());
	}
	
}
