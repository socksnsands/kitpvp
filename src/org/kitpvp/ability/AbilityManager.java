package org.kitpvp.ability;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.user.User;

public class AbilityManager implements Listener {

	private int cooldownHandler;

	public AbilityManager() {
		this.startCooldownHandler();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}

	private void startCooldownHandler() {
		this.cooldownHandler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(),
				new Runnable() {

					@Override
					public void run() {
						for (User user : Core.getInstance().getUserManager().getUsers()) {
							if (user != null) {
								user.callCooldownTick();
							}
						}
					}

				}, 1, 1);
	}

	public ArrayList<Ability> getAbilities() {
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for (Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()) {
			if (unlockable instanceof Ability) {
				abilities.add((Ability) unlockable);
			}
		}
		return abilities;
	}

	private void stopCooldownHandler() {
		Bukkit.getServer().getScheduler().cancelTask(this.cooldownHandler);
	}

	public Ability getAbility(String name) {
		for (Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()) {
			if (unlockable instanceof Ability) {
				if (unlockable.getName().equalsIgnoreCase(name)) {
					return (Ability) unlockable;
				}
			}
		}
		return null;
	}

	public boolean isAbility(String name) {
		for (Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()) {
			if (unlockable instanceof Ability) {
				if (unlockable.getName().equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (Core.getInstance().getUserManager().isLoadedUser(event.getPlayer())) {
			User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
			for (Ability ability : user.getActiveAbilities()) {
				if (ability.getClickedItem() != null) {
					if (ability.getClickedItem().equals(event.getPlayer().getInventory().getItemInHand())) {
						if (!user.isOnCooldown(ability)) {
							ability.onInteract(event.getPlayer(), event.getAction());
						} else {
							user.getPlayer()
									.sendMessage(ability.getScarcity().getColor() + ability.getName() + ChatColor.GRAY
											+ " is still on cooldown for " + ChatColor.RED
											+ this.round(user.getRemainingCooldown(ability) / 20, 1) + "s"
											+ ChatColor.GRAY + "!");
						}
					}
				}
			}
		}
	}

	private double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

}
