package me.crafter.mc.chestshoppro;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.apache.commons.codec.binary.Base64;

public class Utils {

	public static boolean ok = false;
	public static Economy economy = null;
	public static BlockFace allowedfaces[] = {BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST, BlockFace.UP};
	
	public Utils(){
		setupEconomy();
	}
	
	public static String serialize(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", itemStack);
        return new String(Base64.encodeBase64(config.saveToString().getBytes()));
    }
	
	public static ItemStack deserialize(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.decodeBase64(stringBlob)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("item", null);
    }
	
	public static String itemToShopString(ItemStack itemstack){
		itemstack = itemstack.clone();
		itemstack.setAmount(1);
		if (itemstack == null || itemstack.getType() == Material.AIR) return "";
		String itemname = toTitleCase(itemstack.getType().name());
		short durability = itemstack.getDurability();
		// Check special item
		ItemStack potentialcopy = new ItemStack(itemstack.getType());
		potentialcopy.setDurability(durability);
		if (itemstack.equals(potentialcopy)){
			if (durability != 0){
				itemname += ":" + durability;
			}
		} else {
			itemname += "#";
			itemname += serialize(itemstack);
		}
		return itemname;
	}
	
	public static ItemStack shopStringToItem(String shopstring){
		ItemStack itemstack;
		if (shopstring.contains("#")){ // If line3 is a special item
			itemstack = Utils.deserialize(shopstring.split("#", 2)[1]);
			if (itemstack == null){ return null; }
			itemstack.setAmount(1);
		} else { // If line3 is a normal item
			short durability = 0;
			if (shopstring.contains(":")){ // If :data exists
				try {
					durability = Short.parseShort(shopstring.split(":", 2)[1]);
				} catch (Exception ex){ return null; }
				shopstring = shopstring.split(":", 2)[0]; // temp workaround
			}
			try {
				Material material = Material.valueOf(shopstring.toUpperCase().replace(" ", "_"));
				if (material == null) return null;
				itemstack = new ItemStack(material);
				itemstack.setDurability(durability);
			} catch (Exception ex){ return null; } // error check anyways
		}
		return itemstack;
	}
	
	public static boolean consistencyTest(Player player){
		String deserialized = itemToShopString(player.getInventory().getItemInMainHand());
		ItemStack item2 = shopStringToItem(deserialized);
		return player.getInventory().getItemInMainHand().equals(item2);
	}
	
	public static void consistencyCopy(Player player){
		String deserialized = itemToShopString(player.getInventory().getItemInMainHand());
		ItemStack item2 = shopStringToItem(deserialized);
		player.getInventory().addItem(item2);
	}
	
	public static boolean areSameItem(ItemStack item1, ItemStack item2){
		if (item1 == null || item2 == null) return item1 == item2;
		// Book workaround
		if (item1.getType() == Material.WRITTEN_BOOK && item2.getType() == Material.WRITTEN_BOOK){
			BookMeta book1 = (BookMeta)item1.getItemMeta();
			BookMeta book2 = (BookMeta)item2.getItemMeta();
			if (book1.hasAuthor() && book1.getAuthor().equals(book2.getAuthor()) && 
					book1.hasTitle() && book1.getTitle().equals(book2.getTitle()) && 
					book1.hasDisplayName() && book1.getDisplayName().equals(book2.getDisplayName())){
				return true;
			} else {
				return false;
			}
		}
		return item1.equals(item2);
	}
	
	public void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        economy = rsp.getProvider();
        if (economy != null) ok = true;
	}
	
	public static String toTitleCase(String givenString){
		givenString = givenString.toLowerCase().replace("_", " ");
		try {
		    String[] arr = givenString.split(" ");
		    StringBuffer sb = new StringBuffer();

		    for (int i = 0; i < arr.length; i++) {
		        sb.append(Character.toUpperCase(arr[i].charAt(0)))
		            .append(arr[i].substring(1)).append(" ");
		    }          
		    return sb.toString().trim();
		} catch (Exception ex){
			return "?";
		}
	}
	
	public static boolean playerHasMoney(OfflinePlayer player, double request){
		if (player == null) return false;
		return economy.getBalance(player) >= request;
	}
	
	public static boolean playerRemoveMoney(OfflinePlayer player, double request){
		if (player == null) return false;
		return economy.withdrawPlayer(player, request).type == EconomyResponse.ResponseType.SUCCESS;
	}
	
	public static boolean playerAddMoney(OfflinePlayer player, double request){
		if (player == null) return false;
		return economy.depositPlayer(player, request).type == EconomyResponse.ResponseType.SUCCESS;
	}
	
	public static boolean inventoryHasItem(Inventory inventory, ItemStack itemstack, int amount){
		int currentamount = 0;
		for (ItemStack content : inventory){
			if (content != null && content.isSimilar(itemstack)){
				currentamount += amount;
			}
		}
		return currentamount >= amount;
	}
	
	public static boolean inventoryRemoveItem(Inventory inventory, ItemStack itemstack, int amount){
		int remainingamount = amount;
		for (ItemStack content : inventory){
			if (content != null && content.isSimilar(itemstack)){
				if (content.getAmount() > remainingamount){
					content.setAmount(content.getAmount() - remainingamount);
					remainingamount = 0;
					break;
				} else if (content.getAmount() == remainingamount){
					inventory.remove(content);
					remainingamount = 0;
					break;
				} else {
					remainingamount -= content.getAmount();
					inventory.remove(content);
					break;
				}
			}
		}
		return remainingamount == 0;
	}
	
}

