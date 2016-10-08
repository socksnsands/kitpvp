package org.kitpvp.ability.abilities;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Groundslam extends Ability {

	private static String name = "Ground Slam";
	
	public Groundslam() {
		super(name, "Right click to throw up enemies within 3 blocks up!", Material.IRON_SPADE, Scarcity.BLUE, 8, 1);
		super.setClickedItem(Material.BRICK);
		super.setCooldown(20*18);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(!super.callEvent(player, Core.getInstance().getAbilityManager().getAbility(name)).isCancelled()){
				for(int x = -3; x < 3; x++){
					for(int z = -3; z < 3; z++){
						for(int y = 1; y <= 2; y++){
							Location loc = player.getLocation().clone().add(x, -y, z);
							if(!loc.getBlock().getType().equals(Material.AIR)){
								loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
								break;
							}
						}
					}
				}
				for(Player p : player.getWorld().getPlayers()){
					if(p.getLocation().distance(player.getLocation()) <=4 ){
						if(!p.equals(player)){
							p.setVelocity(new Vector(0, 1.6, 0));
						}
					}
				}
				super.putOnCooldown(player);
			}
		}
	}

}
