package me.crafter.mc.chestshoppro;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class ChestShopPro extends JavaPlugin {


	public void onEnable(){
		// Debug
		getServer().getPluginManager().registerEvents(new DebugListener(), this);
		Bukkit.getScheduler().runTaskTimer(this, new CashUtil(), 2, 2);
		
		// Config
		
		
		// Utils (including economy)
		new Utils();
		
		// Listener
		getServer().getPluginManager().registerEvents(new PlayerShoppingListener(), this);
		
		// Metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
    public void onDisable(){
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args){
    	if (cmd.getName().equals("chestshoppro")){
    		switch (args[0]){
    		case "tostring":
    			sender.sendMessage(Utils.itemToShopString(((Player)sender).getInventory().getItemInMainHand()));
    			break;
    		case "test":
    			sender.sendMessage("Test: " + Utils.consistencyTest(((Player)sender)));
    			break;
    		case "copy":
    			sender.sendMessage("Test: ");
    			Utils.consistencyCopy(((Player)sender));
    			break;
    		case "serialize":
    			sender.sendMessage(Utils.serialize(((Player)sender).getInventory().getItemInMainHand()));
    			break;
    		case "deserialize":
    			String str = Utils.serialize(((Player)sender).getInventory().getItemInMainHand());
    			((Player)sender).getInventory().addItem(Utils.deserialize(str));
    			break;
    		}	
    	}
    	return true;
    }
	
}
