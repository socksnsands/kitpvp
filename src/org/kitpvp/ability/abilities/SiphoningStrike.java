package org.kitpvp.ability.abilities;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.util.ParticleEffect;

public class SiphoningStrike extends Ability {

	public SiphoningStrike() {
		super("Siphoning Strike", "Strike your foes!", Material.BONE, Scarcity.BLUE, 10);
		super.setCooldown(15 * 20);
		super.setClickedItem(new ItemStack(Material.BONE));
	}

	@Override
	public void onInteract(Player player, Action action) {

		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			List<Block> playerLineOfSight = player.getLineOfSight((HashSet<Byte>) null, 3);
			if (!super.callEvent(player, this).isCancelled()) {
				player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
				super.putOnCooldown(player);
				for (Block blocks : playerLineOfSight) {
					Location blocksLocation = blocks.getLocation();
					for (Entity entities : blocksLocation.getWorld().getNearbyEntities(blocksLocation, 1, 1, 1)) {
						if ((entities instanceof LivingEntity)) {
							LivingEntity entity = (LivingEntity) entities;
							if (!entity.equals(player)) {
								
								PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 6 * 20, 1);
								entity.addPotionEffect(wither);
								
								player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
								player.playSound(entities.getLocation(), Sound.ENTITY_CREEPER_HURT, 1, 1);
								ParticleEffect.DRAGONBREATH.display(1, 1, 1, 1, 2, player.getLocation(), 200);
							}
						}
					}
				}
			}

		}

	}

}