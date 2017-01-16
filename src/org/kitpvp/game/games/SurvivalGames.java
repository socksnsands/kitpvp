package org.kitpvp.game.games;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.kitpvp.core.Core;
import org.kitpvp.game.Game;
import org.kitpvp.unlockable.UnlockableSeries;

public class SurvivalGames extends Game {

	public SurvivalGames() {
		super("SG", new Location(Bukkit.getWorld("world"), 136, 4, -22), 2, 24);
		super.addReward(UnlockableSeries.SG, 1);
		super.addReward(Core.getInstance().getAbilityManager().getAbility("Fotified"), 1);
		super.setWinMessage(ChatColor.GRAY + "You won: " + ChatColor.GOLD + "1x SG Series" + ChatColor.GRAY + ", " + ChatColor.GOLD + "1x Fortified Ability" + ChatColor.GRAY + "!");
	}

}
