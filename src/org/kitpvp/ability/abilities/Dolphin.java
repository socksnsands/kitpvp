package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Dolphin extends Ability implements Listener {
	
	public Dolphin() {
		super("Dolphin", "Press shift while in liquid _L_to throw yourself forward!", Material.WATER_BUCKET, Scarcity.RED, 9, 1);
	}
	
	@EventHandler
	public void onShift(PlayerToggleSneakEvent event){
		if(Core.getInstance().getUserManager().getUser(event.getPlayer()).getActiveAbilities().contains(super.getAbility())){
				if(event.getPlayer().getLocation().getBlock().isLiquid()){
					event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
				}
		}
	}

}
