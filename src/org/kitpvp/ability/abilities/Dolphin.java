package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Dolphin extends Ability implements Listener {

	private static String name = "Dolphin";
	
	public Dolphin() {
		super(name, "Press shift while in water to throw yourself forward!", Material.WATER_BUCKET, Scarcity.RED, 9, 1);
	}
	
	@EventHandler
	public void onShift(PlayerToggleSneakEvent event){
		if(Core.getInstance().getUserManager().getUser(event.getPlayer()).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name))){
				if(event.getPlayer().getLocation().getBlock().getType().equals(Material.WATER) || event.getPlayer().getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)){
					event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
					event.getPlayer().setFallDistance(0);
				}
		}
	}

}
