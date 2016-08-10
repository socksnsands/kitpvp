package org.kitpvp.ability.abilities;

import java.util.ArrayList;

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

public class ParalysisLaser extends Ability {

	private static String name = "Paralysis Laser";

	public ParalysisLaser() {
		super(name, "Shoot a beam of paralyzing dust!", Material.EMERALD_BLOCK, Scarcity.DARK_RED, 15);
		super.setCooldown(20 * 12);
		super.setClickedItem(Material.STICK);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_DEATH, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 1, loc, 200);
				if (i % 5 == 0) {
					for (Player p : player.getWorld().getPlayers()) {
						if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
							if (!players.contains(p.getName())) {
								players.add(p.getName());
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 6));
								p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
								p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 50, -5));
								Core.getInstance().getDamageManager().damage(p, player, 4);
							}
						}
					}
				}
			}
		}
	}

}
