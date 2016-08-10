package org.kitpvp.damage;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class DamageManager {

	private HashMap<Player, Player> lastDamaged = new HashMap<Player, Player>();

	public DamageManager() {

	}

	public Player getLastDamaged(Player player) {
		if (lastDamaged.containsKey(player))
			return lastDamaged.get(player);
		return null;
	}

	public boolean hasLastDamaged(Player player) {
		if (lastDamaged.containsKey(player))
			return true;
		return false;
	}

	public void damage(Player damaged, Player damager, double amount) {
		damaged.damage(amount);
		this.setLastDamaged(damaged, damager);
	}

	public void setLastDamaged(Player damaged, Player damager) {
		this.lastDamaged.put(damaged, damager);
	}

}
