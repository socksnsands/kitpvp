package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class ElectricLaser extends Ability {

	public ElectricLaser(int level) {
		super("Electric Laser", "Shoot a beam of electricity _L_shocking hit players for _H" + (1+ (level*2)) + "H_ seconds._L_ Has a cooldown of _H12H_ seconds.", Material.GOLD_AXE, Scarcity.RED, 12 + (level*2), level);
		super.setCooldown(20 * 12);
		super.setClickedItem(Material.STICK);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, super.getAbility()).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, loc, 200);
				if (i % 5 == 0) {
					for (Player p : player.getWorld().getPlayers()) {
						if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
							if (!players.contains(p.getName())) {
								players.add(p.getName());
								for (int sh = 0; sh < 25; sh++) {
									Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(),
											new Runnable() {

												@Override
												public void run() {
													p.playEffect(EntityEffect.HURT);
												}

											}, sh * 2);
								}
								Core.getInstance().getDamageManager().damage(p, player, 4);
							}
						}
					}
				}
			}
		}
	}

}
