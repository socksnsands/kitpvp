package org.kitpvp.user.rank;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(ChatColor.WHITE, "", 2, 0),
	D1(ChatColor.GREEN, "vip", 3, 1),
	D2(ChatColor.BLUE, "mvp", 4, 2),
	D3(ChatColor.GOLD, "pro", 5, 4),
	FAMOUS(ChatColor.AQUA, "famous", 9, 1),
	JRMOD(ChatColor.LIGHT_PURPLE, "jr.mod", 9, 1),
	MOD(ChatColor.LIGHT_PURPLE, "mod", 9, 1),
	SRMOD(ChatColor.LIGHT_PURPLE, "sr.mod", 9, 1),
	ADMIN(ChatColor.RED, "admin", 9, 5),
	DEVELOPER(ChatColor.DARK_AQUA, "dev", 9, 5);

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
