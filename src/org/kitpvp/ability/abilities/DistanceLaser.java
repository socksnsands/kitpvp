package org.kitpvp.ability.abilities;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class DistanceLaser extends Ability {

	public DistanceLaser() {
		super("Distance Laser", "A laser not limited to distance!",
				Material.IRON_BARDING, Scarcity.RED, 10);
		super.setCooldown(20 * 20);
		super.setClickedItem(Material.IRON_BARDING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onInteract(Player player, Action action) {
		if (super.callEvent(player, this).isCancelled())
			return;
		super.putOnCooldown(player);
		player.getWorld().playSound(player.getLocation(),
				Sound.FIREWORK_LAUNCH, 1, 1);
		ArrayList<String> players = new ArrayList<String>();
		for (int i = 0; i < 400; i++) {
			Location l = player.getEyeLocation().getDirection().normalize()
					.multiply(i / 5).toLocation(player.getWorld());
			Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
			ParticleEffect.CRIT.display(0, 0, 0, 0, 1, loc, 200);
			if (i % 5 == 0) {
				for (Player p : player.getWorld().getPlayers()) {
					if (p != player
							&& p.getLocation().clone().add(0, 1, 0)
									.distance(loc) < 1) {
						if (!players.contains(p.getName())) {
							players.add(p.getName());
							Core.getInstance().getDamageManager()
									.damage(p, player, 7);
						}
					}
				}
			}
		}
	}

}
