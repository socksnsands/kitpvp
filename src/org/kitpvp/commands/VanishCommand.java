package org.kitpvp.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class VanishCommand implements CommandExecutor {

	private static ArrayList<UUID> vanished = new ArrayList();

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		if (lbl.equalsIgnoreCase("v") || lbl.equalsIgnoreCase("vanish")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("vanish")) {
					if (vanished.contains(p.getUniqueId())) {
						for (Player p1 : Bukkit.getOnlinePlayers()) {
							if (!p1.hasPermission("vanish")) {
								p1.showPlayer(p);
							}
						}
						p.sendMessage(
								ChatColor.GRAY + "You are" + ChatColor.BOLD + "no longer " + ChatColor.GOLD + "Vanished" + ChatColor.GRAY + "!");
					} else {
						for (Player p1 : Bukkit.getOnlinePlayers()) {
							if (!p1.hasPermission("vanish")) {
								p1.hidePlayer(p);
							}

						}
						p.sendMessage(
								ChatColor.GRAY + "You are now " + ChatColor.GOLD + "Vanished" + ChatColor.GRAY + "!");
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
