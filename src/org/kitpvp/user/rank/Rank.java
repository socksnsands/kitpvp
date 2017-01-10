package org.kitpvp.user.rank;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(ChatColor.WHITE, "", 2, 0),
	D1(ChatColor.GREEN, "", 3, 1),
	D2(ChatColor.BLUE, "", 4, 2),
	D3(ChatColor.GOLD, "", 5, 4),
	FAMOUS(ChatColor.AQUA, "", 9, 1),
	JRMOD(ChatColor.LIGHT_PURPLE, "", 9, 1),
	MOD(ChatColor.DARK_PURPLE, "", 9, 1),
	ADMIN(ChatColor.RED, "", 9, 5),
	DEVELOPER(ChatColor.DARK_AQUA, "", 9, 5);

	private ChatColor color;
	private String prefix;
	private int maxLoadouts;
	private int bonusPercent;

	Rank(ChatColor color, String prefix, int maxLoadouts, int bonusPercent) {
		this.color = color;
		this.prefix = prefix;
		this.maxLoadouts = maxLoadouts;
		this.bonusPercent = bonusPercent;
	}

	public String getPrefix(){
		return prefix;
	}
	
	public ChatColor getColor() {
		return this.color;
	}

	public int getMaxLoadouts() {
		return this.maxLoadouts;
	}
	
	public int getBonusPercent(){
		return this.bonusPercent;
	}

}
