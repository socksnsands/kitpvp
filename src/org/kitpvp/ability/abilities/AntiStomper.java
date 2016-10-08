package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.kitpvp.ability.Ability;

public class AntiStomper extends Ability {
	
	public AntiStomper(int level) {
		super("Anti-Stomper", "When you get stomped, the stomper gets damaged _H" + (level == 1 ? "1/2" : "all") + "H_ stomped damage", Material.IRON_BOOTS, Scarcity.WHITE, 3+level, level);
	}

}
