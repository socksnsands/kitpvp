package org.kitpvp.ability.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Spiderman extends Ability implements Listener {

	public Spiderman() {
		super("Spiderman", "Shoot webs!", Material.WEB, Scarcity.BLUE, 4, 1);
		super.setClickedItem(Material.STRING);
		super.setCooldown(20*14);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			FallingBlock fb = player.getWorld().spawnFallingBlock(player.getLocation().clone().add(0,1,0), Material.WEB, (byte) 0);
			fb.setVelocity(player.getLocation().getDirection().multiply(1.2));
			fb.setDropItem(false);
			super.putOnCooldown(player);
		}
	}
	
	@EventHandler
	public void onBlockFall(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof FallingBlock) {
			FallingBlock fb = (FallingBlock)event.getEntity();
			if (fb.getBlockId() == Material.WEB.getId()) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

					@Override
					public void run() {
						event.getBlock().getLocation().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, Material.WEB);
						event.getBlock().setType(Material.AIR);
					}
					
				}, 20*6);
				
			}
		}

	}
	
	

}
