package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class EarthLaser extends Ability {

	private static String name = "Earth Laser";
	
	public EarthLaser() {
		super(name, "Shoot an earth beam!", Material.GRASS, Scarcity.BLUE, 10);
		super.setCooldown(20*12);
		super.setClickedItem(Material.STICK);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()){
			super.putOnCooldown(player);
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 1, 1);
			ArrayList<String> players = new ArrayList<String>();
			for(int i = 0; i < 50; i++){
				Location l = player.getLocation().getDirection().normalize().multiply(i/5).toLocation(player.getWorld());
				Location loc = player.getLocation().clone().add(l).add(0, 2, 0);
				ParticleEffect.SLIME.display(0, 0, 0, 0, 1, loc, 200);
				if(i %5 == 0){
					for(Player p : player.getWorld().getPlayers()){
						if(p != player && p.getLocation().clone().add(0,1,0).distance(loc) < 1){
							if(!players.contains(p.getName())){
							players.add(p.getName());
							p.setVelocity(new Vector(0, 1, 0));
							FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation().clone().add(0,-.3,0), p.getLocation().clone().add(0,-1,0).getBlock().getType(), (byte)0);
							fb.setVelocity(new Vector(0, .4, 0));
							fb.setCustomName("no_land");
							fb.setDropItem(false);
							}
						}
					}
				}
			}
		}
	}

}
