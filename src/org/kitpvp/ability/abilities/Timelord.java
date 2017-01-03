package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Timelord extends Ability implements Listener {

	ArrayList<String> frozen = new ArrayList<String>();
	
	public Timelord() {
		super("Timelord", "Freeze time for _H5sH_", Material.WATCH, Scarcity.GOLD, 12, 1);
		super.setClickedItem(Material.WATCH);
		super.setCooldown(20*32);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK))
			return;
		if(!super.callEvent(player, this).isCancelled()){
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				if(p.getLocation().distance(player.getLocation()) < 5){
					if(!p.equals(player)){
						p.sendMessage(ChatColor.RED +  player.getName() + " froze you!");
						if(!this.frozen.contains(p.getUniqueId().toString())){
							this.frozen.add(p.getUniqueId().toString());
						}
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(),new Runnable(){

							@Override
							public void run() {
								if(frozen.contains(p.getUniqueId().toString())){
									frozen.remove(p.getUniqueId().toString());
								}								
							}
						}, 20*5);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if(this.frozen.contains(p.getUniqueId().toString())){
			p.teleport(event.getFrom());
		}
	}

}
