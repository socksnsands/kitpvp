
package org.kitpvp.unlockable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
//import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.core.Core;

public abstract class Unlockable {

	public enum Scarcity {

		WHITE("White", ChatColor.WHITE, 1), 
		BLUE("Blue", ChatColor.BLUE, .5), 
		PURPLE("Purple", ChatColor.DARK_PURPLE, .2), 
		RED("Red", ChatColor.RED, .05), 
		DARK_RED("Dark Red", ChatColor.DARK_RED, .02), 
		GOLD("Gold", ChatColor.GOLD, .005), 
		BLACK("Black", ChatColor.DARK_GRAY, .001);

		private String display;
		private ChatColor color;
		private double percentChance;

		Scarcity(String display, ChatColor color, double percentChance) {
			this.display = display;
			this.color = color;
			this.percentChance = percentChance;
		}

		public String getDisplay() {
			return display;
		}

		public ChatColor getColor() {
			return color;
		}

		public double getChance() {
			return percentChance;
		}
	}

	private String name;
	private String description;
	private Scarcity scarcity;

	private ItemStack icon;

	public Unlockable(String name, String description, Scarcity scarcity) {
		this.name = name;
		this.description = description;
		this.scarcity = scarcity;
	}

	protected void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public ItemStack getIcon() {
		ItemStack itemStack = new ItemStack(Material.REDSTONE_BLOCK);
		if (icon != null)
			itemStack = icon;
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(this.scarcity.getColor() + this.name);
//		for (ItemFlag itf : ItemFlag.values()) {
//			im.addItemFlags(itf);
//		}
		if (description != ""){
			String desc = ChatColor.GRAY + description;
			if(desc.contains("_H"))
			desc = desc.replaceAll("_H", ChatColor.YELLOW.toString() + "");
			if(desc.contains("H_"))
			desc = desc.replaceAll("H_", ChatColor.GRAY.toString() + "");
			
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			
			List<String> strings = Arrays.asList(desc.split("_L_"));
			List<String> f = new ArrayList<String>();
			strings.forEach(s -> {
				f.add(ChatColor.GRAY + s.trim());
			});
			lore.addAll(f);
//			String re = desc;
//			while(re.length() > 18){
//				for(int i = 0; i < re.length()-18; i++){
//					if(re.charAt(i+18) == ' '){
//						String s = re.charAt(i + 16) + re.charAt(i + 17) + re.charAt(i + 18) + "";
//						lore.add(re.split(s, 1)[0] + s);
//						re = re.split(s, 1)[1];
//						break;
//					}
//				}
//			}
			im.setLore(lore);
		}
		itemStack.setItemMeta(im);
		return itemStack;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return this.description;
	}

	public boolean isRegistered() {
		if (Core.getInstance().getUnlockableManager().getRegisteredUnlockables().contains(this))
			return true;
		return false;
	}

	public Scarcity getScarcity() {
		return this.scarcity;
	}

}
