package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import net.md_5.bungee.api.ChatColor;

public class MuteChatCommand implements CommandExecutor, Listener {
	
	
	
	private boolean muted = false;
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		
		Player p = e.getPlayer();
		User user = Core.getInstance().getUserManager().getUser(p);
		if(muted == true){
			if(user.getRank().getValue() < Rank.JRMOD.getValue()){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "The chat is currently muted");
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(lbl.equalsIgnoreCase("mc")){
			
			if (sender instanceof Player) {
				Player p = (Player) sender;
				User user = Core.getInstance().getUserManager().getUser(p);
				if (user.getRank().getValue() >= Rank.JRMOD.getValue()) {
					if(muted == false){
						muted = true;
						Bukkit.broadcastMessage(ChatColor.RED + "The chat has been locked by " + ChatColor.GOLD + p.getName());
					}else{
						muted = false;
						Bukkit.broadcastMessage(ChatColor.GREEN + "The chat has been unlocked by" + ChatColor.GOLD + p.getName());
					}
				} else {
					p.sendMessage(ChatColor.RED + "No Permission!");
				}
			} else {
				sender.sendMessage("You must be a player to run this command.");
			}
			
		}
		
		return false;
	}
	
	

}
