package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Trampoline extends Ability implements Listener {

	private static String name = "Trampoline";
	
	public Trampoline() {
		super(name, "Every time you hit the ground you bounce back up!", Material.SPONGE, Scarcity.BLUE, 8);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
			Player player = event.getPlayer();
			User user = Core.getInstance().getUserManager().getUser(player);
			if(!user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name)))
				return;
			if(!player.getLocation().clone().add(0,-1,0).getBlock().getType().equals(Material.AIR) && !player.getLocation().clone().add(0,-1,0).getBlock().isLiquid()){
				if(player.getFallDistance() > 8){
				if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()){
					player.setVelocity(player.getVelocity().multiply(-.5));
				}
				}
			}
	}
	
}
