package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Ninja extends Ability implements Listener {

	private static String name = "Ninja";
	
	public Ninja() {
		super(name, "Deal 2x damage from behind!", Material.NETHER_STAR, Scarcity.GOLD, 12);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			if(event.getEntity() instanceof Player){
				Player damager = (Player) event.getDamager();
				Player hit = (Player) event.getEntity();
				if(Core.getInstance().getUserManager().getUser(damager).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name))){
					if(Math.abs(damager.getLocation().getPitch() - hit.getLocation().getPitch()) < 40){
						event.setDamage(event.getDamage()*2);
					}
				}
			}
		}
	}

}
