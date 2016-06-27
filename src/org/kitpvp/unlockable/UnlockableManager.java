package org.kitpvp.unlockable;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.kitpvp.ability.abilities.Bash;
import org.kitpvp.ability.abilities.FeatherBoots;
import org.kitpvp.ability.abilities.FlameAura;
import org.kitpvp.ability.abilities.Flash;
import org.kitpvp.ability.abilities.Jesus;
import org.kitpvp.ability.abilities.QuickShot;
import org.kitpvp.ability.abilities.Shocker;
import org.kitpvp.ability.abilities.WindStorm;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.Unlockable.Scarcity;

public class UnlockableManager {

	private ArrayList<Unlockable> unlockables = new ArrayList<Unlockable>();
	
	public UnlockableManager(){
		registerUnlockables();
		registerAllListeners();
	}
	
	public void addUnlockable(Unlockable unlockable){
		if(!unlockable.isRegistered())
			unlockables.add(unlockable);
	}
	
	public void removeUnlockable(Unlockable unlockable){
		if(unlockable.isRegistered())
			unlockables.remove(unlockable);
	}
	
	public ArrayList<Unlockable> getRegisteredUnlockables(){
		return unlockables;
	}
	
	private void registerUnlockables(){
		//Abilities
		unlockables.add(new FeatherBoots());
		unlockables.add(new FlameAura());
		unlockables.add(new QuickShot());
		unlockables.add(new Flash());
		unlockables.add(new Shocker());
		unlockables.add(new Jesus());
		unlockables.add(new Bash());
		unlockables.add(new WindStorm());
	}
	private void registerAllListeners() {
		//If an ability implements Listener..
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new FeatherBoots(), Core.getInstance());
		pm.registerEvents(new Shocker(), Core.getInstance());
		pm.registerEvents(new Jesus(), Core.getInstance());
		pm.registerEvents(new Bash(), Core.getInstance());
	}
	
	public Scarcity getRandom(){
		Random random = new Random();
		double d = random.nextDouble();
		Scarcity result = Scarcity.WHITE;
		for(Scarcity scarcity : Scarcity.values()){
			if(scarcity.getChance() > d){
				if(!(result.getChance() < scarcity.getChance())){
					result = scarcity;
				}
			}
		}
		return result;
	}
	
}
