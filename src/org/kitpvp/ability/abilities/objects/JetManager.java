package org.kitpvp.ability.abilities.objects;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	public JetManager(){
		this.startJetCheck();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	public void addJet(JetObject jet){
		if(!this.jets.contains(jet))
			this.jets.add(jet);
	}
	
	public void removeJet(JetObject jet){
		if(this.jets.contains(jet))
			this.jets.remove(jet);
	}
	
	public ArrayList<JetObject> getJets(){
		return this.jets;
	}
	
	private void startJetCheck(){
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable(){

			@Override
			public void run() {
				ArrayList<JetObject> js = jets;
				for(JetObject jet : js){
					if(!jet.hasPassenger()){
						jet.despawn();
					}else{
						if(jet.getSpeed() == 1){
							jet.getMinecart().setVelocity(jet.getPassenger().getLocation().getDirection().multiply(.2D));
							jet.removeFuel(1);
						}else if(jet.getSpeed() == 2){
							jet.getMinecart().setVelocity(jet.getPassenger().getLocation().getDirection().multiply(.55D));
							jet.removeFuel(3);
						}else{
							jet.getMinecart().setVelocity(new Vector(0, .04, 0));
							jet.removeFuel(.3);
						}
						ActionBar ab = new ActionBar(ChatColor.DARK_GRAY + "Jet" + ChatColor.GRAY +"- Fuel: " + ChatColor.GREEN + (int)jet.getFuel() + ChatColor.GRAY + " Missiles: " + ChatColor.GREEN + jet.getMissileAmmo());
						ab.sendToPlayer(jet.getPassenger());
						jet.getPassenger().setFallDistance(0);
						ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, jet.getMinecart().getLocation(), 200);
					}
				}
			}
			
		}, 1, 1);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEntityEvent event){
		if(event.getRightClicked() instanceof Minecart){
			if(event.getRightClicked().getCustomName().equals("jet")){
				event.setCancelled(true);
			}
		}
	}
	
	public boolean isInJet(Player player){
		if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Jet"))){
			for(JetObject jet : this.jets){
				if(jet.getPassenger().equals(player)){
					return true;
				}
			}
		}
		return false;
	}
	
	public JetObject getCurrentJet(Player player){
		if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Jet"))){
			for(JetObject jet : this.jets){
				if(jet.getPassenger().equals(player)){
					return jet;
				}
			}
		}
		return null;
	}
	
	@EventHandler
	public void onDestroy(VehicleDestroyEvent event){
		for(JetObject jet : this.jets){
			if(jet.getMinecart().equals(event.getVehicle())){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event){
		if(event.getEntity() instanceof Arrow){
			if(event.getEntity().getCustomName().equals("jet_missile")){
				event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0);
				Arrow arrow = (Arrow) event.getEntity();
				for(Player player : event.getEntity().getWorld().getPlayers()){
					if(player.getLocation().distance(event.getEntity().getLocation()) < 3 && player != arrow.getShooter()){
						player.damage(5);
					}
				}
				event.getEntity().remove();
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		this.checkAndRemove(event.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		this.checkAndRemove(event.getPlayer());
	}
	
	private void checkAndRemove(Player player){
		if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Jet"))){
			if(this.isInJet(player)){
				this.getCurrentJet(player).despawn();
			}
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event){
		if(Core.getInstance().getUserManager().getUser(event.getPlayer()).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Jet"))){
		if(this.isInJet(event.getPlayer())){
			event.setCancelled(true);
		}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		ItemStack item = event.getPlayer().getItemInHand();
		if(item.getType().equals(Material.STICK) || item.getType().equals(Material.LEVER)){
			if(item.getItemMeta() != null){
				if(item.getItemMeta().getDisplayName() != null){
					if(this.isInJet(event.getPlayer())){
						if(item.getItemMeta().getDisplayName().startsWith(ChatColor.RED + "Missile")){
							this.getCurrentJet(event.getPlayer()).fireMissile();
						}
						if(item.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Speed Toggle")){
							this.getCurrentJet(event.getPlayer()).switchSpeed();
							String speed = "Slow";
							if(this.getCurrentJet(event.getPlayer()).getSpeed() == 0)
								speed = "Hovering";
							if(this.getCurrentJet(event.getPlayer()).getSpeed() == 2)
								speed = "Fast";
							ItemMeta im = item.getItemMeta();
							im.setDisplayName(ChatColor.GREEN + "Speed Toggle " + ChatColor.GRAY + "(" + speed + ")");
							item.setItemMeta(im);
							event.getPlayer().updateInventory();
							event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1, 1);
						}
						if(item.getItemMeta().getDisplayName().startsWith(ChatColor.BLUE + "Machine Gun")){
							Player player = event.getPlayer();
							player.getWorld().playSound(player.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1, 1);
							ArrayList<String> players = new ArrayList<String>();
							for(int i = 0; i < 100; i++){
								Location l = player.getLocation().getDirection().normalize().multiply(i/5).toLocation(player.getWorld());
								Location loc = player.getLocation().clone().add(l).add(0, .5, 0);
								ParticleEffect.CRIT.display(0, 0, 0, 0, 1, loc, 200);
								if(i %5 == 0){
									for(Player p : player.getWorld().getPlayers()){
										if(p != player && p.getLocation().clone().add(0,1,0).distance(loc) < 1){
											if(!players.contains(p.getName())){
											players.add(p.getName());
											p.damage(1);
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
