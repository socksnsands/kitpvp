package org.kitpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
import org.kitpvp.game.Game;
import org.kitpvp.game.games.SurvivalGames;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

public class JoinCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg1.getName().equalsIgnoreCase("join")) {
			if (arg0 instanceof Player) {
				Player player = (Player) arg0;
				if(Core.getInstance().getGameManager().isGameActive()){
					Game game = Core.getInstance().getGameManager().getActiveGame();
					game.join(player);
				}else{
					player.sendMessage(ChatColor.RED + "No game is currently joinable.");
				}
			} else {
				arg0.sendMessage("Command for players only!");
			}
		}
		if (arg1.getName().equalsIgnoreCase("host")) {
			if (arg0 instanceof Player) {
				Player player = (Player) arg0;
				User user = Core.getInstance().getUserManager().getUser(player);
				if(!(user.getRank().getValue() >= Rank.SRMOD.getValue())){
					player.sendMessage(ChatColor.GRAY + "This command requires rank " + Rank.SRMOD.getColor() + Rank.SRMOD.toString() + ChatColor.GRAY + "!");
					return false;
				}
				if(!Core.getInstance().getGameManager().isGameActive()){
					player.sendMessage(ChatColor.GRAY + "Attempting to host an SG...");
					SurvivalGames sg = new SurvivalGames();
					sg.start();
				}else{
					player.sendMessage(ChatColor.RED + "A game is already in progress!");
				}
			} else {
				arg0.sendMessage("Command for players only!");
			}
		}
		return false;
	}

}
