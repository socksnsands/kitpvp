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

	public FlameAura(int level) {
		super("Flame Aura", "25% chance to burn nearby_L_ (_H5H_ blocks) players for _H3H_ seconds _L_every _H" + (7-(2*level)) + "H_ seconds.", Material.FIREWORK_CHARGE, Scarcity.PURPLE, 8+(level*3), level);
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
											if (random.nextInt(4) == 0) {
												player.setFireTicks(20 * 3);
												ParticleEffect.FLAME.display(3, 3, 3, 0, 2, player.getLocation().clone().add(0,1.5,0), 200);
											}
										}
									}
								}
							}
						}
					}
			}

		}, (7-(2*super.getLevel())) * 20, (7-(2*super.getLevel())) * 20);
	}

	private AbilityUseEvent getEvent(Player player) {
		return super.callEvent(player, this);
	}

}
