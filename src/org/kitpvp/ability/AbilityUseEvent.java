package org.kitpvp.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AbilityUseEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private Ability ability;
	private boolean cancelled = false;
	
	public AbilityUseEvent(Player player, Ability ability){
		this.player = player;
		this.ability = ability;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Ability getAbility(){
		return this.ability;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
