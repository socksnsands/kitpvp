package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.bukkit.event.player.PlayerKickEvent;

public class Bash extends Ability implements Listener {

	public Bash() {
		super("Bash", "Bash through your enemies!", Material.SHIELD, Scarcity.PURPLE);
		super.setCooldown(15 * 20);
		super.setClickedItem(new ItemStack(Material.STICK));
	}

	private ArrayList<Player> inBash = new ArrayList<Player>();

	@Override
	public void onInteract(Player player, Action action) {

		// check if bash ability equipped
		// TODO: check if off of cooldown / not disabled
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {

			inBash.add(player);

			Vector dash = player.getLocation().getDirection().multiply(1.5D).setY(0.4D);

			player.setVelocity(dash);

		}
	}

	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if (inBash.contains(p)) {
			inBash.remove(p);
		}
	}
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		
		if (inBash.contains(p)) {
			inBash.remove(p);
		}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		// TODO: CHeck if player has ability equiped
		if (inBash.contains(p)) {
			for (Entity entities : p.getNearbyEntities(1.0, 1.0, 1.0)) {
				if ((entities instanceof LivingEntity)) {
					LivingEntity entity = (LivingEntity) entities;
					entity.damage(4.0);
					Vector kb = p.getLocation().getDirection().multiply(-0.2D).setY(0.7D);
					p.setVelocity(kb);
					Vector ekb = p.getLocation().getDirection().multiply(1.0D).setY(0.3D);
					entity.setVelocity(ekb);
					PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 2 * 20, 2, true);
					entity.addPotionEffect(slow);
					inBash.remove(p);
				}
			}
		}

	}

}
