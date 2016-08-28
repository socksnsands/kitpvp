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

public class Leap extends Ability {

	public Leap() {
		super("Disengage", "Leap Away!", Material.FEATHER, Scarcity.BLUE, 8);
		super.setClickedItem(new ItemStack(Material.FEATHER));
		super.setCooldown(15 * 20);
	}
	
	/*
*Leap* _Coded_
Type: ACTIVE
Scarcity: Blue
Cooldown: 15s
Points: 8
Desc: Take a Mighty Leap forward, about 5 blocks in the air and 8 blocks in targeted direction
	 */

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
				if (!super.callEvent(player, this).isCancelled()) {
					player.playSound(player.getLocation(), Sound.DIG_SNOW, 1, 1);
					PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 7 * 20, 1);
					ParticleEffect.CLOUD.display(0, 0, 0, 0, 3, player.getLocation(), 200);
					player.addPotionEffect(speed);
					player.setVelocity(player.getLocation().getDirection().multiply(1.0).setY(1.7).setX(2.5));
					super.putOnCooldown(player);
				}
			}
	}
}