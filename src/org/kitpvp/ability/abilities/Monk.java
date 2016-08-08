package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Monk extends Ability implements Listener {

	private static String name = "Monk";
	
	public Monk() {
		super(name, "Click a user to hide their sword!", Material.TRIPWIRE_HOOK, Scarcity.PURPLE, 10);
	}

	//TODO will finish later, got caught up on something else.
	
//	@EventHandler
//	public void onMonk(PlayerInteractEntityEvent event){
//		if(event.getRightClicked() instanceof Player){
//			Player player = event.getPlayer();
//			User user = Core.getInstance().getUserManager().getUser(player);
//			Player clickedPlayer = (Player) event.getRightClicked();
//			User clickedUser = Core.getInstance().getUserManager().getUser(clickedPlayer);
//			if(user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name))){
	
//			}
//		}
//	}

}
