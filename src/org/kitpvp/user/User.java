package org.kitpvp.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;
import org.kitpvp.unlockable.Unlockable;

public class User {

	private Player player;
	
	private ArrayList<Unlockable> ownedUnlockables = new ArrayList<Unlockable>();
	private ArrayList<Ability> activeAbilities = new ArrayList<Ability>();
	private HashMap<Ability, Integer> cooldowns = new HashMap<Ability, Integer>();
	
	private boolean isSafe = true;
	
	public User(Player player){
		this.player = player;
		
		//TODO load unlockables
	}
	
	public void addUnlockable(Unlockable unlockable){
		if(!ownedUnlockables.contains(unlockable)){
			//TODO save unlockable to db
			ownedUnlockables.add(unlockable);
		}
	}
	
	public void removeUnlockable(Unlockable unlockable){
		if(ownedUnlockables.contains(unlockable)){
			//TODO remove unlockable from db
			ownedUnlockables.remove(unlockable);
		}
	}
	
	public ArrayList<Ability> getActiveAbilities(){
		return this.activeAbilities;
	}
	
	public boolean isOnCooldown(Ability ability){
		return cooldowns.containsKey(ability);
	}
	
	public int getRemainingCooldown(Ability ability){
		if(isOnCooldown(ability))
			return cooldowns.get(ability);
		return -1;
	}
	
	public void clearCooldowns(){
		this.cooldowns.clear();
	}
	
	public void setSafe(boolean safe){
		this.isSafe = safe;
	}
	
	public boolean isSafe(){
		return isSafe;
	}
	
	public void callCooldownTick(){
		for(Ability ability : cooldowns.keySet()){
			cooldowns.put(ability, cooldowns.get(ability)-1);
			if(cooldowns.get(ability) == 0){
				cooldowns.remove(ability);
				ability.onFinishCooldown(player, ability);
			}
		}
	}
	
	public void addCooldown(Ability ability){
		if(cooldowns.containsKey(ability))
			cooldowns.remove(ability);
		cooldowns.put(ability, ability.getCooldownTicks());
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public ArrayList<Unlockable> getOwnedUnlockables(){
		return this.ownedUnlockables;
	}
	
}
