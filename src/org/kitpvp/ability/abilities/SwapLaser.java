package org.kitpvp.ability.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class SwapLaser extends Ability {

	private static String name = "Swap Laser";

	public SwapLaser() {
		super(name, "Shoot a beam swapping your health with that of your foe!", Material.BEACON, Scarcity.BLACK, 24, 1);
		super.setCooldown(20 * 70);
		super.setClickedItem(Material.COAL);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
			boolean ha = false;
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply((i / 5))
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.SPELL.display(0, 0, 0, 1, 1, loc, 200);
				if (!ha)
					if (i % 5 == 0) {
						for (Player p : player.getWorld().getPlayers()) {
							if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
								Damageable dm = p, dm2 = player;
								double health = dm.getHealth();
								p.setHealth(dm2.getHealth());
								player.setHealth(health);
								p.getWorld().playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1, 1);
								ha = true;
								break;
							}
						}
					}
			}
		}
	}

}
