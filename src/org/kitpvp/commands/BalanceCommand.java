package org.kitpvp.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.user.User;

public class BalanceCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg1.getName().equalsIgnoreCase("bal")) {
			if (arg0 instanceof Player) {
				Player player = (Player) arg0;
				User user = Core.getInstance().getUserManager().getUser(player);
				player.sendMessage(ChatColor.GRAY + "Money: " + ChatColor.GREEN + user.getBalance());
				int chests = 0;
				for (UnlockableSeries series : UnlockableSeries.values()) {
					chests += user.getQuantityOfSeries(series);
				}
				player.sendMessage(ChatColor.GRAY + "Series: " + ChatColor.LIGHT_PURPLE + chests);
				int abilities = 0;
				for (Ability ability : user.getOwnedAbilities()) {
					abilities += user.getOwnedUnlockablesAsHashmap().get(ability);
				}
				player.sendMessage(ChatColor.GRAY + "Abilities: " + ChatColor.AQUA + abilities);
			} else {
				arg0.sendMessage("Command for players only!");
			}
		}
		return false;
	}

}
