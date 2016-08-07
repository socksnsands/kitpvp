package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.rank.Rank;


public class SetRankCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

		if (cmd.getName().equalsIgnoreCase("setrank")) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (args[0].equalsIgnoreCase(players.getName())) {
					if (args[1].toUpperCase() == "DEFAULT") {
						Core.getInstance().getUserManager().getUser(players).setRank(Rank.DEFAULT);
					} 
					else if (args[1].toUpperCase() == "STAFF") {
						Core.getInstance().getUserManager().getUser(players).setRank(Rank.STAFF);
					}
					else if (args[1].toUpperCase() == "ADMIN") {
						Core.getInstance().getUserManager().getUser(players).setRank(Rank.ADMIN);
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid Rank!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Player not found!");
				}
			}
		}
		return false;
	}

}
