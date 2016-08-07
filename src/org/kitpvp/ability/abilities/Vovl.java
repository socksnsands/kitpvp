package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;

public class Vovl extends Ability {

	private static String name = "Vovl";
	
	public Vovl() {
		super(name, "Bench press", Material.ANVIL, Scarcity.BLACK, 30);
		super.setCooldown(20*40);
		super.setClickedItem(Material.IRON_INGOT);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(!super.callEvent(player, this).isCancelled()){
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 2));
				player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
				super.putOnCooldown(player);
			}
		}
	}

	
	
}
