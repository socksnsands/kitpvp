package org.kitpvp.user;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kitpvp.core.Core;

public class UserManager implements Listener {

	private ArrayList<User> users = new ArrayList<User>();
	
	public UserManager(){
		initialLoad();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	public User getUser(Player player){
		for(User user : users)
			if(user.getPlayer().equals(player))
				return user;
		return null;
	}
	
	public boolean isLoadedUser(Player player){
		for(User user : users)
			if(user.getPlayer().equals(player))
				return true;
		return false;
	}
	
	public ArrayList<User> getUsers(){
		return this.users;
	}
	
	private void initialLoad(){
		if(!users.isEmpty())
			return;
		for(Player player : Bukkit.getServer().getOnlinePlayers())
			users.add(new User(player));
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(this.getUser(player).isSafe()){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		for(User user : users)
			if(user.getPlayer().equals(event.getPlayer()))
				return;
		users.add(new User(event.getPlayer()));
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		leave(event.getPlayer());	
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
			leave(event.getPlayer());
	}
	
	private void leave(Player player){
		if(!isLoadedUser(player))
			return;
		users.remove(getUser(player));
	}
	
	
}
