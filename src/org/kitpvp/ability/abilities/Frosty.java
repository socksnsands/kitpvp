package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Frosty extends Ability implements Listener {

	private static String name = "Frosty";
	
	public Frosty() {
		super(name, "Chance at freezing enemies on hit!", Material.SNOW_BALL, Scarcity.PURPLE, 10);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			Player player = (Player) event.getDamager();
			User user = (User) Core.getInstance().getUserManager().getUser(player);
			if(!user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name)))
				return;
			Random random = new Random();
			int i = random.nextInt(15);
			if(i < 2){
				if(!super.callEvent(player, this).isCancelled()){
					if(event.getEntity() instanceof LivingEntity){
						LivingEntity le = (LivingEntity) event.getEntity();
						event.getEntity().getWorld().playEffect(event.getEntity().getLocation().clone().add(0,1,0), Effect.STEP_SOUND, Material.ICE);
						event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
						le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
					}
				}
			}
		}
	}

}
