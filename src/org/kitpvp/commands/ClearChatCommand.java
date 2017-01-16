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

public class ClearChatCommand implements CommandExecutor{
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		if (lbl.equalsIgnoreCase("cc") ) {
				Player p = (Player) sender;
				User user = Core.getInstance().getUserManager().getUser(p);
				if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {
					for(int i = 0; i < 100; i++){
						Bukkit.broadcastMessage(" ");
					}
					Bukkit.broadcastMessage(ChatColor.GRAY + "The chat has been cleared by " + ChatColor.GOLD + ChatColor.BOLD.toString()
					+ p.getName());
				} else {
					p.sendMessage(ChatColor.RED + "No Permission!");
				}
		}

		return true;

	}


}
