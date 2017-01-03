package org.kitpvp.market;

import java.util.Date;

import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;

public class MarketedItem {

	private Player player;
	private int cost;
	private Ability object;
	private Date created;
	private boolean cancelled = false;
	
	public MarketedItem(Player player, int cost, Ability ability){
		this.player = player;
		this.cost = cost;
		this.object = ability;
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
	
	public Ability getAbility(){
		return this.object;
	}
	
	public Date getCreatedDate(){
		return this.created;
	}
	
}
