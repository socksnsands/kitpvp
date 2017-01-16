package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Ping;

import net.md_5.bungee.api.ChatColor;

public class PingCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (((cmd.getName().equalsIgnoreCase("ping")) || (cmd.getName().equalsIgnoreCase("ms")))
				&& (p.hasPermission("ping"))) {
			if (args.length == 0) {
				p.sendMessage(ChatColor.GRAY + "Your ping: " + ChatColor.GREEN + Ping.getPing(p));
			} else {
				Player t = Bukkit.getServer().getPlayer(args[0]);
				if (t != null) {
					p.sendMessage(ChatColor.RED + t.getName() + "'s ping: " + ChatColor.GREEN + Ping.getPing(t));
				} else {
					p.sendMessage(ChatColor.RED + "That player is not online!");
				}
			}
		}
		return false;
	}

}
