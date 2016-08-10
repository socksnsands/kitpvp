package org.kitpvp.ability.abilities;

import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;

public class OmegaFlash extends Ability {

	public OmegaFlash() {
		super("Omega Flash", "Instantly Flash a Great Distance forward!", Material.FEATHER, Scarcity.RED, 12);
		super.setClickedItem(new ItemStack(Material.FEATHER));
		super.setCooldown(20 * 20);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)
				|| action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			if (!super.callEvent(player, this).isCancelled()) {
				Location location = player.getLineOfSight((HashSet<Byte>) null, 21)
						.get(player.getLineOfSight((HashSet<Byte>) null, 21).size() - 1).getLocation();
				location.setYaw(player.getLocation().getYaw());
				location.setPitch(player.getLocation().getPitch());
				player.teleport(location);
				player.getLocation().getWorld().playEffect(player.getLocation(), Effect.CLOUD, 2);
				player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 1, 1);
				super.putOnCooldown(player);
				player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
			}
		}
	}

}
