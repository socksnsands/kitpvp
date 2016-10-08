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

	STARTER_ABILITY(ChatColor.YELLOW + "Starter",
			"Starter series",
			Arrays.asList("Noob", "Heal"),
			Material.CHEST), 
	GOD_ABILITY(ChatColor.GOLD + "Gods", 
			"Premium+ series",
			Arrays.asList("Jesus", "Aphrodite", "Aphrodite II", "Poseidon", "Zeus", "Hades", "Hulk", "Monkey", "Dolphin"),
			Material.CHEST), 
	WAR_ABILITY(ChatColor.RED + "War", 
			"Premium series",
			Arrays.asList("Jet", "Explosive Grenade", "Flash Grenade", "Totem", "Shotgun", "Pirate", "Snail"),
			Material.CHEST), 
	HEIGHTS_ABILITY(
			ChatColor.RED + "Heights", 
			"Premium series",
			Arrays.asList("Anti-Stomper", "Anti-Stomper II", "Stomper", "Trampoline"),
			Material.CHEST), 
	SUPERHERO_ABILITY(
			ChatColor.RED + "Superhero", "Premium series",
			Arrays.asList("Flash", "Flame Aura", "Bulk Up", "Wind Storm", "Enrage",	"Jedi", "Anvil", "Spiderman", "Ninja", "Ground Slam", "Ice Blockade", "Paralysis Blockade"),
			Material.CHEST), 
	WINTER_ABILITY(
			ChatColor.AQUA + "Winter",
			"Limited edition: Winter 2016",
			Arrays.asList("Frosty", "Snowball Shotgun", "Frost Laser"),
			Material.SNOW_BLOCK), 
	LASER_ABILITY(ChatColor.RED + "Laser",
			"Premium series",
			Arrays.asList("Frost Laser", "Fire Laser",
			"Paralysis Laser", "Dark Laser",
			"Mythical Laser", "Damage Laser", "Damage Laser II", "Damage Laser III",
			"Swap Laser", "Electric Laser",
			"Earth Laser", "Cooldown Laser",
			"Omega Laser", "Distance Laser"),
			Material.CHEST),
	UNREAL_ABILITY(ChatColor.RED + "Unreal",
			"Premium series",
			Arrays.asList("Black Hole", "Body Shield"),
			Material.ENDER_CHEST);

	private String display;
	private String description;
	private List<Unlockable> unlockables;
	private Material icon;

	UnlockableSeries(String display, String description, List<String> unlockables, Material icon) {
		this.display = display;
		this.description = description;
		List<Unlockable> us = new ArrayList<Unlockable>();
		for (String name : unlockables)
			if (Core.getInstance().getUnlockableManager().isUnlockable(name))
				us.add(Core.getInstance().getUnlockableManager().getUnlockable(name));
		this.unlockables = us;
		this.icon = icon;
	}

	public String getDisplay() {
		return this.display;
	}

	public String getDescription() {
		return this.description;
	}

	public List<Unlockable> getUnlockables() {
		return this.unlockables;
	}

	public Material getIconMaterial() {
		return this.icon;
	}

	public ItemStack getIcon() {
		ItemStack icon = new ItemStack(this.icon);
		ItemMeta im = icon.getItemMeta();
		im.setDisplayName(ChatColor.GRAY + this.display);
		List<String> lore = new ArrayList<>();
		lore.addAll(Arrays.asList(ChatColor.GRAY + this.description, "", ChatColor.GRAY + "Could contain:"));
		this.cleanupUnlockablesList();
		for (Unlockable unlockable : unlockables) {
			lore.add(ChatColor.GRAY + " - " + unlockable.getScarcity().getColor() + unlockable.getName());
		}
		im.setLore(lore);
		icon.setItemMeta(im);
		return icon;
	}

	private void cleanupUnlockablesList() {
		boolean runAgain = false;
		for (Unlockable unlockable : this.unlockables) {
			int index = unlockables.lastIndexOf(unlockable);
			if (index - 1 != -1) {
				if (Core.getInstance().getUnlockableManager().isMoreRare(unlockables.get(index - 1), unlockable)) {
					unlockables.set(index, unlockables.get(index - 1));
					unlockables.set(index - 1, unlockable);
					runAgain = true;
				}
			}
		}
		if (runAgain)
			cleanupUnlockablesList();
	}

}
