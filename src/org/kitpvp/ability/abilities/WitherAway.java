package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.AbilityUseEvent;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.ParticleEffect;

public class WitherAway extends Ability {

	public WitherAway() {
		super("Wither Away", "Wither nearby players for 6 seconds!", Material.SKULL, Scarcity.RED, 12);
		this.startWitherAway();
	}

	private void startWitherAway() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {

			@Override
			public void run() {
				if (Core.getInstance().getUserManager().getUsers() != null)
					for (User user : Core.getInstance().getUserManager().getUsers()) {
						if (user.getActiveAbilities()
								.contains(Core.getInstance().getAbilityManager().getAbility("Wither Away"))) {
							if (!getEvent(user.getPlayer()).isCancelled()) {
								for (Entity entity : user.getPlayer().getNearbyEntities(7, 7, 7)) {
									if (entity instanceof Player) {
										Player player = (Player) entity;
										User u = Core.getInstance().getUserManager().getUser(player);
										if (!u.isSafe()) {
											Random random = new Random();
											ParticleEffect.SUSPENDED_DEPTH.display(3, 3, 3, 3, 5, player.getLocation(), 200);
											if (random.nextInt(2) == 0) {
												PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 6 * 20, 1);
												player.addPotionEffect(wither);
												ParticleEffect.DRAGONBREATH.display(0, 0, 0, 1, 3, player.getLocation(), 200);
											}
										}
									}
								}
							}
						}
					}
			}

		}, 8 * 20, 8 * 20);
	}

	private AbilityUseEvent getEvent(Player player) {
		return super.callEvent(player, this);
	}

}
