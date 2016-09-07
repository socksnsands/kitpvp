package org.kitpvp.market;

import java.util.Date;

import org.bukkit.entity.Player;

public class MarketedItem {

	private Player player;
	private int cost;
	private Object object;
	private Date created;
	private boolean cancelled = false;
	
	public MarketedItem(Player player, int cost, Object marketedObject){
		this.player = player;
		this.cost = cost;
		this.object = marketedObject;
		this.created = new Date();
	}
	
	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}
	
	public boolean isCancelled(){
		return this.cancelled;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public Object getObject(){
		return this.object;
	}
	
	public Date getCreatedDate(){
		return this.created;
	}
	
}
