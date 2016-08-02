package org.kitpvp.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitpvp.ability.AbilityManager;
import org.kitpvp.ability.abilities.objects.TotemManager;
import org.kitpvp.unlockable.UnlockableManager;
import org.kitpvp.user.UserManager;
import org.kitpvp.util.ItemManager;

public class Core extends JavaPlugin implements Listener {

	private static Core instance;
	
	private UnlockableManager unlockableManager;
	private AbilityManager abilityManager;
	private UserManager userManager;
	private TotemManager totemManager;
	private ItemManager itemManager;
	
	public void onEnable(){
		instance = this;
		
		unlockableManager = new UnlockableManager();
		abilityManager = new AbilityManager();
		userManager = new UserManager();
		totemManager = new TotemManager();
		itemManager = new ItemManager();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		registerCommands();
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent event){
		event.setMaxPlayers(100);
		event.setMotd(ChatColor.GOLD + "Kitpvp \n" + ChatColor.GRAY + "Test message of the day!");
	}
	
	public void onDisable(){
		this.stopServer("Server restarting!");
	}
	
	public void stopServer(String reason){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.kickPlayer(ChatColor.RED + reason);
		}
		Bukkit.getServer().shutdown();
	}
	
	private void registerCommands(){
		getCommand("dev").setExecutor(new DeveloperCommand());
	}
	
	public UnlockableManager getUnlockableManager(){
		return unlockableManager;
	}
	
	public AbilityManager getAbilityManager(){
		return this.abilityManager;
	}
	
	public UserManager getUserManager(){
		return this.userManager;
	}
	
	public TotemManager getTotemManager(){
		return this.totemManager;
	}
	
	public ItemManager getItemManager(){
		return this.itemManager;
	}
	
	public static Core getInstance(){
		if(instance == null)
			System.out.println("Instance is null");
		return instance;
	}
	
	
	
}
