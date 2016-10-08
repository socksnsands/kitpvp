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
		super("Snail", "_H33%H_ chance of giving a player slowness _H2H_ when you hit them for _H2_H seconds.", Material.FERMENTED_SPIDER_EYE, Scarcity.DARK_RED, 9, 1);
	}

	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof Player){
				Player p = (Player) e.getEntity();
				Player damager = (Player) e.getDamager();
				if(Core.getInstance().getUserManager().getUser(damager).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Snail"))){
					Random r = new Random();
					int percent = r.nextInt(100);
					if(percent < 33){
						if(!super.callEvent(damager, Core.getInstance().getAbilityManager().getAbility("Snail")).isCancelled()){
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
						}
					}
				}
			}
		}
	}
		
}
