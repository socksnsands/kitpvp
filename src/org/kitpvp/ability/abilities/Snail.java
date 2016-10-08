package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Snail extends Ability implements Listener{

	public Snail() {
		super("Snail", "{H33%H} chance of giving a player slowness {H2H} when you hit them.", Material.FERMENTED_SPIDER_EYE, Scarcity.DARK_RED, 9, 1);
	}

	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof Player){
				Player p = (Player) e.getEntity();
				Player damager = (Player) e.getDamager();
				if(Core.getInstance().getUserManager().getUser(damager).getActiveAbilities().contains(this)){
					Random r = new Random();
					int percent = r.nextInt(100);
					if(percent < 33){
						if(!super.callEvent(damager, this).isCancelled()){
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
						}
					}
				}
			}
		}
	}
		
}
