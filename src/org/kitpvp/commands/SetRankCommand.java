package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

public class SetRankCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

		if (cmd.getName().equalsIgnoreCase("setrank")) {
			if (!sender.isOp())
				return false;
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Invalid format: /setrank (player) (rank)");
			}
			Player target = null;
			boolean foundTarget = false;
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (args[0].equalsIgnoreCase(players.getName())) {
					target = players;
					foundTarget = true;
				}
			}
			if (foundTarget) {
				if (target != null) {
					User user = Core.getInstance().getUserManager().getUser(target);
					try {
						user.setRank(Rank.valueOf(args[1].toUpperCase()));
						sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + " to rank "
								+ Rank.valueOf(args[1].toUpperCase()).getColor() + args[1].toLowerCase()
								+ ChatColor.GREEN + "!");
					} catch (Exception ex) {
						sender.sendMessage(ChatColor.RED + "Rank \"" + args[1] + "\" not found!");
						sender.sendMessage(ChatColor.RED + "Rank list:");
						for (Rank rank : Rank.values()) {
							sender.sendMessage(ChatColor.GRAY + " - " + rank.getColor() + rank.toString());
						}
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Player not found!");
			}
		}
		return false;
	}

}
