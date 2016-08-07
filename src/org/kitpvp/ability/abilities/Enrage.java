package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class Enrage extends Ability {
	
	/*
	 * Type: PASSIVE
Scarcity: Purple
Cooldown: N/A
Points: 10
Possible Second Desc if you don’t like the first: 
Every 10 seconds, you have a 1/2 chance of becoming enraged,
 if you do, you gain Absorption 1, Speed 1, and Strength 1 for 7 seconds.
	 */
	
	public Enrage() {
		super("Enrage", "Randomly become Enraged every", Material.BLAZE_POWDER, Scarcity.PURPLE, 10);
		this.startEnrageCycle();
	}
	
	private void startEnrageCycle(){
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable(){

			@Override
			public void run() {
				if(
						Core
						.getInstance()
						.getUserManager()
						.getUsers()
						!= 
						null)
				for(User user : Core.getInstance().getUserManager().getUsers()){
					if(user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Flame Aura"))){
						if(!getEvent(user.getPlayer()).isCancelled()){
										Player player = (Player) user.getPlayer();
										Random random = new Random();
										if(random.nextInt(1) == 0){
											PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 7*20);
											PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 1, 7*20);
											PotionEffect absorb = new PotionEffect(PotionEffectType.ABSORPTION, 1, 7*20);
											
											player.addPotionEffect(strength);
											player.addPotionEffect(speed);
											player.addPotionEffect(absorb);
											
											//FIX: ParticleEffect.REDSTONE.display(, player.getLocation(), 2.0);
											player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
							}
						}
					}
				}
			}
			
		}, 7*20, 7*20);
	}
	
	private AbilityUseEvent getEvent(Player player){
		return super.callEvent(player, this);
	}

}