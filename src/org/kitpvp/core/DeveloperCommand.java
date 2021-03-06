package org.kitpvp.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;
import org.kitpvp.loadout.Loadout;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.user.User;

public class DeveloperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		if (arg1.getName().equalsIgnoreCase("dev")) {
			if (!arg0.isOp()) {
				arg0.sendMessage("Command for admins only.");
				return false;
			}
			if (arg3.length == 0) {
				arg0.sendMessage("Please specify dev project");
				return false;
			}

			if (arg3[0].equalsIgnoreCase("opener")) {
				if (!(arg0 instanceof Player)) {
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				player.getInventory().addItem(Core.getInstance().getItemManager().getUnlockableOpener());
				return false;
			}
			
			if(arg3[0].equalsIgnoreCase("motd")){
		        String m = "";
		        for (int i = 1; i < arg3.length; i++) {
		        	m = m + (i > 1 ? " " : "") + arg3[i];
		        }
		        Core.getInstance().getConfig().set("motd", m);
		        Core.getInstance().saveConfig();
		        return false;
			}
			
			if(arg3[0].equalsIgnoreCase("debug")){
		        Core.getInstance().toggleDebug();
		        arg0.sendMessage("Toggled debug, current state: " + (Core.getInstance().getDebug() ? "on" : "off"));
		        return false;
			}
			
			if(arg3[0].equalsIgnoreCase("whitelistreason")){
		        String m = "";
		        for (int i = 1; i < arg3.length; i++) {
		        	m = m + (i > 1 ? " " : "") + arg3[i];
		        }
		        Core.getInstance().getConfig().set("whitelist.reason", m);
		        Core.getInstance().saveConfig();
		        return false;
			}

			if (arg3[0].equalsIgnoreCase("giveseries")) {
				if (arg3.length >= 3) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (player.getName().equalsIgnoreCase(arg3[1])) {
							User user = Core.getInstance().getUserManager().getUser(player);
							try {
								UnlockableSeries series = UnlockableSeries.valueOf(arg3[2]);
								int a = 1;
								if(arg3.length >= 4)
									try{
										a = Integer.valueOf(arg3[3]);
									}catch(Exception ex){}
								for(int i = 0; i < a; i++)
									user.addSeries(series);
							} catch (IllegalArgumentException ex) {
								arg0.sendMessage("Series \"" + arg3[2] + "\" not found!");
								arg0.sendMessage("Series list:");
								for (UnlockableSeries series : UnlockableSeries.values())
									arg0.sendMessage(series + " - " + series.getDescription());
							}
						}
					}
				}
				return false;
			}

			if (arg3[0].equalsIgnoreCase("safety")) {
				if (!(arg0 instanceof Player)) {
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				User user = Core.getInstance().getUserManager().getUser(player);
				user.setSafe(!user.isSafe());
				player.sendMessage("Safety: " + user.isSafe());
				return false;
			}

			if (arg3[0].equalsIgnoreCase("reset")) {
				if (!(arg0 instanceof Player)) {
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				User user = Core.getInstance().getUserManager().getUser(player);
				user.resetData();
				return false;
			}

			if (arg3[0].equalsIgnoreCase("load")) {
				if (!(arg0 instanceof Player)) {
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				if (arg3.length >= 2) {
					try {
						if (Core.getInstance().getUserManager().getUser(player).getLoadouts().size() < Core
								.getInstance().getUserManager().getUser(player).getRank().getMaxLoadouts()) {
							Loadout loadout = Core.getInstance().getUserManager().getUser(player)
									.readLoadoutString(arg3[1]);
							if (loadout.getPointValue() <= loadout.getMaxPoints()) {
								Core.getInstance().getUserManager().getUser(player).addLoadout(loadout);
								player.sendMessage(ChatColor.GREEN + "Loadout loaded!");
							} else {
								player.sendMessage(ChatColor.RED + "Loadout has too many points! " + ChatColor.GRAY
										+ "(" + loadout.getPointValue() + "/" + loadout.getMaxPoints() + ")");
							}
						} else
							player.sendMessage(ChatColor.RED + "You already have your maximum number of loadouts!");
					} catch (Exception ex) {
						player.sendMessage(ChatColor.RED + "Error loading loadout...");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Correct usage: /dev load (formatted loadout)");
				}
				return false;
			}

			if (arg3[0].equalsIgnoreCase("abilities")) {
				if (!(arg0 instanceof Player)) {
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				if (arg3.length >= 2) {
					for (Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()) {
						if (unlockable instanceof Ability) {
							if (((Ability) unlockable).getName().toLowerCase().replaceAll(" ", "_")
									.equalsIgnoreCase(arg3[1].replaceAll(" ", "_"))) {
								if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
										.contains(unlockable)) {
									Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
											.remove((Ability) unlockable);
									player.sendMessage("Removed " + ((Ability) unlockable).getName() + "!");
								} else {
									Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
											.add((Ability) unlockable);
									player.sendMessage("Added " + ((Ability) unlockable).getName() + "!");
									if (Core.getInstance().getAbilityManager().getAbility(arg3[1].replaceAll("_", " "))
											.getClickedItem() != null)
										player.getInventory().addItem(Core.getInstance().getAbilityManager()
												.getAbility(arg3[1].replaceAll("_", " ")).getClickedItem());
								}
								return false;
							}
						}
					}
					player.sendMessage("No abilities found called \"" + arg3[1] + "\"");
				} else {
					String list = "";
					for (Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables())
						if (unlockable instanceof Ability) {
							Ability ability = (Ability) unlockable;
							ChatColor color = ChatColor.RED;
							if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
									.contains(ability))
								color = ChatColor.GREEN;
							list += ability.getScarcity().getColor() + "[" + color
									+ ability.getName().replace(" ", "_").toLowerCase()
									+ ability.getScarcity().getColor() + "] ";
						}

					player.sendMessage("Select an ability. List: " + list);
				}
				return false;
			}
			arg0.sendMessage("Project not found: " + arg3[0]);
		}

		return false;
	}

}
