package org.kitpvp.ability.abilities.objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.kitpvp.core.Core;

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
				for(JetObject jet : jets){
					if(!jet.hasPassenger()){
						jet.despawn();
					}
				}
			}
			
		}, 10, 10);
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
	public void onHit(ProjectileHitEvent event){
		if(event.getEntity() instanceof Arrow){
			if(event.getEntity().getCustomName().equals("jet_missile")){
				event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0);
				Arrow arrow = (Arrow) event.getEntity();
				for(Player player : event.getEntity().getWorld().getPlayers()){
					if(player.getLocation().distance(event.getEntity().getLocation()) < 2){
						player.damage(2);
						player.setVelocity(new Vector(player.getLocation().getX() - arrow.getLocation().getX(), player.getLocation().getY() - arrow.getLocation().getY(), player.getLocation().getZ() - arrow.getLocation().getZ()));
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
		if(item.getType().equals(Material.STICK)){
			if(item.getItemMeta() != null){
				if(item.getItemMeta().getDisplayName() != null){
					if(item.getItemMeta().getDisplayName().startsWith(ChatColor.RED + "Missile")){
						if(this.isInJet(event.getPlayer())){
							
						}
					}
				}
			}
		}
	}
	
}
