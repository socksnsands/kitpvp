package org.kitpvp.ability.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class FlashGrenade extends Ability {

	private static String name = "Flash Grenade";

	public FlashGrenade() {
		super(name, "Throw a flash grenade!", Material.GLOWSTONE_DUST, Scarcity.BLUE, 5);
		super.setClickedItem(Material.GLOWSTONE_DUST);
		super.setCooldown(20 * 14);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!super.callEvent(player, this).isCancelled()) {
				Item item = player.getWorld().dropItem(player.getLocation().clone().add(0, 1, 0),
						new ItemStack(Material.GLOWSTONE_DUST));
				item.setPickupDelay(Integer.MAX_VALUE);
				item.setVelocity(player.getLocation().getDirection().multiply(1.2));
				super.putOnCooldown(player);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

					@Override
					public void run() {
						ParticleEffect.EXPLOSION_HUGE.display(0, 0, 0, 0, 1, item.getLocation(), 200);
						item.getWorld().playSound(item.getLocation(), Sound.GLASS, 1, 1);
						for (Entity entity : item.getNearbyEntities(5, 5, 5)) {
							if (entity instanceof Player) {
								Player le = (Player) entity;
								le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*4, 0));
								le.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*4, 0));
							}
						}
						item.remove();
					}

				}, 20 * 2);
			}
		}
	}

}
