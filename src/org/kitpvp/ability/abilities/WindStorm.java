package org.kitpvp.ability.abilities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

@SuppressWarnings("deprecation")
public class WindStorm extends Ability implements Listener {

	public WindStorm() {
		super("Wind Storm", "Summon a storm of deadly winds!", Material.WOOL, Scarcity.DARK_RED, 16);
		super.setClickedItem(Material.SUGAR);
		super.setCooldown(60 * 20);
	}

	@Override
	public void onInteract(Player player, Action action) {
		if (!super.callEvent(player, this).isCancelled()) {
			this.playWindEffect(player.getLocation(), player);
			super.putOnCooldown(player);
		}
	}

	private void playWindEffect(Location location, Player player) {
		for (int x = -5; x < 5; x++) {
			for (int y = -2; y < 5; y++) {
				for (int z = -5; z < 5; z++) {
					Location loc = location.clone().add(x, y, z);
					for (int i = 0; i < 40; i++) {
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

							@Override
							public void run() {
								Location a = loc.clone();
								Location b = location.clone();
								a.setY(0);
								b.setY(0);
								if (a.distance(b) <= 5) {
									Random random = new Random();
									if (random.nextInt(15) == 0) {

										if (a.distance(b) > 4)
											ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, loc, 100);
									}
									throwBlock(loc.clone().add(0,1,0));
									if (random.nextInt(150) == 0)
										loc.getWorld().playSound(loc, Sound.ENDERDRAGON_WINGS, 1, 1);
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										if (!p.equals(player)) {
											double dist = p.getLocation().distance(location);
											if (dist <= 4.5) {
												p.damage(2);
												int h = (int) (3 - dist <= 0 ? 0 : 3 - dist);
												int l = (int) (-3 - dist >= 0 ? 0 : -3 - dist);
												p.setVelocity(new Vector(random.nextInt(h - l) + l,
														(random.nextInt(h - l) + l), random.nextInt(h - l) + l));
											}
										}
									}
								}
							}

						}, i * 4);

					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockFall(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof FallingBlock) {
			if (event.getEntity().getName().equals("no_land")) {
				FallingBlock fs = (FallingBlock) event.getEntity();
				fs.getLocation().getWorld().playEffect(fs.getLocation(), Effect.STEP_SOUND,
						Material.getMaterial(fs.getBlockId()));
				event.setCancelled(true);
			}
		}

	}

	private void throwBlock(Location block) {
		List<Material> noThrow = Arrays.asList(Material.AIR, Material.DOUBLE_PLANT, Material.LONG_GRASS,
				Material.YELLOW_FLOWER);
		if (!noThrow.contains(block.getBlock().getType())) {
			if (!block.getBlock().isLiquid()) {
				if (block.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
					Random random = new Random();
					if (random.nextInt(15) == 0) {
						FallingBlock fs = block.getWorld().spawnFallingBlock(block.clone().add(0, 1, 0),
								block.getBlock().getType(), block.getBlock().getData());
						fs.setDropItem(false);
						fs.setVelocity(new Vector((random.nextDouble()) - .5, (random.nextDouble() * 1.4),
								(random.nextDouble()) - .5));
						fs.setCustomName("no_land");
						fs.setDropItem(false);
						block.getBlock().setType(block.getBlock().getType());
					}
				}
			}
		}
	}

}
