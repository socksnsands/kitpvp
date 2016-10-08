package org.kitpvp.ability.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class FrostBite extends Ability implements Listener{

	public FrostBite() {
		super("Frostbite", "Explodes a chilling bite at nearby players.", Material.SNOW_BLOCK, Scarcity.DARK_RED, 11, 1);
		super.setClickedItem(Material.WOOL);
		super.setCooldown(20 * 20);
	}
	
	
	@Override
	public void onInteract(Player player, Action action) {
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)
				|| action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility("Frostbite")).isCancelled()){
			new BukkitRunnable(){
                double t = 0;
                double pi = Math.PI;
                public void run(){
                        t += pi / 16;
                        Location loc = player.getLocation();
                        for(double phi = 0; phi <= 2 * pi; phi += pi / 2){
                                double x = 0.3 * (4 * pi - t) * Math.cos(t + phi);
                                double y = 0.2 * t;
                                double z = 0.3 * (4 * pi - t) * Math.sin(t + phi);
                                loc.add(x,y,z);
                                ParticleEffect.SNOW_SHOVEL.display(0,0,0,0,5,loc,1); //customize this for your particleLib
                                loc.subtract(x,y,z);
                       
                               
                                if(t >= 4 * pi){
                                        this.cancel();
                                loc.add(x,y,z);
                                ParticleEffect.SNOW_SHOVEL.display(0,0,0,1,5, loc,50.0); //customize this for your particleLib
                                loc.subtract(x,y,z);
                                }
                        }
                }
        }.runTaskTimer(Core.getInstance(), 0, 1);
			}
			super.putOnCooldown(player);
		}
				for(Entity nearby : player.getNearbyEntities(6, 6, 6)){
					if(nearby instanceof Player){
						Player near = (Player) nearby;
						Core.getInstance().getDamageManager().damage(near, player, 10);
					}
				}
		
	}
}
