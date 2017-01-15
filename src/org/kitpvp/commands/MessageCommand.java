package org.kitpvp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;

public class MessageCommand implements CommandExecutor {

		private HashMap<String, String> reply = new HashMap<String, String>();
	
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
		        if (t != null){
		        	String msg = ChatColor.GRAY + "(" + Core.getInstance().getUserManager().getUser(p).getRank().getColor() + p.getName() + ChatColor.GRAY + " -> " + Core.getInstance().getUserManager().getUser(t).getRank().getColor() + t.getName() + ChatColor.GRAY + "): " + m;
		       	 	p.sendMessage(msg);
		    		t.sendMessage(msg);
		    		reply.put(p.getName(), t.getName());
		    		reply.put(t.getName(), p.getName());
		        }else{
		        	p.sendMessage(ChatColor.RED.toString() + args[0] + " is not online.");
		        }
		      }
		    }else{
		    	if(lbl.equalsIgnoreCase("r")){
		    		if(reply.get(p) != null){
		    			Player t = Bukkit.getPlayer(reply.get(p.getName()));
		    			 String m = "";
		 		        for (int i = 1; i < args.length; i++) {
		 		          m = m + (i > 1 ? " " : "") + args[i];
		 		        }
		 		        if(t != null){
		 		        	String msg = ChatColor.GRAY + "(" + Core.getInstance().getUserManager().getUser(p).getRank().getColor() + p.getName() + ChatColor.GRAY + " -> " + Core.getInstance().getUserManager().getUser(t).getRank().getColor() + t.getName() + ChatColor.GRAY + "): " + m;
		 		        	p.sendMessage(msg);
		    				t.sendMessage(msg);
		 		        }else{
		 		        	p.sendMessage(ChatColor.RED + reply.get(p.getName()) + " is not online.");
		 		        }
		    		}else{
		    			p.sendMessage(ChatColor.RED + "You have nobody to reply to.");
		    		}
		    	}
		    }
			return false;
		  }
}