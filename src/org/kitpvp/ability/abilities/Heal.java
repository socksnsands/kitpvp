package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.kitpvp.ability.Ability;
import org.kitpvp.util.ParticleEffect;

public class Heal extends Ability {

	public Heal() {
		super("Heal", "Instantly heal your health and remove all potion effects!", Material.APPLE, Scarcity.BLUE, 4, 1);
		super.setClickedItem(new ItemStack(Material.APPLE));
		super.setCooldown(12 * 20);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
				if (!super.callEvent(player, this).isCancelled()) {
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
					for(PotionEffect potionEffect : player.getActivePotionEffects()){
						player.removePotionEffect(potionEffect.getType());
					}
					if(player.getHealth() + 20 < player.getMaxHealth()){
						player.setHealth(player.getHealth() + 20);
					}else{
						player.setHealth(player.getMaxHealth());
					}
					ParticleEffect.HEART.display(0, 0, 0, 0, 1, player.getLocation().clone().add(0,1.5,0), 200);
					super.putOnCooldown(player);
				}
		}
	}
}