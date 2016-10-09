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
		super("Anvil", "Deal no knockback to players_L_and receive no knockback.", Material.ANVIL, Scarcity.GOLD, 14, 1);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			Player player = (Player) event.getDamager();
			if(!(event.getEntity() instanceof Player))
				return;
			Player d = (Player) event.getEntity();
			if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Anvil")) || Core.getInstance().getUserManager().getUser(d).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Anvil"))){
				if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility("Anvil")).isCancelled()){
					event.setCancelled(true);
					Core.getInstance().getDamageManager().damage(d, player, event.getDamage());
					d.setVelocity(new Vector(0, -.01, 0));
				}
			}
		}
	}

}
