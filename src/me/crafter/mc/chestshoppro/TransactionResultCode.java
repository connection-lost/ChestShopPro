package me.crafter.mc.chestshoppro;

public enum TransactionResultCode {
	
	BUY_SUCCESS, SELL_SUCCESS, BUY_SUCCESS_PORTION, SELL_SUCCESS_PORTION,
	
	BUY_FAILED_NOT_ENOUGH_MONEY, BUY_FAILED_SOLD_OUT,
	SELL_FAILED_NOT_ENOUGH_MONEY, SELL_FAILED_NOTHING_TO_SELL,
	
	BUY_FAILED_UNKNOWN, SELL_FAILED_UNKNOWN, 
	
	UNDEFINED

}
