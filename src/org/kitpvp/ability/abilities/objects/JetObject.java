package org.kitpvp.ability.abilities.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class JetObject {

	private Player player;
	private Minecart jet;

	private ItemStack[] pInv;

	private int speed = 1;

	private int missileAmmo = 20;
	private double fuel = 800;

	public JetObject(Player player) {
		this.player = player;
		pInv = player.getInventory().getContents();
		jet = player.getWorld().spawn(player.getLocation(), Minecart.class);
		jet.setPassenger(player);
		jet.setCustomName("jet");
		this.setupInv();
		Core.getInstance().getJetManager().addJet(this);
	}

	public void switchSpeed() {
		if (speed < 2)
			speed++;
		else
			speed = 0;
	}

	public int getSpeed() {
		return this.speed;
	}

	public double getFuel() {
		return this.fuel;
	}

	public int getMissileAmmo() {
		return this.missileAmmo;
	}

	public void removeFuel(double amount) {
		this.fuel -= amount;
		if (this.fuel <= 0)
			this.despawn();
	}

	public Minecart getMinecart() {
		return this.jet;
	}

	private void setupInv() {
		player.getInventory().clear();
		player.getInventory().addItem(Core.getInstance().getItemManager().createItem(
				ChatColor.GREEN + "Speed Toggle " + ChatColor.GRAY + "(Slow)", Material.LEVER, (byte) 0, 1, null));
		player.getInventory().addItem(Core.getInstance().getItemManager().createItem(ChatColor.RED + "Missile",
				Material.STICK, (byte) 0, 1, null));
		player.getInventory().addItem(Core.getInstance().getItemManager().createItem(ChatColor.BLUE + "Machine Gun",
				Material.STICK, (byte) 0, 1, null));
	}

	public void fireMissile() {
		if (!(this.missileAmmo <= 0)) {
			this.missileAmmo--;
			Arrow arrow = player.launchProjectile(Arrow.class);
			arrow.setCustomName("jet_missile");
		}
		// if(this.missileAmmo <= 0)
		// this.despawn();
	}

	public boolean hasPassenger() {
		return this.jet.getPassenger() != null;
	}

	public Player getPassenger() {
		if (this.jet.getPassenger() instanceof Player)
			return (Player) this.jet.getPassenger();
		return null;
	}

	public void despawn() {
		ParticleEffect.CLOUD.display(1, 1, 1, 0, 4, jet.getLocation(), 50);
		jet.getWorld().playSound(jet.getLocation(), Sound.ZOMBIE_METAL, 1, 1);
		jet.remove();
		if (Bukkit.getServer().getOnlinePlayers().contains(player))
			Core.getInstance().getUserManager().getUser(player)
					.setCooldown(Core.getInstance().getAbilityManager().getAbility("Jet"), 20 * 70);
		this.resetPlayerInventory();
		Core.getInstance().getJetManager().removeJet(this);
	}

	private void resetPlayerInventory() {
		player.getInventory().clear();
		player.getInventory().setContents(pInv);
		player.setFallDistance(0);
	}

}
