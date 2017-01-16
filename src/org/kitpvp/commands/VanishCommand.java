package org.kitpvp.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import net.md_5.bungee.api.ChatColor;

public class VanishCommand implements CommandExecutor {

	private ArrayList<UUID> vanished = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		if (lbl.equalsIgnoreCase("v") || lbl.equalsIgnoreCase("vanish")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				User user = Core.getInstance().getUserManager().getUser(p);
				if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {
					if (vanished.contains(p.getUniqueId())) {
						for (Player p1 : Bukkit.getOnlinePlayers()) {
							if (!p1.canSee(p)) {
								p1.showPlayer(p);
							}
						}
						if(vanished.contains(p.getUniqueId())){
							vanished.remove(p.getUniqueId());
						}
						p.sendMessage(
								ChatColor.GRAY + "You are no longer " + ChatColor.GRAY + "vanished" + ChatColor.GRAY + "!");
					} else {
						for (Player p1 : Bukkit.getOnlinePlayers()) {
							if (!(Core.getInstance().getUserManager().getUser(p1).getRank().getValue() >= Rank.JRMOD.getValue())) {
								p1.hidePlayer(p);
							}

						}
						p.sendMessage(
								ChatColor.GRAY + "You are now " + ChatColor.GRAY + "vanished" + ChatColor.GRAY + "!");
						if(!vanished.contains(p.getUniqueId())){
							vanished.add(p.getUniqueId());
						}
					}
				} else {
					p.sendMessage(ChatColor.RED + "No Permission!");
				}
			} else {
				sender.sendMessage("You must be a player to run this command.");
			}
		}

		return true;

	}

}
