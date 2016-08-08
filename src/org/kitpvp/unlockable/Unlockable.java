
package org.kitpvp.unlockable;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
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
		
		
		Scarcity(String display, ChatColor color, double percentChance){
			this.display = display;
			this.color = color;
			this.percentChance = percentChance;
		}

		public String getDisplay(){
			return display;
		}
		
		public ChatColor getColor(){
			return color;
		}
		
		public double getChance(){
			return percentChance;
		}
	}
	
	private String name;
	private String description;
	private Scarcity scarcity;
	
	private ItemStack icon;
	
	public Unlockable(String name, String description, Scarcity scarcity){
		this.name = name;
		this.description = description;
		this.scarcity = scarcity;
	}
	
	protected void setIcon(ItemStack icon){
		this.icon = icon;
	}
	
	public ItemStack getIcon(){
		ItemStack itemStack = new ItemStack(Material.BARRIER);
		if(icon != null)
			itemStack = icon;
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(this.scarcity.getColor() + this.name);
		for(ItemFlag itf : ItemFlag.values()){
			im.addItemFlags(itf);
		}
		if(description != "")
			im.setLore(Arrays.asList(ChatColor.GRAY + description));
		itemStack.setItemMeta(im);
		return itemStack;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isRegistered(){
		if(Core.getInstance().getUnlockableManager().getRegisteredUnlockables().contains(this))
			return true;
		return false;
	}
	
	public Scarcity getScarcity(){
		return this.scarcity;
	}
	
}
