package org.kitpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("fly")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("fly")) {
					if (p.getAllowFlight()) {
						p.sendMessage(ChatColor.GRAY + "Disabled Flight!");
						p.setFlying(false);
						p.setAllowFlight(false);
					} else {
						p.sendMessage(ChatColor.GRAY + "Enabled Flight!");
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
