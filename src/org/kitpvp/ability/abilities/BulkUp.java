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

public class BulkUp extends Ability {

	public BulkUp(int level) {
		super("Bulk Up", "Gain " + (2+(2*level)) + " absorbtion hearts._L_ Has a _H20H_ second cooldown.", Material.GOLD_CHESTPLATE, Scarcity.BLUE, 3+level, level);
		super.setClickedItem(new ItemStack(Material.GOLD_BLOCK));
		super.setCooldown(20 * 20);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.hasPotionEffect(PotionEffectType.ABSORPTION))
				player.removePotionEffect(PotionEffectType.ABSORPTION);
			if (!super.callEvent(player, this).isCancelled()) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
				PotionEffect absorb = new PotionEffect(PotionEffectType.ABSORPTION, 60 * 20, (2 + 2*super.getLevel()));
				ParticleEffect.CRIT_MAGIC.display(0, 0, 0, 0, 1, player.getLocation(), 200);
				player.addPotionEffect(absorb);
				super.putOnCooldown(player);
			}
			}
	}
}