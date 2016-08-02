package org.kitpvp.ability.abilities.objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kitpvp.core.Core;

public class TotemManager implements Listener {

	private ArrayList<Totem> totems = new ArrayList<Totem>();
	
	public TotemManager(){
		this.startTotemActions();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	private void startTotemActions(){
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable(){

			@Override
			public void run() {
				for(Totem totem : totems){
					totem.giveEffect();
				}
			}
			
		}, 20, 20);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			for(Totem totem : totems){
				if(totem.getLocation().clone().add(0,1,0).equals(event.getClickedBlock().getLocation())){
					totem.damage(event.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		for(Totem totem : totems){
			if(totem.getPlayer().equals(event.getPlayer())){
				totem.destroy();
			}
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		for(Totem totem : totems){
			if(totem.getPlayer().equals(event.getPlayer())){
				totem.destroy();
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		for(Totem totem : totems){
			if(totem.getPlayer().equals(event.getEntity())){
				totem.destroy();
			}
		}
	}
	
	public ArrayList<Totem> getTotems(){
		return this.totems;
	}
	
	public void addTotem(Totem totem){
		if(!this.totems.contains(totem))
			this.totems.add(totem);
	}
	
	public void removeTotem(Totem totem){
		if(this.totems.contains(totem))
			this.totems.remove(totem);
	}
	
}
