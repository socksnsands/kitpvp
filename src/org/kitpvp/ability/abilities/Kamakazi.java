package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.ParticleEffect;

public class Kamakazi extends Ability implements Listener {

	public Kamakazi(int level) {
		super("Kamakazi", "Create an explosion on death, dealing _H" + (level*6) + "H_ damage to anyone within _H4H_ blocks.", Material.TNT, Scarcity.BLUE, 7, level);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		User user = Core.getInstance().getUserManager().getUser(event.getEntity());
		if(user.getActiveAbilities().contains(super.getAbility())){
			ParticleEffect.EXPLOSION_HUGE.display(0, 0, 0, 0, 1, event.getEntity().getLocation(), 50);
			for(Entity en : event.getEntity().getNearbyEntities(4, 4, 4)){
				if(en instanceof Damageable){
					Damageable dm = (Damageable) en;
					dm.damage(super.getLevel()*6D);
				}
			}
		}
	}

}
