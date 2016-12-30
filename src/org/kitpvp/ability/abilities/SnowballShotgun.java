package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;

public class SnowballShotgun extends Ability implements Listener {

	ArrayList<Snowball> snowballs = new ArrayList<>();
	
	public SnowballShotgun() {
		super("Snowball Shotgun", "Knockback foes with a powerful shotgun!", Material.GOLD_SPADE, Scarcity.RED, 8, 1);
		super.setCooldown(30);
		super.setClickedItem(Material.GOLD_SPADE);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!super.callEvent(player, this).isCancelled()) {
				for (int i = 0; i < 5; i++) {
					Snowball snowball = (Snowball) player.launchProjectile(Snowball.class);
					snowballs.add(snowball);
					Vector velocity = player.getLocation().getDirection();
					double accuracy = .1;
					velocity.add(new Vector(Math.random() * accuracy - accuracy, Math.random() * accuracy - accuracy,
							Math.random() * accuracy - accuracy));
					snowball.setVelocity(velocity);
				}
				super.putOnCooldown(player);
			}
		}
	}

	@EventHandler
	public void onHit(ProjectileHitEvent event){
		if(event.getEntity() instanceof Snowball){
			Snowball sb = (Snowball) event.getEntity();
			if(this.snowballs.contains(sb))
				this.snowballs.remove(sb);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Snowball) {
			Snowball snowball = (Snowball) event.getDamager();
			if (snowballs.contains(snowball)) {
				snowballs.remove(snowball);
				if (event.getEntity() instanceof LivingEntity) {
					LivingEntity le = (LivingEntity) event.getEntity();
					Damageable dm = le;
					if (dm.getHealth() > 1)
						le.setHealth(dm.getHealth() - 1);
					le.playEffect(EntityEffect.HURT);
					le.setVelocity(snowball.getVelocity());
				}
				event.setCancelled(true);
			}
		}
	}

}
