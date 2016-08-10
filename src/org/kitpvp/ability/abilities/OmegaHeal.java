package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;

public class OmegaHeal extends Ability {

	public OmegaHeal() {
		super("Omega Heal", "Gain Regeneration III for 6 seconds!", Material.GOLDEN_APPLE, Scarcity.PURPLE, 13);
		super.setClickedItem(new ItemStack(Material.GOLDEN_APPLE));
		super.setCooldown(20 * 20);
	}

	
	@Override
	public void onInteract(Player player, Action action) {
		if(action.equals(Action.RIGHT_CLICK_AIR) ||  action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
			player.removePotionEffect(PotionEffectType.REGENERATION);
			if(!super.callEvent(player, this).isCancelled()) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1, 1);
				PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 6 * 20, 3);
				player.addPotionEffect(regen);
				super.putOnCooldown(player);
			}
			}
		}
	}
}