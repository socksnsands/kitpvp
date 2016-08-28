package org.kitpvp.ability.abilities.objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.kitpvp.core.Core;

public class Blockade {

	private Player owner;
	private Material wallBlock;
	private int distance;
	private int size;
	private ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
	private ArrayList<Location> locations = new ArrayList<Location>();
	
	public Blockade(Player player, Material wallBlock, int distance, int size){
		this.owner = player;
		this.wallBlock = wallBlock;
		this.distance = distance;
		this.size = size;
	}
	
	public void spawn(){
		ArrayList<Location> locs = getSpawnPoints();
		for(Location loc : locs){
			if(loc.getBlock().getType().equals(Material.AIR)){
				locations.add(loc);
				loc.getBlock().setType(wallBlock);
				loc.getWorld().playEffect(loc, Effect.STEP_SOUND, wallBlock);
				this.startDespawn();
			}
		}
	}
	
	private void startDespawn(){
		final int r = 10;
		for(int i = 0; i < r; i++){
			final int q = i;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

				@Override
				public void run() {
					if(locations.size() > 0){
						for(Player player : locations.get(0).getWorld().getPlayers()){
							if(player.getLocation().distance(locations.get(0)) < size*2.2){
								if(!player.equals(owner)){
									for(PotionEffect effect : potionEffects){
										player.addPotionEffect(effect);
									}
								}
							}
						}
					}
					
					if(q == (r-1)){
						destroy();
					}
				}
				
			
			}, i*20);
		}
	}
	
	public void destroy(){
		for(Location location : locations){
			if(location.getBlock().getType().equals(wallBlock)){
				location.getWorld().playEffect(location, Effect.STEP_SOUND, wallBlock);
				location.getBlock().setType(Material.AIR);
			}
		}
	}
	
	private ArrayList<Location> getSpawnPoints(){
		ArrayList<Location> spawnPoints = new ArrayList<Location>();
			Location l = owner.getLocation().clone();
			for(int pitch = -(6*size); pitch < 6*size; pitch+=5){
				for(int yaw = -(6*size); yaw < 6*size; yaw+=5){
					l.setYaw((float) (owner.getLocation().getYaw() + yaw));
					l.setPitch((float) (owner.getLocation().getPitch() + pitch));
					Location f = owner.getLocation().clone().add(l.getDirection().normalize().multiply(distance).toLocation(owner.getWorld()));
					if(!spawnPoints.contains(f)){
						spawnPoints.add(f);
					}
				}
			}
//			for(int y = 0; y < 2; y++){
//				l.setYaw((float) (owner.getLocation().getYaw() + (Math.pow(-1, y)*5*i)));
//				Location f = owner.getLocation().clone().add(l.getDirection().normalize().multiply(distance).toLocation(owner.getWorld()));
//				if(!spawnPoints.contains(f)){
//					spawnPoints.add(f);
//				}
//			}
//			for(int p = 0; p < 2; p++){
//				l.setYaw((float) (owner.getLocation().getYaw() + (Math.pow(-1, p)*5*i)));
//				Location f = owner.getLocation().clone().add(l.getDirection().normalize().multiply(distance).toLocation(owner.getWorld()));
//				if(!spawnPoints.contains(f)){
//					spawnPoints.add(f);
//				}
//			}
//			for(int p = 0; p < 2; p++){
//				l.setPitch((float) (owner.getLocation().getPitch() + (Math.pow(-1, p)*5*i)));
//				for(int y = 0; y < 2; y++){
//					l.setYaw((float) (owner.getLocation().getYaw() + (Math.pow(-1, y)*5*i)));
//					Location f = owner.getLocation().clone().add(l.getDirection().normalize().multiply(distance).toLocation(owner.getWorld()));
//					if(!spawnPoints.contains(f)){
//						spawnPoints.add(f);
//					}
//				}
//			}
		return spawnPoints;
	}
	
	public void addPotionEffect(PotionEffect effect){
		if(!this.potionEffects.contains(effect))
			this.potionEffects.add(effect);
	}
	
	public Player getPlayer(){
		return this.owner;
	}
	
	public Material getWallBlock(){
		return this.wallBlock;
	}
	
	public int getDistance(){
		return this.distance;
	}
	
	public int getSize(){
		return this.size;
	}
	
}
