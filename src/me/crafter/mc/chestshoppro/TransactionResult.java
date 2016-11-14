package me.crafter.mc.chestshoppro;

import org.bukkit.inventory.ItemStack;

public class TransactionResult {
	
	TransactionResultCode resultcode = TransactionResultCode.UNDEFINED;
	ItemStack itemstack;
	int amount;
	int money;
	
	public TransactionResult(TransactionResultCode resultcode, ItemStack itemstack, int amount, int money){
		this.resultcode = resultcode;
		this.itemstack = itemstack;
		this.amount = amount;
		this.money = money;
	}
	
	
}