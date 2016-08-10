package org.kitpvp.unlockable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.core.Core;

public enum UnlockableSeries {
	
	STARTER_ABILITY (ChatColor.YELLOW + "Starter", "Starter series", Arrays.asList("Noob", "Heal"), Material.CHEST),
	GOD_ABILITY (ChatColor.RED + "Gods", "Premium series", Arrays.asList("Jesus", "Aphrodite", "Poseidon", "Zeus", "Hades", "Vovl"), Material.CHEST),
	WAR_ABILITY (ChatColor.RED +"War", "Premium series", Arrays.asList("Jet", "Explosive Grenade"), Material.CHEST),
	HEIGHTS_ABILITY (ChatColor.RED + "Heights", "Premium series", Arrays.asList("Anti-Stomper", "Stomper", "Trampoline"), Material.CHEST),
	SUPERHERO_ABILITY (ChatColor.RED + "Superhero", "Premium series", Arrays.asList("Flash", "Flame Aura", "Bulk Up", "Wind Storm" , "Enrage", "Jedi"), Material.CHEST),
	STAFF_ALL_ABILITY (ChatColor.GOLD + "Staff", "Staff only series", Arrays.asList("Jesus", "Bulk Up", "Feather Boots", "Flame Aura", "Flash", "Ignite", "Quick Shot", "Shocker", "Totem", "Wind Storm", "Frosty", "Snowball Shotgun"), Material.ENDER_CHEST),
	DEV_ABILITY(ChatColor.BLUE + "Dev", "Dev testing series", Arrays.asList("Jesus", "Swap Laser"), Material.CHEST),
	WINTER_ABILITY (ChatColor.AQUA + "Winter", "Limited edition: Winter 2016", Arrays.asList("Frosty", "Snowball Shotgun", "Frost Laser"), Material.SNOW_BLOCK),
	LASER_ABILITY (ChatColor.RED + "Laser", "Premium series", Arrays.asList("Frost Laser", "Fire Laser", "Paralysis Laser", "Dark Laser", "Mythical Laser", "Damage Laser", "Swap Laser", "Electric Laser", "Earth Laser", "Cooldown Laser"), Material.CHEST),
	OMEGA_ABILITY (ChatColor.LIGHT_PURPLE + "Omega Set", "Premium series", Arrays.asList(""), Material.CHEST);
	
	private String display;
	private String description;
	private List<Unlockable> unlockables;
	private Material icon;
	
	UnlockableSeries(String display, String description, List<String> unlockables, Material icon){
		this.display = display;
		this.description = description;
		List<Unlockable> us = new ArrayList<Unlockable>();
		for(String name : unlockables)
			if(Core.getInstance().getUnlockableManager().isUnlockable(name))
				us.add(Core.getInstance().getUnlockableManager().getUnlockable(name));
		this.unlockables = us;
		this.icon = icon;
	}
	
	public String getDisplay(){
		return this.display;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public List<Unlockable> getUnlockables(){
		return this.unlockables;
	}
	
	public Material getIconMaterial(){
		return this.icon;
	}
	
	public ItemStack getIcon(){
		ItemStack icon = new ItemStack(this.icon);
		ItemMeta im = icon.getItemMeta();
		im.setDisplayName(ChatColor.GRAY + this.display);
		List<String> lore = new ArrayList<>();
		lore.addAll(Arrays.asList(ChatColor.GRAY + this.description, "", ChatColor.GRAY + "Could contain:"));
		this.cleanupUnlockablesList();
		for(Unlockable unlockable : unlockables){
			lore.add(ChatColor.GRAY + " - " + unlockable.getScarcity().getColor() + unlockable.getName());
		}
		im.setLore(lore);
		icon.setItemMeta(im);
		return icon;
	}
	
	private void cleanupUnlockablesList(){
		boolean runAgain = false;
		for(Unlockable unlockable : this.unlockables){
			int index = unlockables.lastIndexOf(unlockable);
			if(index - 1 != -1){
				if(Core.getInstance().getUnlockableManager().isMoreRare(unlockables.get(index - 1), unlockable)){
					unlockables.set(index, unlockables.get(index-1));
					unlockables.set(index-1, unlockable);
					runAgain = true;
				}
			}
		}
		if(runAgain)
			cleanupUnlockablesList();
	}
	
}
