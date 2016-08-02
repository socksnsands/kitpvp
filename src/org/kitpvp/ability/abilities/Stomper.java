package org.kitpvp.ability.abilities;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Stomper extends Ability implements Listener {

	private static String name = "Stomper";
	
	public Stomper() {
		super(name, "Stomp on people to damage them!", Material.DIAMOND_BOOTS, Scarcity.DARK_RED);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			User user = Core.getInstance().getUserManager().getUser(player);
			if(!user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name)))
				return;
			if(event.getCause().equals(DamageCause.FALL)){
				if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()){
				for(Entity entity : player.getNearbyEntities(3, 2, 3)){
					if(entity instanceof LivingEntity){
						LivingEntity le = (LivingEntity) entity;
						double damage = event.getDamage();
						if(le instanceof Player){
							Player hit = (Player) le;
							if(Core.getInstance().getUserManager().getUser(hit).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Anti-Stomper"))){
								if(!Core.getInstance().getAbilityManager().getAbility("Anti-Stomper").callEvent(hit, Core.getInstance().getAbilityManager().getAbility("Anti-Stomper")).isCancelled()){
									damage = 0;
									player.damage(event.getDamage());
								}
							}else
							if(((Player) le).isSneaking()){
								damage = 0;
							}
						}
						le.damage(damage);
					}
				}
				player.playEffect(EntityEffect.HURT);
				event.setCancelled(true);
				}
			}
		}
	}


}
