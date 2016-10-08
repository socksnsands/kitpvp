package org.kitpvp.ability.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Switcher extends Ability implements Listener {

	private static String name = "Switcher";

	public Switcher() {
		super(name, "Swap places with foes!", Material.SNOW_BALL, Scarcity.PURPLE, 9, 1);
		super.setClickedItem(Material.IRON_BARDING);
		super.setCooldown(22 * 20);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
				Snowball sb = player.launchProjectile(Snowball.class);
				sb.setCustomName("switcher_shot");
				super.putOnCooldown(player);
			}
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Snowball) {
			Snowball sb = (Snowball) event.getDamager();
			if (sb.getShooter() instanceof Player) {
				if (!sb.getName().equals("switcher_shot"))
					return;
				Player player = (Player) sb.getShooter();
				User user = Core.getInstance().getUserManager().getUser(player);
				if (user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility(name))) {
					if (event.getEntity() instanceof Player) {
						Player hit = (Player) event.getEntity();
						Location loc = player.getLocation();
						player.teleport(hit.getLocation());
						hit.teleport(loc);
					}
				}
			}
		}
	}

}
