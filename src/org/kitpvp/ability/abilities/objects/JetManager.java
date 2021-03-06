package org.kitpvp.ability.abilities.objects;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.kitpvp.core.Core;
import org.kitpvp.util.ActionBar;
import org.kitpvp.util.ParticleEffect;

public class JetManager implements Listener {

	ArrayList<JetObject> jets = new ArrayList<JetObject>();

	public JetManager() {
		this.startJetCheck();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}

	public void addJet(JetObject jet) {
		if (!this.jets.contains(jet))
			this.jets.add(jet);
	}

	public void removeJet(JetObject jet) {
		if (this.jets.contains(jet))
			this.jets.remove(jet);
	}

	public ArrayList<JetObject> getJets() {
		return this.jets;
	}

	private void startJetCheck() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {

			@Override
			public void run() {
				ArrayList<JetObject> toRemove = new ArrayList<>();
				for (JetObject jet : jets) {
					if (!jet.hasPassenger()) {
						toRemove.add(jet);
					} else {
						if (jet.getSpeed() == 1) {
							jet.getMinecart()
									.setVelocity(jet.getPassenger().getLocation().getDirection().multiply(.2D));
							jet.removeFuel(1);
						} else if (jet.getSpeed() == 2) {
							jet.getMinecart()
									.setVelocity(jet.getPassenger().getLocation().getDirection().multiply(.55D));
							jet.removeFuel(3);
						} else {
							jet.getMinecart().setVelocity(new Vector(0, .04, 0));
							jet.removeFuel(.3);
						}
						ActionBar ab = new ActionBar(ChatColor.DARK_GRAY + "Jet" + ChatColor.GRAY + "- Fuel: "
								+ ChatColor.GREEN + (int) jet.getFuel() + ChatColor.GRAY + " Missiles: "
								+ ChatColor.GREEN + jet.getMissileAmmo());
						ab.sendToPlayer(jet.getPassenger());
						jet.getPassenger().setFallDistance(0);
						ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, jet.getMinecart().getLocation(), 200);
					}
				}
				for(JetObject jo : toRemove)
					jo.despawn();
			}

		}, 1, 1);
	}

	public boolean isJet(Minecart minecart){
		for(JetObject jo : this.jets){
			if(jo.getMinecart().equals(minecart)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isJetMissile(Arrow arrow){
		for(JetObject jo : this.jets){
			for(Arrow a : jo.getMissiles()){
				if(a.equals(arrow)){
					return true;
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Minecart) {
			if(isJet((Minecart) event.getRightClicked())){
				event.setCancelled(true);
			}
		}
	}

	public boolean isInJet(Player player) {
		if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
				.contains(Core.getInstance().getAbilityManager().getAbility("Jet"))) {
			for (JetObject jet : this.jets) {
				if (jet.getPassenger().equals(player)) {
					return true;
				}
			}
		}
		return false;
	}

	public JetObject getCurrentJet(Player player) {
		if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
				.contains(Core.getInstance().getAbilityManager().getAbility("Jet"))) {
			for (JetObject jet : this.jets) {
				if (jet.getPassenger().equals(player)) {
					return jet;
				}
			}
		}
		return null;
	}

	@EventHandler
	public void onDestroy(VehicleDestroyEvent event) {
		for (JetObject jet : this.jets) {
			if (jet.getMinecart().equals(event.getVehicle())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if (this.isJetMissile(arrow)) {
				event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0);
				for (Player player : event.getEntity().getWorld().getPlayers()) {
					if (player.getLocation().distance(event.getEntity().getLocation()) < 3
							&& player != arrow.getShooter()) {
						if(arrow.getShooter() instanceof Player)
						Core.getInstance().getDamageManager().damage(player, (Player)arrow.getShooter(), 7);
					}
				}
				event.getEntity().remove();
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		this.checkAndRemove(event.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		this.checkAndRemove(event.getPlayer());
	}

	private void checkAndRemove(Player player) {
		if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
				.contains(Core.getInstance().getAbilityManager().getAbility("Jet"))) {
			if (this.isInJet(player)) {
				this.getCurrentJet(player).despawn();
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if (Core.getInstance().getUserManager().getUser(event.getPlayer()).getActiveAbilities()
				.contains(Core.getInstance().getAbilityManager().getAbility("Jet"))) {
			if (this.isInJet(event.getPlayer())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		for (Entity entity : event.getPlayer().getWorld().getEntities()) {
			if (entity instanceof Minecart) {
				Minecart minecart = (Minecart) entity;
					if (isJet(minecart)) {
						if (minecart.getPassenger().equals(event.getPlayer())) {
							minecart.remove();
						}
					}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (item.getType().equals(Material.STICK) || item.getType().equals(Material.LEVER)) {
			if (item.getItemMeta() != null) {
				if (item.getItemMeta().getDisplayName() != null) {
					if (this.isInJet(event.getPlayer())) {
						if (item.getItemMeta().getDisplayName().startsWith(ChatColor.RED + "Missile")) {
							this.getCurrentJet(event.getPlayer()).fireMissile();
						}
						if (item.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Speed Toggle")) {
							this.getCurrentJet(event.getPlayer()).switchSpeed();
							String speed = "Slow";
							if (this.getCurrentJet(event.getPlayer()).getSpeed() == 0)
								speed = "Hovering";
							if (this.getCurrentJet(event.getPlayer()).getSpeed() == 2)
								speed = "Fast";
							ItemMeta im = item.getItemMeta();
							im.setDisplayName(ChatColor.GREEN + "Speed Toggle " + ChatColor.GRAY + "(" + speed + ")");
							item.setItemMeta(im);
							event.getPlayer().updateInventory();
							event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(),
									Sound.IRONGOLEM_HIT, 1, 1);
						}
						if (item.getItemMeta().getDisplayName().startsWith(ChatColor.BLUE + "Machine Gun")) {
							Player player = event.getPlayer();
							player.getWorld().playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
							ArrayList<String> players = new ArrayList<String>();
							for (int i = 0; i < 100; i++) {
								Location l = player.getLocation().getDirection().normalize().multiply(i / 5)
										.toLocation(player.getWorld());
								Location loc = player.getLocation().clone().add(l).add(0, .5, 0);
								ParticleEffect.CRIT.display(0, 0, 0, 0, 1, loc, 200);
								if (i % 5 == 0) {
									for (Player p : player.getWorld().getPlayers()) {
										if (p != player && p.getLocation().clone().add(0, 1, 0).distance(loc) < 1) {
											if (!players.contains(p.getName())) {
												players.add(p.getName());
												Core.getInstance().getDamageManager().setLastDamaged(p, player);
												Damageable dm = p;
												p.setHealth(dm.getHealth()-2);
												p.playEffect(EntityEffect.HURT);
												player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
