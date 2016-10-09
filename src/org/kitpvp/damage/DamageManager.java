package org.kitpvp.damage;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.kitpvp.core.Core;

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
	
	public void setVelocity(Player player, Vector velocity){
		if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Anvil")))
			return;
		player.setVelocity(velocity);
	}
	
	public void damage(Player damaged, Player damager, double amount) {
		this.setLastDamaged(damaged, damager);
		if(amount != -1)
			damaged.damage(amount);
	}

	public void setLastDamaged(Player damaged, Player damager) {
		this.lastDamaged.put(damaged, damager);
	}

}
