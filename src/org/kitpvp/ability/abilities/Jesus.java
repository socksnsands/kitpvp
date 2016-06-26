package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Jesus extends Ability implements Listener {

	private ArrayList<String> haveBeenSaved = new ArrayList<String>();
	
	public Jesus() {
		super("Jesus", "Come back to life!", Material.GOLDEN_APPLE, Scarcity.GOLD);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		if(haveBeenSaved.contains(event.getPlayer().getName()))
			haveBeenSaved.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onKicked(PlayerKickEvent event){
		if(haveBeenSaved.contains(event.getPlayer().getName()))
			haveBeenSaved.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		if(Core.getInstance().getUserManager().getUser(event.getEntity()).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Jesus"))){
			if(!haveBeenSaved.contains(event.getEntity().getName())){
				//TODO reset inventory (1hb soup)
				event.getEntity().setHealth(20.0);
				event.setDeathMessage("");
				event.getEntity().sendMessage(ChatColor.GOLD + "You were revived!");
				this.playJesusEffect(event.getEntity());	
				haveBeenSaved.add(event.getEntity().getName());
			}else{
				haveBeenSaved.remove(event.getEntity().getName());
			}
		}
	}
	
	private void playJesusEffect(final Player player){
		player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 4*20, 0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4*20, 40));
		for(int i = 0; i < 10; i++){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

				@Override
				public void run() {
					player.getWorld().playEffect(player.getLocation().clone().add(0,2,0), Effect.CLOUD, 1);
				}
				
			}, i*8);
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 4*20, 40));
	}

}
