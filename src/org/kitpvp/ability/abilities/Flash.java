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
import org.kitpvp.util.ParticleEffect;

public class Flash extends Ability {

	public Flash(int level) {
		super("Flash", "Instantly teleport _L_ _H" + 5+(level*3) + "H_ blocks forward._L_ Has a cooldown of _H12H_ seconds.", Material.BLAZE_ROD, Scarcity.PURPLE, 8 + (level*2), level);
		super.setClickedItem(new ItemStack(Material.BLAZE_ROD));
		super.setCooldown(12 * 20);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)
				|| action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			if (!super.callEvent(player, super.getAbility()).isCancelled()) {
				Location location = player.getLineOfSight((HashSet<Byte>) null, 5+(super.getLevel()*3))
						.get(player.getLineOfSight((HashSet<Byte>) null, 5+(super.getLevel()*3)).size() - 1).getLocation();
				location.setYaw(player.getLocation().getYaw());
				location.setPitch(player.getLocation().getPitch());
				player.teleport(location);
				player.getLocation().getWorld().playEffect(player.getLocation(), Effect.CLOUD, 1);
				player.getLocation().getWorld().playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
				super.putOnCooldown(player);
				ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0, 1, player.getLocation(), 200);
				player.playSound(player.getLocation(), Sound.LAVA_POP, 1, 1);
			}
		}
	}

}
