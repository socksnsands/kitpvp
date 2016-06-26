package org.kitpvp.pet;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.kitpvp.unlockable.Unlockable;

public abstract class Pet extends Unlockable implements Listener {

	private String name;
	private int health;
	private EntityType entityType;
	
	public Pet(String name, Scarcity scarcity, EntityType entityType) {
		super(scarcity);
		
		this.name = name;
		this.health = 50;
		this.entityType = entityType;
	}
		
	public void setName(String name){
		this.name = name;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public EntityType getEntityType(){
		return this.entityType;
	}
	
}
