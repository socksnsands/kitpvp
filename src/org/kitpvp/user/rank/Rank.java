package org.kitpvp.user.rank;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(ChatColor.WHITE, 2, 0), D1(ChatColor.GREEN, 3, 1), D2(ChatColor.DARK_GREEN, 4, 2), D3(ChatColor.YELLOW, 5, 3), D4(ChatColor.GOLD, 7, 5), MOD(ChatColor.LIGHT_PURPLE, 9, 1), ADMIN(ChatColor.RED, 9, 3), OWNER(ChatColor.DARK_RED, 9, 5);

	private ChatColor color;
	private int maxLoadouts;
	private int bonusPercent;

	Rank(ChatColor color, int maxLoadouts, int bonusPercent) {
		this.color = color;
		this.maxLoadouts = maxLoadouts;
		this.bonusPercent = bonusPercent;
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
