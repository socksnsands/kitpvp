package org.kitpvp.ability.abilities;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;

public class Totem extends Ability {

	public Totem() {
		super("Totem", "Create totem buffs!", Material.ARMOR_STAND, Scarcity.RED, 10, 1);
		super.setCooldown(15 * 20);
		super.setClickedItem(new ItemStack(Material.IRON_BARDING));
	}

	@Override
	public void onInteract(Player player, Action action) {

		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {

			if (!super.callEvent(player, this).isCancelled()) {

				Random random = new Random();

				PotionEffectType type = PotionEffectType.REGENERATION;
				int amplifier = 4;
				Material topMaterial = Material.STAINED_GLASS;
				byte topByte = 6;

				int q = random.nextInt(5);

				if (q == 0) {
					type = PotionEffectType.INCREASE_DAMAGE;
					amplifier = 0;
					topMaterial = Material.STAINED_GLASS;
					topByte = 14;
				} else if (q >= 3) {
					type = PotionEffectType.DAMAGE_RESISTANCE;
					amplifier = 1;
					topMaterial = Material.STAINED_GLASS;
					topByte = 8;
				}

				org.kitpvp.ability.abilities.objects.Totem totem = new org.kitpvp.ability.abilities.objects.Totem(
						player, player.getLocation(), type, amplifier, 20 * 10, 6, topMaterial, topByte);

				super.putOnCooldown(player);

			}

		}

	}

}