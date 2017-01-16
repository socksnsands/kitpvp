package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Fortified extends Ability implements Listener {

	public Fortified() {
		super("Fortified", "Take 0.5 less damage for each nearby (15 block) enemy over 1.", Material.IRON_BLOCK, Scarcity.EVENT, 8, 1);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Fortified"))){
				int numb = 0;
				for(Entity en : player.getNearbyEntities(15, 15, 15)){
					if(en instanceof Player){
						numb++;
					}
				}
				if(numb >= 2){
					numb--;
					event.setDamage(event.getDamage()-numb);
				}
			}
		}
	}

}
