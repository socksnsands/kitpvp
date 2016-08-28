package org.kitpvp.ability.abilities;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class ArmorLock extends Ability {

	public ArmorLock() {
		super("Body Shield", "Shield your body in a special material to stop all damage!", Material.DIAMOND_CHESTPLATE, Scarcity.PURPLE, 6);
		// TODO Auto-generated constructor stub
		super.setClickedItem(Material.DIAMOND_CHESTPLATE);
		super.setCooldown(20*30);
	}
	
	@Override
	public void onInteract(Player player, Action action) {
		if(super.callEvent(player, this).isCancelled())
			return;
		super.putOnCooldown(player);
		
		Creeper c =  (Creeper) player.getWorld().spawnEntity(player.getLocation(),EntityType.CREEPER);
		c.setNoDamageTicks(Integer.MAX_VALUE);
		c.setPowered(true);
		c.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE));
		noAI(c);
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 100));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 5, 100));
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

			@Override
			public void run() {
				c.remove();
			}

		}, 20 * 5);
	}
	
	void noAI(Entity bukkitEntity) {
	    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
	    NBTTagCompound tag = nmsEntity.getNBTTag();
	    if (tag == null) {
	        tag = new NBTTagCompound();
	    }
	    nmsEntity.c(tag);
	    tag.setInt("NoAI", 1);
	    nmsEntity.f(tag);
	}
}
