package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.abilities.objects.Blockade;

public class ParalysisBlockade extends Ability {

	private static String name = "Paralysis Blockade";

	public ParalysisBlockade() {
		super(name, "Spawn a large wall of emeralds, paralyzing nearby players!", Material.EMERALD, Scarcity.DARK_RED, 14, 1);
		super.setClickedItem(Material.STICK);
		super.setCooldown(20 * 40);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(!super.callEvent(player, this).isCancelled()){
				Blockade blockade = new Blockade(player, Material.EMERALD_BLOCK, 7, 5);
				blockade.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 6));
				blockade.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, -5));				
				blockade.spawn();
				super.putOnCooldown(player);
			}
		}
	}
}
