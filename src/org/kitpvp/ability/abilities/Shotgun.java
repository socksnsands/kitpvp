package org.kitpvp.ability.abilities;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Shotgun extends Ability implements Listener {

	public Shotgun() {
		super("Shotgun", "Knockback foes with a powerful shotgun!", Material.ARROW, Scarcity.DARK_RED, 8, 1);
		super.setCooldown(20*2);
		super.setClickedItem(Material.STONE_SPADE);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!super.callEvent(player, this).isCancelled()) {
				for (int i = 0; i < 5; i++) {
					Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
					arrow.setCustomName("shotgun_shot");
					Vector velocity = player.getLocation().getDirection();
					double accuracy = .1;
					velocity.add(new Vector(Math.random() * accuracy - accuracy, Math.random() * accuracy - accuracy,
							Math.random() * accuracy - accuracy));
					arrow.setVelocity(velocity);
				}
				super.putOnCooldown(player);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getCustomName().equals("shotgun_shot")) {
				if (event.getEntity() instanceof Player) {
					Player le = (Player) event.getEntity();
					if(arrow.getShooter() instanceof Player)
						Core.getInstance().getDamageManager().damage(le, (Player)arrow.getShooter(), -1);
					if (le.getHealth() > 1)
						le.setHealth(le.getHealth() - 3);
					le.playEffect(EntityEffect.HURT);
					le.setVelocity(arrow.getVelocity().setY(.4));
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onLand(ProjectileHitEvent event){
		if(event.getEntity() instanceof Arrow){
			if(event.getEntity().getCustomName().equals("shotgun_shot")){
				event.getEntity().remove();
			}
		}
	}

}
