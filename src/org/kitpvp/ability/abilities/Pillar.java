package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Pillar extends Ability {

	public Pillar(int level) {
		super("Pillar", "Stack up _H" + (level*10) + "H_ blocks!", Material.QUARTZ_BLOCK, Scarcity.BLUE, 5 + (level*2), level);
		super.setClickedItem(Material.QUARTZ_BLOCK);
		super.setCooldown(30 * 12);
	}
	
	@Override
	public void onInteract(Player player, Action action) {
		ArrayList<Location> loc = new ArrayList<Location>();
		for(int i = 0; i < super.getLevel()*10; i++){
			if(!player.getLocation().clone().add(0, i, 0).getBlock().getType().equals(Material.AIR)){
				player.sendMessage(ChatColor.RED + "This location is not open enough to pillar!");
				return;
			}
			loc.add(player.getLocation().clone().add(0, i, 0).getBlock().getLocation());
		}
		if (!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility("Pillar")).isCancelled()) {
			super.putOnCooldown(player);
			loc.forEach(l -> {l.getBlock().setType(Material.QUARTZ_BLOCK);});
			player.setFallDistance(0);
			player.teleport(loc.get(loc.size() - 1).clone().add(0,1.1,0));
			player.setVelocity(new Vector(0, .2, 0));
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

				@Override
				public void run() {
					loc.forEach(l -> {l.getBlock().setType(Material.AIR);});
				}
				
			}, 20*8);
		}
	}

}
