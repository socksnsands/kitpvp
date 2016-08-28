package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class OmegaLaser extends Ability {
	// THIS CLASS IS REALLY INNEFFICIENT!

	public OmegaLaser() {
		super("Omega Laser", "Rapidly Fire Energy Lasers!", Material.NETHER_STAR, Scarcity.DARK_RED, 15);
		super.setCooldown(25 * 20);
		super.setClickedItem(Material.NETHER_STAR);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility("Omega Laser")).isCancelled()) {
			super.putOnCooldown(player);
			ArrayList<String> players = new ArrayList<String>();

			for (int i = 0; i < 5; i++) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
					public void run() {
						for (int i = 0; i < 50; i++) {

							Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
									.toLocation(player.getWorld());
							Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
							ParticleEffect.SPELL.display(0, 0, 0, 1, 1, loc, 200);
							if (i % 5 == 0) {
								for (Player p : player.getWorld().getPlayers()) {
									if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
										if (!players.contains(p.getName())) {
											Core.getInstance().getDamageManager().damage(p, player, 3);
											player.getWorld().playSound(player.getLocation(),
													Sound.ORB_PICKUP, 1, 1);
										}
									}
								}
							}
						}
					}
				}, i * 10);
			}

		}
	}
}
