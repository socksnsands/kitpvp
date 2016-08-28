package org.kitpvp.ability.abilities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Pirate extends Ability implements Listener {

	private HashMap<Player, ArmorStand> birdies = new HashMap<Player, ArmorStand>();
	
	public Pirate() {
		super("Pirate", "Get a bird that blocks 10% of damage!", Material.EGG, Scarcity.RED, 8);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Pirate"))){
				event.setDamage(event.getDamage()*.9);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(Core.getInstance().getUserManager().getUser(player).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Pirate"))){
			if(!birdies.containsKey(player)){
				ArmorStand stand = player.getWorld().spawn(getLocation(player.getLocation(), -90), ArmorStand.class);
				stand.setVisible(false);
				stand.setGravity(false);
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				sm.setOwner("MHF_Chicken");
				skull.setItemMeta(sm);
				stand.setHelmet(skull);
				stand.setSmall(true);
				birdies.put(player, stand);
			}else{
				if(!player.isSneaking())
					birdies.get(player).teleport(getLocation(player.getLocation(), -90));
				else
					birdies.get(player).teleport(getLocation(player.getLocation().clone().add(0,-.2,0), -90));

			}
		}else{
			this.checkAndRemove(player);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		this.checkAndRemove(event.getEntity());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		this.checkAndRemove(event.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		this.checkAndRemove(event.getPlayer());
	}
	
	public void removeAllBirdies(){
		for(Player player : this.birdies.keySet()){
			birdies.get(player).remove();
		}
		this.birdies.clear();
	}
	
	private void checkAndRemove(Player player){
		if(this.birdies.containsKey(player)){
			this.birdies.get(player).remove();
			this.birdies.remove(player);
		}
	}
	
	private Location getLocation(Location location, int relativeLoc){
		Location faked = location.clone();
		faked.setPitch(0);
		faked.setYaw(location.getYaw()+relativeLoc);
		Location f = faked.getDirection().normalize().multiply(.5).toLocation(location.getWorld());
		f.setY(f.getY()+.7);
		f.add(location.getX(), location.getY(), location.getZ());
		f.setYaw(location.getYaw());
		f.setPitch(location.getPitch());
		return f;
	}
	
}
