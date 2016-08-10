package org.kitpvp.user.rank;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(ChatColor.WHITE, 2), STAFF(ChatColor.YELLOW, 3), ADMIN(ChatColor.RED, 7);

	private ChatColor color;
	private int maxLoadouts;

	Rank(ChatColor color, int maxLoadouts) {
		this.color = color;
		this.maxLoadouts = maxLoadouts;
	}

	public ChatColor getColor() {
		return this.color;
	}

	public int getMaxLoadouts() {
		return this.maxLoadouts;
	}

}
