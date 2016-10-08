package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;

public class QuickShot extends Ability {

	public QuickShot() {
		super("Quick Shot", "Instantly shoot an arrow!", new ItemStack(Material.ARROW), Scarcity.BLUE, 5, 1);
		super.setClickedItem(new ItemStack(Material.STONE_HOE));
		super.setCooldown(5 * 20);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)
				|| action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			if (!super.callEvent(player, this).isCancelled()) {
				player.getLocation().getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
				player.launchProjectile(Arrow.class);
				super.putOnCooldown(player);
			}
		}
	}

}
