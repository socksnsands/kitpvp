package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Poseidon extends Ability {

	private static String name = "Poseidon";

	public Poseidon() {
		super(name, "Shoot a wave!", Material.WATER_BUCKET, Scarcity.DARK_RED, 18);
		super.setClickedItem(Material.PRISMARINE_SHARD);
		super.setCooldown(20 * 35);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!this.isAboveVoid(player.getLocation())) {
				if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()) {
					player.getWorld().playSound(player.getLocation(), Sound.WITHER_HURT, 1, 1);
					this.playPoseidonEffect(player);
					super.putOnCooldown(player);
				}
			} else {
				player.sendMessage(ChatColor.RED + "You cannot use poseidon here!");
			}
		}
	}

	private void playPoseidonEffect(final Player player) {
		Location startLoc = getLocationBelow(player.getLocation()).add(0, 1, 0);
		ArrayList<Location> hitLocations = new ArrayList<>();
		ArrayList<String> hitPlayers = new ArrayList<>();
		Location faked = startLoc;
		faked.setPitch(0);
		for (int i = 0; i < 12; i++) {
			Location l = faked.getDirection().normalize().multiply(i).toLocation(player.getWorld());
			final Location loc = startLoc.clone().add(l);
			// Bukkit.broadcastMessage(loc.getX() + ", " + loc.getY() + ", " +
			// loc.getZ());
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

				@Override
				public void run() {
					if (!hitLocations.contains(loc.getBlock().getLocation())) {
						throwUpBlock(loc);
						for (Player p : Bukkit.getServer().getOnlinePlayers()) {
							if (p != player && !hitPlayers.contains(p.getName())
									&& p.getLocation().distance(loc) < 1.5) {
								hitPlayers.add(p.getName());
								Core.getInstance().getDamageManager().damage(p, player, 16);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
								p.getWorld().playSound(p.getLocation(), Sound.ZOMBIE_METAL, 1, 1);
								p.setVelocity(faked.getDirection().multiply(1.4).setY(.4));
							}
						}
					}
				}

			}, i);
		}
	}

	private void throwUpBlock(Location location) {
		@SuppressWarnings("deprecation")
		FallingBlock fb = location.getWorld().spawnFallingBlock(location.clone().add(0, -.3, 0), Material.LAPIS_BLOCK,
				(byte) 0);
		fb.setVelocity(new Vector(0, .3, 0));
		fb.setCustomName("no_land");
		fb.setDropItem(false);
	}

	private boolean isAboveVoid(Location location) {
		if (getLocationBelow(location) == null) {
			return true;
		}
		return false;
	}

	private Location getLocationBelow(Location location) {
		for (int i = location.getBlockY(); i > location.getBlockY() - 10; i--) {
			if (!location.getWorld().getBlockAt(location.getBlockX(), i, location.getBlockZ()).getType()
					.equals(Material.AIR)) {
				Location loc = location.getWorld().getBlockAt(location.getBlockX(), i, location.getBlockZ())
						.getLocation();
				loc.setPitch(location.getPitch());
				loc.setYaw(location.getYaw());
				return loc;
			}
		}
		return null;
	}

}
