package com.manulife.pension.ireports.dao;

public class Footnote {

	private String symbol;
	private String text;
	private int orderNumber;

	public Footnote(String symbol, String symbolText) {
		this.symbol = symbol;
		this.text = symbolText;
	}
	
	public Footnote(String symbol, String symbolText,int orderNumber) {
		this.symbol = symbol;
		this.text = symbolText;
		this.orderNumber = orderNumber;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getText() {
		return text;
	}
	
	public int getOrderNumber(){
		return orderNumber;
	}

}
