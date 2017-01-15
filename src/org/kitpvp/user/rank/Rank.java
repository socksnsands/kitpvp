package org.kitpvp.user.rank;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(ChatColor.WHITE, "", 2, 0, 1),
	D1(ChatColor.GREEN, "", 3, 1, 2),
	D2(ChatColor.BLUE, "", 4, 2, 3),
	D3(ChatColor.GOLD, "", 5, 4, 4),
	FAMOUS(ChatColor.AQUA, "", 9, 1, 5),
	JRMOD(ChatColor.LIGHT_PURPLE, "", 9, 1, 6),
	MOD(ChatColor.DARK_PURPLE, "", 9, 1, 7),
	ADMIN(ChatColor.RED, "", 9, 5, 8),
	DEVELOPER(ChatColor.DARK_AQUA, "", 9, 5, 8);

	private ChatColor color;
	private String prefix;
	private int maxLoadouts;
	private int bonusPercent;
	private int value;

	Rank(ChatColor color, String prefix, int maxLoadouts, int bonusPercent, int value) {
		this.color = color;
		this.prefix = prefix;
		this.maxLoadouts = maxLoadouts;
		this.bonusPercent = bonusPercent;
		this.value = value;
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
	
	public int getValue(){
		return value;
	}

}
