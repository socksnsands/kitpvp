package org.kitpvp.user.rank;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(ChatColor.GRAY),
	STAFF(ChatColor.YELLOW),
	ADMIN(ChatColor.RED);
	
	private ChatColor color;
	
	Rank(ChatColor color){
		this.color = color;
	}
	
	public ChatColor getColor(){
		return this.color;
	}
	
}
