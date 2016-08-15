package org.kitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

public class PayCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

		if (cmd.getName().equalsIgnoreCase("pay")) {
			if (!(sender instanceof Player))
				return false;
			Player player = (Player) sender;
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Invalid format: /pay (player) (money / ability:ability_name / series:series_name)");
				return false;
			}
			Player target = null;
			boolean foundTarget = false;
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (args[0].equalsIgnoreCase(players.getName())) {
					target = players;
					foundTarget = true;
				}
			}
			if (foundTarget) {
				if (target != null) {
					User user = Core.getInstance().getUserManager().getUser(player);
					User tUser = Core.getInstance().getUserManager().getUser(target);
					try {
						String requestedSend = args[1];
						if(requestedSend.startsWith("ability:")){
							String abilityName = requestedSend.split(":", 2)[1];
							abilityName = abilityName.replaceAll("_", " ");
							if(Core.getInstance().getAbilityManager().isAbility(abilityName)){
								Ability ability = Core.getInstance().getAbilityManager().getAbility(abilityName);
									if(user.getOwnedAbilities().contains(ability)){
										user.removeUnlockable(ability);
										tUser.addUnlockable(ability);
										player.sendMessage(ChatColor.GREEN + "Sent " + ability.getScarcity().getColor() +  ability.getName() + ChatColor.GREEN + " to " + tUser.getRank().getColor() + target.getName() + ChatColor.GREEN + "!");
										target.sendMessage(ChatColor.GREEN + "Received " + ability.getScarcity().getColor() +  ability.getName() + ChatColor.GREEN + " from " + user.getRank().getColor() + player.getName() + ChatColor.GREEN + "!");
									}else{
										player.sendMessage(ChatColor.RED + "You do not own " + ability.getScarcity() + ability.getName() + ChatColor.RED + "!");
									}
							}else{
								player.sendMessage(ChatColor.RED + "No ability found called " + abilityName + ChatColor.RED + "!");
							}
						}
						if(requestedSend.startsWith("series:")){
							String seriesName = requestedSend.split(":", 2)[1];
							seriesName = seriesName.replaceAll("_", " ");
							if(this.isUnlockableSeries(seriesName)){
								UnlockableSeries series = this.getUnlockableSeries(seriesName);
									if(user.getQuantityOfSeries(series) > 0){
										user.removeSeries(series);
										tUser.addSeries(series);
										player.sendMessage(ChatColor.GREEN + "Sent " + series.getDisplay() + ChatColor.GREEN + " series to " + tUser.getRank().getColor() + target.getName() + ChatColor.GREEN + "!");
										target.sendMessage(ChatColor.GREEN + "Received " + series.getDisplay() + ChatColor.GREEN + " series from " + user.getRank().getColor() + player.getName() + ChatColor.GREEN + "!");
									}else{
										player.sendMessage(ChatColor.RED + "You do not own any " + series.getDisplay() + ChatColor.RED + " series!");
									}
							}else{
								player.sendMessage(ChatColor.RED + "No series found called " + seriesName + ChatColor.RED + "!");
							}
						}
						if(this.isInteger(requestedSend)){
							int amount = this.getInteger(requestedSend);
							if(amount > 0){
								if(user.getBalance() >= amount){
									user.removeMoney(amount);
									tUser.addMoney(amount);
									player.sendMessage(ChatColor.GREEN + "Sent " + amount + " coins(s) to " + target.getName());
									target.sendMessage(ChatColor.GREEN + "Received " + amount + " coin(s) from " + player.getName());
								}else{
									player.sendMessage(ChatColor.RED + "You cannot afford to send " + amount + " coin(s) as you only have " + user.getBalance() + " coin(s)!");
								}
							}else{
								player.sendMessage(ChatColor.RED + "You cannot send less than 1 coin!");
							}
						}
						user.setRank(Rank.valueOf(args[1].toUpperCase()));
						sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + " to rank "
								+ Rank.valueOf(args[1].toUpperCase()).getColor() + args[1].toLowerCase()
								+ ChatColor.GREEN + "!");
						target.sendMessage(ChatColor.GREEN + "Your rank was set to " + Rank.valueOf(args[1].toUpperCase()).getColor() + args[1].toLowerCase() + ChatColor.GREEN +"!");
					} catch (Exception ex) {
						
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Player not found!");
			}
		}
		return false;
	}
	
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	private int getInteger(String s) {
		int i = 0;
	    try { 
	        i =Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return 0; 
	    } catch(NullPointerException e) {
	        return 0;
	    }
	    return i;
	}
	
	private boolean isUnlockableSeries(String name){
		for(UnlockableSeries series : UnlockableSeries.values()){
			if(series.toString().equalsIgnoreCase(name) || ChatColor.stripColor(series.getDisplay().toString()).equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	private UnlockableSeries getUnlockableSeries(String name){
		for(UnlockableSeries series : UnlockableSeries.values()){
			if(series.toString().equalsIgnoreCase(name) || ChatColor.stripColor(series.getDisplay().toString()).equalsIgnoreCase(name)){
				return series;
			}
		}
		return null;
	}

}
