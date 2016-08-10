package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;

public class Heal extends Ability {

	public Heal() {
		super("Heal", "Gain Regeneration I for 8 seconds!", Material.APPLE, Scarcity.BLUE, 7);
		super.setClickedItem(new ItemStack(Material.APPLE));
		super.setCooldown(12 * 20);
	}

	
	@Override
	public void onInteract(Player player, Action action) {
		if(action.equals(Action.RIGHT_CLICK_AIR) ||  action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
			player.removePotionEffect(PotionEffectType.REGENERATION);
			if(!super.callEvent(player, this).isCancelled()) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
				PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 8 * 20, 1);
				player.addPotionEffect(regen);
				super.putOnCooldown(player);
			}
			}
		}
	}
}