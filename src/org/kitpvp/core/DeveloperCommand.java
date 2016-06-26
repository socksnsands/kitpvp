package org.kitpvp.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.user.User;

public class DeveloperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("dev")){
			if(!arg0.isOp()){
				arg0.sendMessage("Command for admins only.");
				return false;
			}
			if(arg3.length == 0){
				arg0.sendMessage("Please specify dev project");
				return false;
			}
			
			if(arg3[0].equalsIgnoreCase("safety")){
				if(!(arg0 instanceof Player)){
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				User user = Core.getInstance().getUserManager().getUser(player);
				user.setSafe(!user.isSafe());
				player.sendMessage("Safety: " + user.isSafe());
			}
			
			if(arg3[0].equalsIgnoreCase("abilities")){
				if(!(arg0 instanceof Player)){
					arg0.sendMessage("This command is for players only.");
					return false;
				}
				Player player = (Player) arg0;
				if(arg3.length >= 2){
					for(Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables()){
						if(unlockable instanceof Ability){
							if(((Ability)unlockable).getName().toLowerCase().replaceAll(" ", "_").equalsIgnoreCase(arg3[1].replaceAll(" ", "_"))){
								if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(unlockable)){
									Core.getInstance().getUserManager().getUser(player).getActiveAbilities().remove((Ability) unlockable);
									player.sendMessage("Removed " + ((Ability) unlockable).getName() + "!");
								}else{
									Core.getInstance().getUserManager().getUser(player).getActiveAbilities().add((Ability) unlockable);
									player.sendMessage("Added " + ((Ability) unlockable).getName() + "!");
									if(Core.getInstance().getAbilityManager().getAbility(arg3[1].replaceAll("_", " ")).getClickedItem() != null)
										player.getInventory().addItem(Core.getInstance().getAbilityManager().getAbility(arg3[1].replaceAll("_", " ")).getClickedItem());
								}
								return false;
							}
						}
					}
					player.sendMessage("No abilities found called \"" + arg3[1] + "\"");
				}else{
					String list = "";
					for(Unlockable unlockable : Core.getInstance().getUnlockableManager().getRegisteredUnlockables())
						if(unlockable instanceof Ability){
							Ability ability = (Ability)unlockable;
							ChatColor color = ChatColor.RED;
							if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(ability))
								color = ChatColor.GREEN;
							list += ability.getScarcity().getColor() + "[" + color + ability.getName().replace(" ", "_").toLowerCase() + ability.getScarcity().getColor() + "] ";
						}
							
					player.sendMessage("Select an ability. List: " + list);
				}
			}
		}
		
		return false;
	}

}
