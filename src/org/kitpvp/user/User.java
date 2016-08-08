package org.kitpvp.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.loadout.Loadout;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.user.rank.Rank;
import org.kitpvp.util.ItemManager;

public class User {

	private Player player;
	
	private HashMap<Unlockable, Integer> ownedUnlockables = new HashMap<Unlockable, Integer>();
	private HashMap<UnlockableSeries, Integer> ownedSeries = new HashMap<UnlockableSeries, Integer>();
	private ArrayList<Ability> activeAbilities = new ArrayList<Ability>();
	private HashMap<Ability, Integer> cooldowns = new HashMap<Ability, Integer>();
	private ArrayList<Loadout> loadouts = new ArrayList<Loadout>();
	private Loadout activeLoadout;
	
	private Rank rank;
	
	private int balance = 0;
		
	private boolean isSafe = true;
	
	public User(Player player){
		this.player = player;
		
		rank = Rank.DEFAULT;
		//TODO load unlockables & series & balance & loadouts & rank
	}
	
	public Loadout getActiveLoadout(){
		return this.activeLoadout;
	}
	
	public void restartCooldowns(){
		for(Ability ability : this.getActiveAbilities()){
			if(!ability.getName().equalsIgnoreCase("Jet"))
				this.cooldowns.put(ability, ability.getCooldownTicks());
			else
				this.cooldowns.put(ability, 20*70);
		}
	}
	
	public Rank getRank(){
		return this.rank;
	}
	
	public void setRank(Rank rank){
		this.rank = rank;
		//TODO save to config
	}
	
	public void setCooldown(Ability ability, int cooldown){
		this.cooldowns.put(ability, cooldown);
	}
	
	public void resetInventory(){
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getActivePotionEffects().clear();
		player.setFlying(false);
		player.setFallDistance(0);
		player.setFireTicks(0);
		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
	}
	
	public void addSeries(UnlockableSeries series){
		if(ownedSeries.containsKey(series))
			ownedSeries.put(series, ownedSeries.get(series) + 1);
		else
			ownedSeries.put(series, 1);
		//TODO save to db
	}
	
	public void removeMoney(int amount){
		this.balance -= amount;
		if(this.balance < 0)
			this.balance = 0;
	}
	
	public void addMoney(int amount){
		this.balance += amount;
	}
	
	public int getBalance(){
		return this.balance;
	}
	
	public void removeSeries(UnlockableSeries series){
		if(ownedSeries.containsKey(series))
			if(ownedSeries.get(series) > 0)
				ownedSeries.put(series, ownedSeries.get(series)-1);
		//TODO save to db
	}
	
	public int getQuantityOfSeries(UnlockableSeries series){
		if(ownedSeries.containsKey(series))
			return ownedSeries.get(series);
		return 0;
	}
	
	public void addUnlockable(Unlockable unlockable){
		if(this.ownedUnlockables.containsKey(unlockable))
			this.ownedUnlockables.put(unlockable, this.ownedUnlockables.get(unlockable) + 1);
		else
			this.ownedUnlockables.put(unlockable, 1);
			//TODO save unlockable to db
	}
	
	public void removeUnlockable(Unlockable unlockable){
		if(ownedUnlockables.containsKey(unlockable)){
			if(ownedUnlockables.get(unlockable) > 1){
				ownedUnlockables.put(unlockable, ownedUnlockables.get(unlockable) - 1);
			}else{
				ownedUnlockables.remove(unlockable);
			}
		}
		//TODO remove unlockable from db
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
	
	public void giveSpawnInventory(){
		this.resetInventory();
		
		Inventory inv = player.getInventory();
		ItemManager im = Core.getInstance().getItemManager();
		inv.setItem(0, im.getFFAItem());
		inv.setItem(8, im.getUnlockableOpener());
	}
	
	public void openKitSelector(){
		Inventory inv = Bukkit.getServer().createInventory(player, 9, ChatColor.UNDERLINE + "Kit Selector");
		for(Loadout loadout : this.loadouts){
			inv.addItem(loadout.getSelectableIcon());
		}
		player.openInventory(inv);
	}
	
	public void openKitEditor(){
		Inventory ke = Bukkit.getServer().createInventory(player, 9, ChatColor.UNDERLINE + "Kit Editor");
		for(int i = 0; i < ((this.loadouts.size() > 9) ? 9 : this.loadouts.size()); i++){
			if(this.loadouts.get(i) != null){
				Loadout loadout = this.loadouts.get(i);
			ArrayList<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GRAY + "Abilities:");
			
			for(Ability ability : loadout.getAbilities()){
				lore.add(ChatColor.GRAY + " - " + ability.getScarcity().getColor() + ability.getName());
			}
			ke.addItem(Core.getInstance().getItemManager().createItem(ChatColor.translateAlternateColorCodes('&', loadout.getName()), Material.PISTON_BASE,(byte) 0, 1, lore));
			}
		}
		if(this.loadouts.size() < rank.getMaxLoadouts()){
			int d = rank.getMaxLoadouts() - this.loadouts.size();
			for(int i = 0; i < d; i++){
				ke.setItem(ke.firstEmpty(), Core.getInstance().getItemManager().createItem(ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "Empty", Material.ANVIL, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + "Click to create loadout!")));
			}
		}
		player.openInventory(ke);
	}
	
	public boolean isSafe(){
		return isSafe;
	}
	
	public void callCooldownTick(){
		ArrayList<Ability> toRemove = new ArrayList<Ability>();
		for(Ability ability : cooldowns.keySet()){
			cooldowns.put(ability, cooldowns.get(ability)-1);
			if(cooldowns.get(ability) == 0){
				toRemove.add(ability);
				ability.onFinishCooldown(player, ability);
			}
		}
		for(Ability ability : toRemove){
			cooldowns.remove(ability);
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
		ArrayList<Unlockable> unlockables = new ArrayList<Unlockable>();
		for(Unlockable unlockable: this.ownedUnlockables.keySet())
			unlockables.add(unlockable);
		return unlockables;
	}
	
	public HashMap<Unlockable, Integer> getOwnedUnlockablesAsHashmap(){
		return this.ownedUnlockables;
	}
	
	public void addLoadout(Loadout loadout){
		if(!this.loadouts.contains(loadout))
			this.loadouts.add(loadout);
	}
	
	public ArrayList<Loadout> getLoadouts(){
		return this.loadouts;
	}
	
	public boolean hasLoadout(String name){
		for(Loadout loadout : this.loadouts){
			if(ChatColor.stripColor(loadout.getName()).equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	public Loadout readLoadoutString(String string){
		String name = string.split("~", 2)[0];
		String rest = string.split("~", 2)[1];
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		while(rest.contains("~")){
			if(Core.getInstance().getAbilityManager().isAbility(rest.split("~", 2)[0]))
				abilities.add(Core.getInstance().getAbilityManager().getAbility(rest.split("~", 2)[0]));
			rest = rest.split("~", 2)[1];
		}
		for(Ability ability : abilities){
			if(!this.getOwnedAbilities().contains(ability)){
				if(!player.isOp()){
					abilities.remove(ability);
				}else{
					player.sendMessage(ChatColor.GRAY + "Was going to remove " + ability.getScarcity().getColor() + ability.getName() + ChatColor.GRAY + " as you don't own it but since you are OP you can have it.");
				}
			}
		}
		Loadout loadout = new Loadout(name, abilities);
		if(loadout.getPointValue() > loadout.getMaxPoints()){
			return null;
		}
		return loadout;
	}
	
	public boolean isLoadoutStringTooExpensive(String string){
		return this.readLoadoutString(string) == null;
	}

	public void setActiveLoadout(Loadout loadout){
		this.activeLoadout = loadout;
	}
	
	public void removeActiveLoadout(){
		this.activeLoadout = null;
	}
	
	public Loadout getLoadout(String name){
		for(Loadout loadout : this.loadouts){
			if(ChatColor.stripColor(loadout.getName()).equalsIgnoreCase(name)){
				return loadout;
			}
		}
		return null;
	}
	
	public ArrayList<Ability> getOwnedAbilities(){
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for(Unlockable unlockable : this.ownedUnlockables.keySet())
			if(unlockable instanceof Ability)
				abilities.add((Ability)unlockable);
		return abilities;
	}
	
}
