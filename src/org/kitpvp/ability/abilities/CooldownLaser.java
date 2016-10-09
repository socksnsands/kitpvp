package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class CooldownLaser extends Ability {

	public CooldownLaser() {
		super("Cooldown Laser", "Shoot a beam that restarts cooldowns_L_ of any players hit by it._L_ Has a _H18H_ second cooldown.", Material.SLIME_BALL, Scarcity.GOLD, 10, 1);
		super.setCooldown(18 * 20);
		super.setClickedItem(Material.SLIME_BALL);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(super.getName())).isCancelled()) {
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.PORTAL, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
						.toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, loc, 200);
				if (i % 5 == 0) {
					for (Player p : player.getWorld().getPlayers()) {
						if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
							if (!players.contains(p.getName())) {
								players.add(p.getName());
								Core.getInstance().getUserManager().getUser(p).restartCooldowns();
								p.playEffect(EntityEffect.HURT);
								p.sendMessage(ChatColor.GRAY + player.getName()
										+ " has restarted all of your cooldowns with their "
										+ super.getScarcity().getColor() + super.getName() + ChatColor.GRAY + "!");
							}
						}
					}
				}
			}
		}
	}

}
