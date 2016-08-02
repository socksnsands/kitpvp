package org.kitpvp.ability.abilities;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;

public class Ignite extends Ability {

	public Ignite() {
		super("Ignite", "Ignite your foes!", Material.FIREBALL, Scarcity.WHITE);
		super.setCooldown(15 * 20);
		super.setClickedItem(new ItemStack(Material.BLAZE_ROD));
	}

	@Override
	public void onInteract(Player player, Action action) {

		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			List<Block> playerLineOfSight = player.getLineOfSight((HashSet<Byte>) null, 6);
			if (!super.callEvent(player, this).isCancelled()) {
				super.putOnCooldown(player);
				for (Block blocks : playerLineOfSight) {
					Location blocksLocation = blocks.getLocation();
					for (Entity entities : blocksLocation.getWorld().getNearbyEntities(blocksLocation, 1, 1, 1)) {
						if ((entities instanceof LivingEntity)) {
							LivingEntity entity = (LivingEntity) entities;
							if (!entity.equals(player)) {
								entities.setFireTicks(5 * 20);
							}
						}
					}
				}
			}

		}

	}

}