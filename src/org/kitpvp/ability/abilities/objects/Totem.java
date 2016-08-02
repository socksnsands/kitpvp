package org.kitpvp.ability.abilities.objects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.core.Core;

public class Totem {

	private Player player;
	private Location location;
	private PotionEffectType type;
	private int amplifier;
	private int lifeSpan;
	private double range;
	private Material topMaterial;
	
	private byte topByte = 1;
	
	final private int hitDamage = 25;
	private int health = 100;
	
	private int destroyRunnable;
	
	public Totem(Player player, Location location, PotionEffectType type, int amplifier, int lifeSpan, double range, Material topMaterial, byte topByte){
		this.player = player;
		this.location = location;
		this.type = type;
		this.amplifier = amplifier;
		this.lifeSpan = lifeSpan;
		this.range = range;
		this.topMaterial = topMaterial;
		this.topByte = topByte;
		
		this.spawn();
	}
	
	private void spawn(){
		location.getBlock().setType(Material.COBBLE_WALL);
		location.clone().add(0,1,0).getBlock().setType(this.topMaterial);
		location.clone().add(0,1,0).getBlock().setData(this.topByte);
		startLife();
		Core.getInstance().getTotemManager().addTotem(this);
	}
	
	public void damage(Player damager){
		if(!damager.equals(player)){
			location.getWorld().playEffect(location.clone().add(0,1,0), Effect.STEP_SOUND, this.topMaterial);
			this.health -= hitDamage;
			if(this.health <= 0){
				this.destroy();
			}
		}
	}
	
	public void giveEffect(){
		if(this.player.getLocation().distance(this.location) <= this.range){
			this.player.addPotionEffect(new PotionEffect(this.type, 20, this.amplifier));
			Location head = this.location.getBlock().getLocation().clone().add(.5,1.5,.5);
			double dX = player.getEyeLocation().getX() - head.getX();
			double dY = player.getEyeLocation().getY() - head.getY();
			double dZ = player.getEyeLocation().getZ() - head.getZ();
			for(int i = 1; i <= 10; i++){
				double x = (dX/10)*i;
				double y = (dY/10)*i;
				double z = (dZ/10)*i;
				int q = i;
				if(q > 5)
					q = Math.abs(q - 10);
				Location loc = head.clone().add(x, y, z);
				loc.getWorld().playEffect(loc, Effect.COLOURED_DUST, 0);
			}
		}
	}
	
	private void startLife(){
		this.destroyRunnable = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

			@Override
			public void run() {
				destroy();
			}
			
		}, lifeSpan);
	}
	
	public void destroy(){
		location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.COBBLESTONE);
		location.getBlock().setType(Material.AIR);
		location.clone().add(0,1,0).getBlock().setType(Material.AIR);
		if(Bukkit.getServer().getScheduler().isCurrentlyRunning(this.destroyRunnable))
			Bukkit.getServer().getScheduler().cancelTask(this.destroyRunnable);
		Core.getInstance().getTotemManager().removeTotem(this);
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Location getLocation(){
		return this.location;
	}
	
	public PotionEffectType getType(){
		return this.type;
	}
	
	public int getAmplifier(){
		return this.amplifier;
	}
	
	public int getLifeSpan(){
		return this.lifeSpan;
	}
	
	public double getRange(){
		return this.range;
	}
	
	public Material getTopMaterial(){
		return this.topMaterial;
	}
	
	public byte getTopByte(){
		return this.topByte;
	}
	
}
