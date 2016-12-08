package me.crafter.mc.chestshoppro;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.connorlinfoot.titleapi.TitleAPI;

import net.md_5.bungee.api.ChatColor;

public class CashUtil implements Runnable {

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()){
			TitleAPI.sendTitle(p, 0, 10, 0, " ", ChatColor.GREEN + p.getName() + "      $" + Utils.economy.getBalance(p));
		}
	}
}
