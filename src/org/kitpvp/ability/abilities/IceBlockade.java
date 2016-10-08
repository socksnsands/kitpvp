package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.abilities.objects.Blockade;

public class IceBlockade extends Ability {

	private static String name = "Ice Blockade";

	public IceBlockade() {
		super(name, "Spawn a wall of ice, slowing nearby players!", Material.PACKED_ICE, Scarcity.RED, 9, 1);
		super.setClickedItem(Material.STICK);
		super.setCooldown(20 * 36);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(!super.callEvent(player, this).isCancelled()){
				Blockade blockade = new Blockade(player, Material.PACKED_ICE, 7, 4);
				blockade.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2));
				blockade.spawn();
				super.putOnCooldown(player);
			}
		}
	}
}
