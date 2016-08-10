package org.kitpvp.pet;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.kitpvp.unlockable.Unlockable;

public abstract class Pet extends Unlockable implements Listener {

	private int health;
	private EntityType entityType;

	public Pet(String name, String description, Scarcity scarcity, EntityType entityType) {
		super(name, description, scarcity);

		this.health = 50;
		this.entityType = entityType;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return this.health;
	}

	public EntityType getEntityType() {
		return this.entityType;
	}

}
