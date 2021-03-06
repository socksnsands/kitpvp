package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Jedi extends Ability {

	private static String name = "Jedi";

	public Jedi() {
		super(name, "Push back anyone close!", Material.DRAGON_EGG, Scarcity.PURPLE, 8, 1);
		super.setClickedItem(Material.COAL);
		super.setCooldown(20 * 22);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!super.callEvent(player, this).isCancelled()) {
				for (Player p : player.getWorld().getPlayers()) {
					if (p.getLocation().distance(player.getLocation()) < 5 && p != player) {
						Core.getInstance().getDamageManager().setVelocity(p, new Vector((p.getLocation().getX() - player.getLocation().getX()), .7,
								(p.getLocation().getZ() - player.getLocation().getZ())));
					}
				}
				player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
				super.putOnCooldown(player);
			}
		}
	}

}
