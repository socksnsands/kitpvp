package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;

public class BulkUp extends Ability {

	public BulkUp() {
		super("Bulk Up", "Gain 4 Absorbation Hearts!", Material.GOLD_CHESTPLATE, Scarcity.BLUE, 10);
		super.setClickedItem(new ItemStack(Material.GOLD_BLOCK));
		super.setCooldown(20 * 20);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
				player.removePotionEffect(PotionEffectType.ABSORPTION);
				if (!super.callEvent(player, this).isCancelled()) {
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					PotionEffect absorb = new PotionEffect(PotionEffectType.ABSORPTION, 60 * 20, 2);
					player.addPotionEffect(absorb);
					super.putOnCooldown(player);
				}
			}
		}
	}
}