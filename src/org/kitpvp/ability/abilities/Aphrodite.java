package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.ParticleEffect;

public class Aphrodite extends Ability implements Listener {

	private static String name = "Aphrodite";

	public Aphrodite() {
		super(name, "10% of attacks heal you!", Material.RED_ROSE, Scarcity.RED, 14);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			User user = Core.getInstance().getUserManager().getUser(player);
			if (user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name))) {
				Random random = new Random();
				int r = random.nextInt(10);
				if (r == 7) {
					if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name))
							.isCancelled()) {
						ParticleEffect.HEART.display(0, 0, 0, 0, 1, player.getLocation().clone().add(0, 1.5, 0), 200);
						player.getWorld().playSound(player.getLocation(), Sound.BURP, 1, 1);
						if (player.getMaxHealth() < player.getHealth() + event.getDamage())
							player.setHealth(player.getMaxHealth());
						else
							player.setHealth(player.getHealth() + event.getDamage());
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
