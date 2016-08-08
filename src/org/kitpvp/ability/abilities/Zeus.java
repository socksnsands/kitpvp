package org.kitpvp.ability.abilities;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Zeus extends Ability {

	private static String name = "Zeus";
	
	public Zeus() {
		super(name, "Strike down lightning!", Material.WOOD_AXE, Scarcity.PURPLE, 9);
		super.setClickedItem(Material.WOOD_AXE);
		super.setCooldown(20*28);
	}
	
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			Location location = player.getLineOfSight((HashSet<Byte>) null, 15).get(player.getLineOfSight((HashSet<Byte>) null, 15).size()-1).getLocation();
			if(!location.getBlock().getType().equals(Material.AIR)){
				if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()){
					location.getWorld().strikeLightningEffect(location);
					super.putOnCooldown(player);
					location.getWorld().playSound(location, Sound.BLOCK_ANVIL_LAND, 1, 1);
					for(Player p : player.getWorld().getPlayers()){
						if(p.getLocation().distance(location) < 4){
							if(p != player){
								Core.getInstance().getDamageManager().damage(p, player, 15 - (p.getLocation().distance(location)*2.5));
							}
						}
					}
				}
			}else{
				player.sendMessage(ChatColor.RED + "That block is too far away!");
			}
		}
	}

}
