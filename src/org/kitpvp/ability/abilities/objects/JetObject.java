package org.kitpvp.ability.abilities.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class JetObject {

	private Player player;
	private Minecart jet;
	
	private PlayerInventory pInv;
	
	private int missileAmmo = 40;
	
	public JetObject(Player player){
		this.player = player;
		pInv = player.getInventory();
		jet = player.getWorld().spawn(player.getLocation(), Minecart.class);
		jet.setPassenger(player);
		jet.setDerailedVelocityMod(player.getLocation().getDirection().multiply(.2D));
		jet.setCustomName("jet");
		this.setupInv();
	}
	
	private void setupInv(){
		player.getInventory().clear();
		player.getInventory().addItem(Core.getInstance().getItemManager().createItem(ChatColor.RED + "Missile", Material.STICK, (byte)0, 1, null));
	}
	
	public void fireMissile(){
		this.missileAmmo--;
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setCustomName("jet_missile");
		if(this.missileAmmo == 0)
			this.despawn();
	}
	
	public boolean hasPassenger(){
		return this.jet.getPassenger() != null;
	}
	
	public Player getPassenger(){
		if(this.jet.getPassenger() instanceof Player)
			return (Player) this.jet.getPassenger();
		return null;
	}
	
	public void despawn(){
		ParticleEffect.CLOUD.display(1, 1, 1, 0, 4, jet.getLocation(), 50);
		jet.getWorld().playSound(jet.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);
		jet.remove();
		Core.getInstance().getUserManager().getUser(player).setCooldown(Core.getInstance().getAbilityManager().getAbility("Jet"), 20*70);
		this.resetPlayerInventory();
	}
	
	private void resetPlayerInventory(){
		player.getInventory().setContents(pInv.getContents());
		player.getInventory().setArmorContents(pInv.getArmorContents());
		player.setFallDistance(0);
	}
	
}
