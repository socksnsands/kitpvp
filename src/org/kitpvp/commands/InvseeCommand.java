package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import net.md_5.bungee.api.ChatColor;

public class InvseeCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("invsee")) {

			User user = Core.getInstance().getUserManager().getUser(p);
			if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {

				if (args.length == 0) {
					p.sendMessage(ChatColor.GRAY + "Usage: /invsee (player)");
				} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					Inventory targetInv = targetPlayer.getInventory();
					p.openInventory(targetInv);
				} else {
					p.sendMessage(ChatColor.RED + "That player is not online!");
				}
			} else {
				p.sendMessage(ChatColor.WHITE + "Unknown Command. Type /help for help");
			}
			return false;
		}
		return false;
	}
}
