package org.kitpvp.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

	private String uuid;

	private HashMap<Unlockable, Integer> ownedUnlockables = new HashMap<Unlockable, Integer>();
	private HashMap<UnlockableSeries, Integer> ownedSeries = new HashMap<UnlockableSeries, Integer>();
	private ArrayList<Ability> activeAbilities = new ArrayList<Ability>();
	private HashMap<Ability, Integer> cooldowns = new HashMap<Ability, Integer>();
	private ArrayList<Loadout> loadouts = new ArrayList<Loadout>();
	private Loadout activeLoadout;

	private Rank rank;

	private int balance = 0;

	private boolean isSafe = true;

	public User(String uuid) {
		this.uuid = uuid;
		
		rank = Rank.DEFAULT;
	}

	public String getUUID() {
		return this.uuid;
	}

	public Loadout getActiveLoadout() {
		return this.activeLoadout;
	}

	public void restartCooldowns() {
		for (Ability ability : this.getActiveAbilities()) {
			if (!ability.getName().equalsIgnoreCase("Jet"))
				this.cooldowns.put(ability, ability.getCooldownTicks());
			else
				this.cooldowns.put(ability, 20 * 70);
		}
	}

	public Rank getRank() {
		return this.rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public void setCooldown(Ability ability, int cooldown) {
		this.cooldowns.put(ability, cooldown);
	}

	public void resetInventory() {
		getPlayer().getInventory().clear();
		getPlayer().setHealth(20);
		getPlayer().setFoodLevel(20);
		getPlayer().getActivePotionEffects().clear();
		getPlayer().setFlying(false);
		getPlayer().setFallDistance(0);
		getPlayer().setFireTicks(0);
		getPlayer().setGameMode(GameMode.ADVENTURE);
		getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
		getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
		getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
		getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
	}

	public void addSeries(UnlockableSeries series) {
		if (ownedSeries.containsKey(series))
			ownedSeries.put(series, ownedSeries.get(series) + 1);
		else
			ownedSeries.put(series, 1);
	}

	public void removeMoney(int amount) {
		this.balance -= amount;
		if (this.balance < 0)
			this.balance = 0;
	}

	public void addMoney(int amount) {
		this.balance += amount;
	}

	public int getBalance() {
		return this.balance;
	}

	public void resetData() {
		this.ownedSeries.clear();
		this.ownedUnlockables.clear();
		this.balance = 0;
		this.loadouts.clear();
		this.rank = Rank.DEFAULT;
	}

	public void removeSeries(UnlockableSeries series) {
		if (ownedSeries.containsKey(series))
			if (ownedSeries.get(series) > 0)
				ownedSeries.put(series, ownedSeries.get(series) - 1);
		if (ownedSeries.get(series) == 0)
			ownedSeries.remove(series);
	}

	public int getQuantityOfSeries(UnlockableSeries series) {
		if (ownedSeries.containsKey(series))
			return ownedSeries.get(series);
		return 0;
	}

	public void addUnlockable(Unlockable unlockable) {
		if (this.ownedUnlockables.containsKey(unlockable))
			this.ownedUnlockables.put(unlockable, this.ownedUnlockables.get(unlockable) + 1);
		else
			this.ownedUnlockables.put(unlockable, 1);
	}

	public void removeUnlockable(Unlockable unlockable) {
		if (ownedUnlockables.containsKey(unlockable)) {
			if (ownedUnlockables.get(unlockable) > 1) {
				ownedUnlockables.put(unlockable, ownedUnlockables.get(unlockable) - 1);
			} else {
				ownedUnlockables.remove(unlockable);
			}
		}
	}

	public ArrayList<Ability> getActiveAbilities() {
		return this.activeAbilities;
	}

	public boolean isOnCooldown(Ability ability) {
		return cooldowns.containsKey(ability);
	}

	public int getRemainingCooldown(Ability ability) {
		if (isOnCooldown(ability))
			return cooldowns.get(ability);
		return -1;
	}

	public void clearCooldowns() {
		this.cooldowns.clear();
	}

	public void setSafe(boolean safe) {
		this.isSafe = safe;
	}

	public void giveSpawnInventory() {
		this.resetInventory();

		Inventory inv = getPlayer().getInventory();
		ItemManager im = Core.getInstance().getItemManager();
		inv.setItem(0, im.getFFAItem());
		inv.setItem(8, im.getUnlockableOpener());
	}

	public void openKitSelector() {
		Inventory inv = Bukkit.getServer().createInventory(getPlayer(), 9, ChatColor.UNDERLINE + "Kit Selector");
		for (Loadout loadout : this.loadouts) {
			inv.addItem(loadout.getSelectableIcon());
		}
		getPlayer().openInventory(inv);
	}

	public void openKitEditor() {
		Inventory ke = Bukkit.getServer().createInventory(getPlayer(), 9, ChatColor.UNDERLINE + "Kit Editor");
		for (int i = 0; i < ((this.loadouts.size() > 9) ? 9 : this.loadouts.size()); i++) {
			if (this.loadouts.get(i) != null) {
				Loadout loadout = this.loadouts.get(i);
				ArrayList<String> lore = new ArrayList<>();
				lore.add("");
				lore.add(ChatColor.GRAY + "Abilities:");

				for (Ability ability : loadout.getAbilities()) {
					lore.add(ChatColor.GRAY + " - " + ability.getScarcity().getColor() + ability.getName());
				}
				ke.addItem(Core.getInstance().getItemManager().createItem(
						ChatColor.translateAlternateColorCodes('&', loadout.getName()), Material.PISTON_BASE, (byte) 0,
						1, lore));
			}
		}
		if (this.loadouts.size() < rank.getMaxLoadouts()) {
			int d = rank.getMaxLoadouts() - this.loadouts.size();
			for (int i = 0; i < d; i++) {
				ke.setItem(ke.firstEmpty(),
						Core.getInstance().getItemManager().createItem(
								ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "Empty", Material.ANVIL, (byte) 0, 1,
								Arrays.asList("", ChatColor.GRAY + "Click to create loadout!")));
			}
		}
		getPlayer().openInventory(ke);
	}

	public boolean isSafe() {
		return isSafe;
	}

	public void callCooldownTick() {
		ArrayList<Ability> toRemove = new ArrayList<Ability>();
		for (Ability ability : cooldowns.keySet()) {
			cooldowns.put(ability, cooldowns.get(ability) - 1);
			if (cooldowns.get(ability) == 0) {
				toRemove.add(ability);
				ability.onFinishCooldown(getPlayer(), ability);
			}
		}
		for (Ability ability : toRemove) {
			cooldowns.remove(ability);
		}
	}

	public void addCooldown(Ability ability) {
		if (cooldowns.containsKey(ability))
			cooldowns.remove(ability);
		cooldowns.put(ability, ability.getCooldownTicks());
	}

	public Player getPlayer() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getUniqueId().toString().equals(this.uuid)) {
				return player;
			}
		}
		return null;

	}

	public ArrayList<Unlockable> getOwnedUnlockables() {
		ArrayList<Unlockable> unlockables = new ArrayList<Unlockable>();
		for (Unlockable unlockable : this.ownedUnlockables.keySet())
			unlockables.add(unlockable);
		return unlockables;
	}

	public HashMap<Unlockable, Integer> getOwnedUnlockablesAsHashmap() {
		return this.ownedUnlockables;
	}

	public void addLoadout(Loadout loadout) {
		if (!this.loadouts.contains(loadout))
			this.loadouts.add(loadout);
	}

	public ArrayList<Loadout> getLoadouts() {
		return this.loadouts;
	}

	public boolean hasLoadout(String name) {
		for (Loadout loadout : this.loadouts) {
			if (ChatColor.stripColor(loadout.getName()).equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isLoadoutStringTooExpensive(String string) {
		return this.readLoadoutString(string) == null;
	}

	public void setActiveLoadout(Loadout loadout) {
		this.activeLoadout = loadout;
	}

	public void removeActiveLoadout() {
		this.activeLoadout = null;
	}

	public Loadout getLoadout(String name) {
		for (Loadout loadout : this.loadouts) {
			if (ChatColor.stripColor(loadout.getName()).equalsIgnoreCase(name)) {
				return loadout;
			}
		}
		return null;
	}

	public ArrayList<Loadout> readAllLoadoutStrings(String name) {
		ArrayList<Loadout> loadouts = new ArrayList<Loadout>();
		String[] loadoutStrings = name.split("_");
		for (String string : loadoutStrings) {
			loadouts.add(this.readLoadoutString(string));
		}
		return loadouts;
	}

	public void setMoney(int amount) {
		this.balance = amount;
	}

	public Loadout readLoadoutString(String string) {
		String name = string.split("~", 2)[0];
		String rest = string.split("~", 2)[1];
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		while (rest.contains("~")) {
			if (Core.getInstance().getAbilityManager().isAbility(rest.split("~", 2)[0]))
				abilities.add(Core.getInstance().getAbilityManager().getAbility(rest.split("~", 2)[0]));
			rest = rest.split("~", 2)[1];
		}
		for (Ability ability : abilities) {
			if (!this.getOwnedAbilities().contains(ability)) {
				if (!getPlayer().isOp()) {
					abilities.remove(ability);
				} else {
					getPlayer().sendMessage(ChatColor.GRAY + "Was going to remove " + ability.getScarcity().getColor()
							+ ability.getName() + ChatColor.GRAY
							+ " as you don't own it but since you are OP you can have it.");
				}
			}
		}
		Loadout loadout = new Loadout(name, abilities);
		if (loadout.getPointValue() > loadout.getMaxPoints()) {
			return null;
		}
		return loadout;
	}

	public void readAndSaveAbilitiesString(String string) {
		HashMap<Ability, Integer> abilities = new HashMap<Ability, Integer>();
		String rest = string;
		while (rest.contains("~")) {
			String abilityFull = rest.split("~", 2)[0];
			String abilityName = abilityFull.split("_", 2)[0];
			String abilityQuantity = abilityFull.split("_", 2)[1];
			if (Core.getInstance().getAbilityManager().isAbility(abilityName))
				abilities.put(Core.getInstance().getAbilityManager().getAbility(abilityName),
						Integer.valueOf(abilityQuantity));
			rest = rest.split("~", 2)[1];
		}
		for (Ability ability : abilities.keySet()) {
			this.ownedUnlockables.put(ability, abilities.get(ability));
		}
	}

	public String encodeAllAbilities() {
		String encode = "";
		for (Ability ability : getOwnedAbilities()) {
			String name = ability.getName();
			int amount = this.ownedUnlockables.get(ability);
			encode += name + "_" + amount + "~";
		}
		return encode;
	}

	public void readAndSaveSeriesString(String string) {
		HashMap<UnlockableSeries, Integer> series = new HashMap<UnlockableSeries, Integer>();
		String rest = string;
		while (rest.contains("~")) {
			String seriesFull = rest.split("~", 2)[0];
			String seriesName = seriesFull.split("_", 2)[0];
			String seriesQuantity = seriesFull.split("_", 2)[1];
			if (this.isSeries(seriesName))
				series.put(this.getSeries(seriesName), Integer.valueOf(seriesQuantity));
			rest = rest.split("~", 2)[1];
		}
		for (UnlockableSeries s : series.keySet()) {
			this.ownedSeries.put(s, series.get(s));
		}
	}

	/**
	 * Does not work! (Unless used in readAndSaveSeries, where I am using it.
	 * Only meant for that (and others) I make. - _Ug.
	 */
	private boolean isSeries(String name) {
		for (UnlockableSeries series : UnlockableSeries.values()) {
			if (series.toString().replaceAll("_", " ").equals(name.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Does not work! (Unless used in readAndSaveSeries, where I am using it.
	 * Only meant for that (and others) I make. - _Ug.
	 */
	private UnlockableSeries getSeries(String name) {
		for (UnlockableSeries series : UnlockableSeries.values()) {
			if (series.toString().replaceAll("_", " ").equals(name.toUpperCase())) {
				return series;
			}
		}
		return null;
	}

	public String encodeAllSeries() {
		String encode = "";
		for (UnlockableSeries series : this.ownedSeries.keySet()) {
			String name = series.toString().replaceAll("_", " ");
			int amount = this.ownedSeries.get(series);
			encode += name + "_" + amount + "~";
		}
		return encode;
	}

	public ArrayList<Ability> getOwnedAbilities() {
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for (Unlockable unlockable : this.ownedUnlockables.keySet())
			if (unlockable instanceof Ability)
				abilities.add((Ability) unlockable);
		return abilities;
	}

}
