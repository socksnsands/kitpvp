package org.kitpvp.ability.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class ExplosiveGrenade extends Ability {

	private static String name = "Explosive Grenade";
	
	public ExplosiveGrenade() {
		super(name, "Throw an explosive grenade!", Material.SULPHUR, Scarcity.BLUE, 6);
		super.setClickedItem(Material.SULPHUR);
		super.setCooldown(20*14);
	}
	
	@Override
	public void onInteract(Player player, Action action){
		if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(!super.callEvent(player, this).isCancelled()){
				Item item = player.getWorld().dropItem(player.getLocation().clone().add(0,1,0), new ItemStack(Material.SULPHUR));
				item.setPickupDelay(Integer.MAX_VALUE);
				item.setVelocity(player.getLocation().getDirection().multiply(1.2));
				super.putOnCooldown(player);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

					@Override
					public void run() {
						ParticleEffect.EXPLOSION_HUGE.display(0, 0, 0, 0, 1, item.getLocation(), 200);
						item.getWorld().playSound(item.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						for(Entity entity : item.getNearbyEntities(4, 4, 4)){
							if(entity instanceof Player){
								Player le = (Player) entity;
								Core.getInstance().getDamageManager().damage(le, player, 10-(le.getLocation().distance(item.getLocation())));
								item.remove();
							}
						}
					}
					
				}, 20*2);
			}
		}
	}

}
