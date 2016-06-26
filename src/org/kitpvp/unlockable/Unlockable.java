package org.kitpvp.unlockable;

import org.bukkit.ChatColor;
import org.kitpvp.core.Core;

public abstract class Unlockable {
	
	public enum Scarcity {
		
		WHITE("White", ChatColor.WHITE, 1),
		BLUE("Green", ChatColor.BLUE, .3),
		PURPLE("Aqua", ChatColor.DARK_PURPLE, .15),
		RED("Purple", ChatColor.RED, .05),
		DARK_RED("Red", ChatColor.DARK_RED, .02),
		GOLD("Dark Red", ChatColor.GOLD, .005),
		BLACK("Gold", ChatColor.DARK_GRAY, .001);
		
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
	
	private Scarcity scarcity;
	
	public Unlockable(Scarcity scarcity){
		this.scarcity = scarcity;
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
