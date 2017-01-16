package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import net.md_5.bungee.api.ChatColor;

public class ReportCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("report")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You are not a player!");
				return true;
			}
			if (args.length >= 2) {
				Player target = sender.getServer().getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player not found!");
				} else {
					String reason = "";
					int x = 0;
					for (String a : args) {
						if (x == 0) {
							x++;
							continue;
						}
						reason = reason + " " + a;
					}
					reason = reason.trim();
					sender.sendMessage(ChatColor.GREEN
							+ "Success! report sent, your report helps our server stay clean with unlegit players.");
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						User user = Core.getInstance().getUserManager().getUser(p);
						if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {
							p.sendMessage(ChatColor.GRAY + sender.getName() + " has reported " + target.getName()
									+ " for the reason: " + reason);
							;
						}
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Usage: /report <player> <reason>");
			}
		}
		return false;
	}

}
