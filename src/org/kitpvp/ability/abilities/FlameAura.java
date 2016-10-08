package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.AbilityUseEvent;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.ParticleEffect;

public class FlameAura extends Ability {

	public FlameAura() {
		super("Flame Aura", "Burn nearby players every 7 seconds!", Material.FIREWORK_CHARGE, Scarcity.PURPLE, 8, 1);
		this.startFlameAura();
	}

	private void startFlameAura() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {

			@Override
			public void run() {
				if (Core.getInstance().getUserManager().getUsers() != null)
					for (User user : Core.getInstance().getUserManager().getUsers()) {
						if (user.getActiveAbilities()
								.contains(Core.getInstance().getAbilityManager().getAbility("Flame Aura"))) {
							if (!getEvent(user.getPlayer()).isCancelled()) {
								for (Entity entity : user.getPlayer().getNearbyEntities(5, 5, 5)) {
									if (entity instanceof Player) {
										Player player = (Player) entity;
										User u = Core.getInstance().getUserManager().getUser(player);
										if (!u.isSafe()) {
											Random random = new Random();
											ParticleEffect.FLAME.display(3, 3, 3, 3, 2, player.getLocation(), 200);
											if (random.nextInt(4) == 0) {
												player.setFireTicks(20 * 3);
											}
										}
									}
								}
							}
						}
					}
			}

		}, 7 * 20, 7 * 20);
	}

	private AbilityUseEvent getEvent(Player player) {
		return super.callEvent(player, this);
	}

}
