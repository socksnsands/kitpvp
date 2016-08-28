package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Monkey extends Ability implements Listener {

	private static String name = "Monkey";
	
	public Monkey() {
		super(name, "Press shift while next to a wall to throw yourself forward!", Material.VINE, Scarcity.DARK_RED, 13);
	}
	
	@EventHandler
	public void onShift(PlayerToggleSneakEvent event){
		if(Core.getInstance().getUserManager().getUser(event.getPlayer()).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name))){
			if(event.getPlayer().getLocation().clone().add(0,0,1).getBlock().getType().isSolid()
					|| event.getPlayer().getLocation().clone().add(0,0,-1).getBlock().getType().isSolid()
					|| event.getPlayer().getLocation().clone().add(1,0,0).getBlock().getType().isSolid()
					|| event.getPlayer().getLocation().clone().add(-1,0,0).getBlock().getType().isSolid()){
				if(!event.getPlayer().getLocation().getBlock().getType().equals(Material.WATER) && !event.getPlayer().getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)){
					event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
					event.getPlayer().setFallDistance(0);
				}
			}
		}
	}

}
