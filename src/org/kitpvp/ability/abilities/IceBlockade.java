package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;

public class IceBlockade extends Ability {

	private static String name = "Ice Blockade";
	
	public IceBlockade() {
		super(name, "Spawn a sphere of ice!", Material.PACKED_ICE, Scarcity.PURPLE, 9);
		super.setClickedItem(Material.STICK);
		super.setCooldown(20*36);
		//TODO make ability
	}

}
