package org.kitpvp.market;

import java.util.ArrayList;

public class MarketedItemManager {

	private ArrayList<MarketedItem> marketedItems = new ArrayList<>();
	
	public MarketedItemManager(){
		
	}
	
	public ArrayList<MarketedItem> getMarketedItems(){ 
		return this.marketedItems;
	}
	
	public void addMarketedItem(MarketedItem marketedItem){
		if(!this.marketedItems.contains(marketedItem))
			this.marketedItems.add(marketedItem);
	}
	
	public void removeMarketedItem(MarketedItem marketedItem){
		if(this.marketedItems.contains(marketedItem))
			this.marketedItems.remove(marketedItem);
	}
	
}
