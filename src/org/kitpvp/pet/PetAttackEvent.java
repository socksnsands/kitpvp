package org.kitpvp.pet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetAttackEvent extends Event implements Cancellable {

	private PetEntity attacker;
	private LivingEntity target;
	
	private boolean cancelled;
	
	public PetAttackEvent(PetEntity attacker, LivingEntity target){
		this.attacker = attacker;
		this.target = target;
	}
	
	public PetEntity getAttacker(){
		return this.attacker;
	}
	
	public LivingEntity getTarget(){
		return target;
	}
	
	private static final HandlerList handlers = new HandlerList();

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
