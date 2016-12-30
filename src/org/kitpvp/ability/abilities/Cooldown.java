package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.AbilityUseEvent;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Cooldown extends Ability {

	public Cooldown(int level) {
		super("Cooldown", "Cuts all cooldowns down by _H" + (level+1) + "0%H_.", Material.GOLD_INGOT, Scarcity.DARK_RED, 8 + (level*2), level);
	}
	
	@EventHandler
	public void onUse(AbilityUseEvent event){
		super.callEvent(event.getPlayer(), Core.getInstance().getAbilityManager().getAbility("Cooldown"));
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
		if(user.getRemainingCooldown(event.getAbility()) != 0){
			user.setCooldown(event.getAbility(), user.getRemainingCooldown(event.getAbility()) * (1-((this.getLevel()+1)/10)));
		}
	}

}
