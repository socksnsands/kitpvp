package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.abilities.objects.JetObject;

public class Jet extends Ability {

	private static String name = "Jet";
	
	public Jet() {
		super(name, "Fly and masacre!", Material.MINECART, Scarcity.BLACK, 22);
		super.setClickedItem(Material.EMERALD);
		super.setCooldown(Integer.MAX_VALUE);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(!super.callEvent(player, this).isCancelled()){
				super.putOnCooldown(player);
				JetObject jo = new JetObject(player);
			}
		}
	}

}
