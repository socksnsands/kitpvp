package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.util.ParticleEffect;

public class Disengage extends Ability {

	public Disengage() {
		super("Disengage", "Leap Away!", Material.QUARTZ, Scarcity.BLUE, 8);
		super.setClickedItem(new ItemStack(Material.QUARTZ));
		super.setCooldown(12 * 20);
	}
	
	/*
	 * Scarcity: Blue
	 * 	Cooldown: 12s
	 *  Points: 8
	 */

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
				if (!super.callEvent(player, this).isCancelled()) {
					player.playSound(player.getLocation(), Sound.BLOCK_SNOW_BREAK, 1, 1);
					PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 7 * 20, 1);
					ParticleEffect.SNOW_SHOVEL.display(0, 0, 0, 0, 1, loc, 200);
					player.addPotionEffect(speed);
					player.setVelocity(player.getLocation().getDirection().multiply(-1).setY(1.0).setX(3.0));
					super.putOnCooldown(player);
				}
			}
	}
}