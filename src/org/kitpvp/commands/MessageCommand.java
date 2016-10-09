package org.kitpvp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

		HashMap<String, String> reply = new HashMap<String, String>();
	
		  public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
		  {
		    Player p = (Player)sender;
		    if ((lbl.equalsIgnoreCase("msg")) || (lbl.equalsIgnoreCase("message"))){
		      if (args.length == 0)
		      {
		        p.sendMessage(ChatColor.GRAY + "Usage: /msg (player) (message)");
		      }
		      else if (args.length == 1)
		      {
		        p.sendMessage(ChatColor.GRAY + "Usage: /msg (player) (message)");
		      }
		      else
		      {
		        String m = "";
		        for (int i = 1; i < args.length; i++) {
		          m = m + (i > 1 ? " " : "") + args[i];
		        }
		        Player t = Bukkit.getServer().getPlayer(args[0]);
		        if (t != null)
		        {
		       	 p.sendMessage(ChatColor.DARK_AQUA + "" +ChatColor.BOLD + sender.getName() + " -> " + ChatColor.AQUA + "" + t.getName() + ChatColor.AQUA + ": " + m);
		    		t.sendMessage(ChatColor.AQUA + "" + sender.getName() + ChatColor.BOLD + ChatColor.DARK_AQUA + " -> " + t.getName() + ChatColor.AQUA + ": " + m);
		    		reply.put(p.getName(), t.getName());
		    		reply.put(t.getName(), p.getName());
		        }
		      }
		    }else{
		    	if(lbl.equalsIgnoreCase("r")){
		    		if(reply.containsKey(p.getName())){
		    			Player t = Bukkit.getPlayer(reply.get(p.getName()));
		    			 String m = "";
		 		        for (int i = 1; i < args.length; i++) {
		 		          m = m + (i > 1 ? " " : "") + args[i];
		 		        }
		    			p.sendMessage(ChatColor.AQUA + "" + sender.getName() + ChatColor.BOLD + ChatColor.DARK_AQUA + " -> " + t.getName() + ChatColor.AQUA + ": " + m);
		    		}else{
		    			p.sendMessage(ChatColor.GRAY + "You have nobody to reply to.");
		    		}
		    	}
		    }
			return false;
		  }
}