package org.kitpvp.ability.abilities;


import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Grappler extends Ability {

	private static String name = "Grappler";
	
	public Grappler() {
		super(name, "Grapple around the map!", Material.LEASH, Scarcity.DARK_RED, 14);
		super.setClickedItem(Core.getInstance().getItemManager().createItem(name, Material.FISHING_ROD, (byte)0, 1, null));
	}
	
//	@EventHandler
//	public void onLaunch(ProjectileLaunchEvent event){
//		if(event.getEntity().equals(EntityType.FISHING_HOOK)){
//			if(event.getEntity().getShooter() instanceof Player){
//				Player player = (Player) event.getEntity().getShooter();
//				
//				Location 
//			}
//		}
//	}

	
	//TODO will do later
}
