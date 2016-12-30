package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.ParticleEffect;

public class Aphrodite extends Ability implements Listener {

	public Aphrodite(int level) {
		super("Aphrodite", "_H" + level + "0%H_ of damage dealt to you _L_(from any cause) will heal you instead _L_of damage you.", Material.RED_ROSE, Scarcity.RED, 11+(level*3), level);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			User user = Core.getInstance().getUserManager().getUser(player);
			if (user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(super.getName()))) {
				Random random = new Random();
				int r = random.nextInt(100 + 1);
				if (r >= 100-(10*super.getLevel())) {
					if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(super.getName()))
							.isCancelled()) {
						ParticleEffect.HEART.display(0, 0, 0, 0, 1, player.getLocation().clone().add(0, 1.5, 0), 200);
						player.getWorld().playSound(player.getLocation(), Sound.BURP, 1, 1);
						Damageable dm = player;
						if (dm.getMaxHealth() < dm.getHealth() + event.getDamage())
							dm.setHealth(dm.getMaxHealth());
						else
							dm.setHealth(dm.getHealth() + event.getDamage());
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
