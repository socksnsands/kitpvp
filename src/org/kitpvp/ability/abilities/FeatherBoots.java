package org.kitpvp.ability.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.AbilityUseEvent;
import org.kitpvp.core.Core;

public class FeatherBoots extends Ability implements Listener {

	public FeatherBoots() {
		super("Feather Boots", "Take no fall damage.", new ItemStack(Material.FEATHER), Scarcity.BLUE, 4, 1);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
					.contains(Core.getInstance().getAbilityManager().getAbility("Feather Boots"))) {
				if (event.getCause().equals(DamageCause.FALL)) {
					AbilityUseEvent e = super.callEvent(player, this);
					if (!e.isCancelled()) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

}
