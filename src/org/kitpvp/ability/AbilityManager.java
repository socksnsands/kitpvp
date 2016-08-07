package org.kitpvp.ability;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.user.User;

public class AbilityManager implements Listener {
	
	private int cooldownHandler;
	
	public AbilityManager(){
		this.startCooldownHandler();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	private void startCooldownHandler(){
		this.cooldownHandler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable(){

			@Override
			public void run() {
				for(User user : Core.getInstance().getUserManager().getUsers()){
					user.callCooldownTick();
				}
			}
			
		}, 1, 1);
	}
	
	private void stopCooldownHandler(){
		Bukkit.getServer().getScheduler().cancelTask(this.cooldownHandler);
	}
	
	public Ability getAbility(String name){
		for(Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()){
			if(unlockable instanceof Ability){
				if(unlockable.getName().equalsIgnoreCase(name)){
					return (Ability) unlockable;
				}
			}
		}
		return null;
	}
	
	public boolean isAbility(String name){
		for(Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()){
			if(unlockable instanceof Ability){
				if(unlockable.getName().equalsIgnoreCase(name)){
					return true;
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(Core.getInstance().getUserManager().isLoadedUser(event.getPlayer())){
			User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
			for(Ability ability : user.getActiveAbilities()){
				if(ability.getClickedItem()!=null){
					if(ability.getClickedItem().equals(event.getPlayer().getInventory().getItemInMainHand())){
						if(!user.isOnCooldown(ability)){
							ability.onInteract(event.getPlayer(), event.getAction());
						}
					}
				}
			}
		}
	}
	
}