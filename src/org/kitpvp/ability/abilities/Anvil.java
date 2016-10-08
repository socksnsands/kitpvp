package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Anvil extends Ability implements Listener {

	public Anvil() {
		super("Anvil", "Deal no knockback to players and receive no knockback.", Material.ANVIL, Scarcity.RED, 12, 1);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			Player player = (Player) event.getDamager();
			if(!(event.getEntity() instanceof Player))
				return;
			Player d = (Player) event.getEntity();
			if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(this) || Core.getInstance().getUserManager().getUser(d).getActiveAbilities().contains(this)){
				if(!super.callEvent(player, this).isCancelled()){
					event.getEntity().setVelocity(new Vector(0, 0, 0));
				}
			}
		}
	}

}
