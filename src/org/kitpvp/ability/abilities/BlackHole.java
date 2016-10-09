package org.kitpvp.ability.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.abilities.objects.BlackHoleObject;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class BlackHole extends Ability {
	public BlackHole()
	{
		super("Black Hole", "Sucks nearby enemies in, not allowing them _L_to move and constantly damaging them._L_ Has a {H35H} second cooldown.", Material.OBSIDIAN, Scarcity.GOLD, 15, 1);
		super.setClickedItem(Material.OBSIDIAN);
		super.setCooldown(20*35);
	}
	
	@Override
	public void onInteract(Player player, Action action) {
		if(super.callEvent(player, this).isCancelled())
			return;
		Item item = player.getWorld().dropItem(player.getLocation().clone().add(0, 1, 0),
				new ItemStack(Material.OBSIDIAN));
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setVelocity(player.getEyeLocation().getDirection().multiply(1.2));
		super.putOnCooldown(player);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

			@Override
			public void run() {
				new BlackHoleObject(player, item.getLocation().add(0, 2, 0),item);
				item.remove();
			}

		}, 20 * 2);
	}
}
