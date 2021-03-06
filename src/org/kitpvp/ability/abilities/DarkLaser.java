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

public class DarkLaser extends Ability {

	private static String name = "Dark Laser";

	public DarkLaser(int level) {
		super(name, "Shoot a beam that blinds players_L_ for _H" + (level*3) + "H_ seconds as well_L_ as dealing _H1H_ heart of damage._L_ Has a cooldown of _H18H_ seconds.", Material.COAL_BLOCK, Scarcity.PURPLE, 8 + (level*3), level);
		super.setCooldown(20 * 18);
		super.setClickedItem(Material.STICK);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.SUSPENDED_DEPTH.display(0, 0, 0, 0, 1, loc, 200);
				if (i % 5 == 0) {
					for (Player p : player.getWorld().getPlayers()) {
						if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
							if (!players.contains(p.getName())) {
								players.add(p.getName());
								p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30*super.getLevel(), 0));
								Core.getInstance().getDamageManager().damage(p, player, 2);
							}
						}
					}
				}
			}
		}
	}

}
