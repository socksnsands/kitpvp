package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Noob extends Ability implements Listener {

	private static String name = "Noob";

	public Noob() {
		super(name, "Prevents you from dropping essentials!", Material.MUSHROOM_SOUP, Scarcity.WHITE, 2, 1);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (Core.getInstance().getUserManager().getUser(event.getPlayer()).getActiveAbilities()
				.contains(Core.getInstance().getAbilityManager().getAbility(name))) {
			if (event.getItemDrop().getItemStack().getType().equals(Material.STONE_SWORD)
					|| event.getItemDrop().getItemStack().getType().equals(Material.MUSHROOM_SOUP)) {
				if (!super.callEvent(event.getPlayer(), Core.getInstance().getAbilityManager().getAbility(name))
						.isCancelled()) {
					event.setCancelled(true);
				}
			}
		}
	}

}
