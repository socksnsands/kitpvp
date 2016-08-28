package org.kitpvp.ability.abilities.objects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class BlackHoleObject {
	private Player owner;
	private Location loc;
	private Item i;
	private int clock = 2 * 20 * 5;
	public BlackHoleObject(Player owner, Location l,Item i)
	{
		this.owner = owner;
		this.loc = l;
		this.i = i;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				tick();
				clock--;
				if(clock <= 0)
					this.cancel();
			}
		}.runTaskTimer(Core.getInstance(), 2, 2);
		
	}

	private void tick()
	{
		loc.getWorld().spigot().playEffect(loc, Effect.LARGE_SMOKE, 0, 0, 0.35f, 0.35f, 0.35f, 0f, 70, 40);
		for(Entity s : i.getNearbyEntities(7,7,7))
		{
			if(!(s instanceof Player)) continue;
			s.setVelocity(i.getLocation().toVector().subtract(s.getLocation().toVector()).multiply(0.5));
			((Player) s).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 10));
		}
		for(Entity s : i.getNearbyEntities(1, 1, 1))
		{
			if(!(s instanceof Damageable)) continue;
			((Damageable) s).damage(1);
		}
	}

}
