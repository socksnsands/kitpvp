package org.kitpvp.cheat.cheats;

import org.bukkit.entity.Player;
import org.kitpvp.cheat.Cheat;
import org.kitpvp.core.Core;

import static org.kitpvp.cheat.Cheat.Response.*;

public class ReachCheatModule extends Cheat {

	double max_reach = 0D;
	int hits_over = 0;
	
	public ReachCheatModule() {
		super("Reach");
		super.setBannable(20);
	}
	
	/**
	 * Uses a player and another player (hit), and a third argument if safe (boolean).
	 */
	@Override
	public Response eval(Object...objects){
		if(objects.length < 2){
			return AMBIGUOUS;
		}
		Object a = objects[0];
		Object b = objects[1];
		if(a instanceof Player && b instanceof Player){
			Player player = (Player) a;
			Player hit = (Player) b;
			double reach = player.getEyeLocation().distance(hit.getEyeLocation());
			if((hits_over != 0 && reach > this.max_reach/hits_over) && reach > 2.7){
				if(objects.length < 3 || (objects[2] instanceof Boolean && (Boolean)objects[2] == false)){
					if(Core.getInstance().getDebug()){
						System.out.println("Found " + hit.getName() + " for reach !");
					}
					return YES;
				}else if(objects[2] instanceof Boolean && (Boolean)objects[2] == true){
					this.max_reach += reach;
					this.hits_over++;
					return NO;
				}
			}else{
				return NO;
			}
		}
		return AMBIGUOUS;
	}

}
