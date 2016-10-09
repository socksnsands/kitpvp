package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class FireLaser extends Ability {

	private static String name = "Fire Laser";

	public FireLaser(int level) {
		super(name, "Shoot a beam of fire _L_lighting enemies on fire for _H" + (4 + (2*level)) + "H_ seconds _L_as well as dealing _H" + level + ".5H_ hearts of damage. _L_Has a cooldown of _H12H_ seconds.", Material.BLAZE_ROD, Scarcity.PURPLE, 10 + level*3, level);
		super.setCooldown(20 * 12);
		super.setClickedItem(Material.BLAZE_ROD);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.FIRE_IGNITE, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.FLAME.display(0, 0, 0, 0, 1, loc, 200);
				if (i % 5 == 0) {
					for (Player p : player.getWorld().getPlayers()) {
						if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
							if (!players.contains(p.getName())) {
								players.add(p.getName());
								p.setFireTicks(60 + (40*super.getLevel()));
								Core.getInstance().getDamageManager().damage(p, player, 1 + 2*super.getLevel());
							}
						}
					}
				}
			}
		}
	}

}
