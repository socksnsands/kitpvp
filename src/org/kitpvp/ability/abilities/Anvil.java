package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Anvil extends Ability implements Listener {

	public Anvil() {
		super("Anvil", "Deal no knockback to players!", Material.ANVIL, Scarcity.RED, 12);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			Player player = (Player) event.getDamager();
			if(!(event.getEntity() instanceof Player))
				return;
			if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(this)){
				if(!super.callEvent(player, this).isCancelled()){
					Core.getInstance().getDamageManager().damage((Player)event.getEntity(), player, event.getDamage());
					event.setCancelled(true);
				}
			}
		}
	}

}
