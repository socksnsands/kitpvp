package org.kitpvp.cheat.cheats.detection;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.kitpvp.core.Core;

import static org.kitpvp.cheat.Cheat.Response.*;

public class PvpListener implements Listener {

//	@EventHandler
//	public void onDamage(EntityDamageByEntityEvent event){
//		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
//			Player damager = (Player) event.getDamager();
//			Player hit = (Player) event.getEntity();
//			if(!damager.getGameMode().equals(GameMode.CREATIVE)){
//				if(Core.getInstance().getCheatManager().getCheat("reach").eval(damager, hit, damager.getName().equals("_Ug")).equals(YES)){
////					damager.sendMessage(ChatColor.RED + "It looks like you may be using reach, but I'm also really dumb.");
////					event.setCancelled(true);
//					Core.getInstance().getUserManager().getUser(damager).addOffense(Core.getInstance().getCheatManager().getCheat("reach"));
//				}
//			}
//		}
//	}
	
}
