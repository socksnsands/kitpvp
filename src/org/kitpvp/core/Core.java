package org.kitpvp.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.kitpvp.ability.AbilityManager;
import org.kitpvp.unlockable.UnlockableManager;
import org.kitpvp.user.UserManager;

public class Core extends JavaPlugin {

	private static Core instance;
	
	private UnlockableManager unlockableManager;
	private AbilityManager abilityManager;
	private UserManager userManager;
	
	public void onEnable(){
		instance = this;
		
		unlockableManager = new UnlockableManager();
		abilityManager = new AbilityManager();
		userManager = new UserManager();
		
		registerCommands();
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
	
	public static Core getInstance(){
		return instance;
	}
	
	
	
}
