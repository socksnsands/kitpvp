package org.kitpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import net.md_5.bungee.api.ChatColor;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("fly")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				User user = Core.getInstance().getUserManager().getUser(p);
				if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {
					if (p.getAllowFlight()) {
						p.sendMessage(ChatColor.RED + "Disabled flight!");
						p.setFlying(false);
						p.setAllowFlight(false);
					} else {
						p.sendMessage(ChatColor.GREEN + "Enabled flight!");
						p.setAllowFlight(true);
						p.setFlying(true);
					}
				} else {
					p.sendMessage(ChatColor.RED + "No Permission!");
				}
			} else {
				sender.sendMessage("Must be a player to run this command!");
			}
		}
		return false;
	}
}
