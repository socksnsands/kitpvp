package org.kitpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import net.md_5.bungee.api.ChatColor;

public class HealCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player) sender;
		if (lbl.equalsIgnoreCase("heal")) {
			User user = Core.getInstance().getUserManager().getUser(p);
			if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {
				p.setHealth(20);
				p.sendMessage(ChatColor.GRAY + "Health Replenished");
			} else {

				p.sendMessage(ChatColor.RED + "You do not have permission to heal yourself.");
			}

		}
		return false;
	}
}
