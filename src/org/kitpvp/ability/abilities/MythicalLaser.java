package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class MythicalLaser extends Ability {

	private static String name = "Mythical Laser";

	public MythicalLaser() {
		super(name, "Shoot a beam of magic!", Material.ENCHANTMENT_TABLE, Scarcity.RED, 13, 1);
		super.setCooldown(20 * 16);
		super.setClickedItem(Material.GOLD_HOE);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.SHEEP_SHEAR, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 0, 1, loc, 200);
				if (i % 5 == 0) {
					for (Player p : player.getWorld().getPlayers()) {
						if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
							if (!players.contains(p.getName())) {
								players.add(p.getName());
								Core.getInstance().getDamageManager().setVelocity(p, player.getLocation().getDirection().multiply(2).add(new Vector(0,.6,0)));
								Core.getInstance().getDamageManager().damage(p, player, 6);
							}
						}
					}
				}
			}
		}
	}

}
