package org.kitpvp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class PingCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (((cmd.getName().equalsIgnoreCase("ping")) || (cmd.getName().equalsIgnoreCase("ms")))
				&& (p.hasPermission("ping"))) {
			if (args.length == 0) {
				User u = Core.getInstance().getUserManager().getUser(p);
				int ping = u.getPing();
				p.sendMessage(ChatColor.GRAY + "Your ping: " + this.getColor(ping) + ping);
			} else {
				if(args[0].equalsIgnoreCase("top")){
					ArrayList<User> online = Core.getInstance().getUserManager().getUsers();
					if(online.size() >= 1){
						User f = online.get(0);
						int ping = 1000;
						for(User us : online){
							int pi = us.getPing();
							if(pi < ping){
								f = us;
								ping = pi;
							}
						}
						p.sendMessage(ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Top ping:");
						p.sendMessage(ChatColor.GRAY + f.getPlayer().getName() + ": " + this.getColor(ping) + ping);
						
					}else{
						p.sendMessage(ChatColor.RED + "No players could be found!");
					}
				}else{
					Player t = Bukkit.getServer().getPlayer(args[0]);
					if (t != null) {
						User u = Core.getInstance().getUserManager().getUser(t);
						int ping = u.getPing();
						p.sendMessage(ChatColor.GRAY + t.getName() + "'s ping: " + this.getColor(ping) + ping);
					} else {
						p.sendMessage(ChatColor.RED + "That player is not online!");
					}
				}
			}
		}
		return false;
	}
	
	private ChatColor getColor(int ping){
		if(ping < 100){
			return ChatColor.GREEN;
		}
		if(ping < 200){
			return ChatColor.YELLOW;
		}
		return ChatColor.RED;
	}

}
