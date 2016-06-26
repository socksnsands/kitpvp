package org.kitpvp.ability.abilities;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.AbilityUseEvent;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class Shocker extends Ability implements Listener {

	private ArrayList<Player> shockedPlayers = new ArrayList<Player>();
	
	public Shocker() {
		super("Shocker", "Small chance to shock players (disabling abilities and shaking screen)", Material.DEAD_BUSH, Scarcity.RED);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Player damager = (Player) event.getDamager();
			final Player damaged = (Player) event.getEntity();
			User user = Core.getInstance().getUserManager().getUser(damager);
			if(user.getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Shocker"))){
				Random random = new Random();
				int i = random.nextInt(20);
				if(i < 3){
					if(!super.callEvent(damager, this).isCancelled()){
					if(!shockedPlayers.contains(damaged)){
						shockedPlayers.add(damaged);
						for(int q = 0; q < 20; q++){
							final int f = q;
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

							@Override
							public void run() {
								damaged.playEffect(EntityEffect.HURT);
								if(f==19)
									shockedPlayers.remove(damaged);
							}
							
						}, q*2);
					}
					}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onUse(AbilityUseEvent event){
		Player player = event.getPlayer();
		if(shockedPlayers.contains(player)){
			event.setCancelled(true);
		}
	}

}
