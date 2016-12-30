package org.kitpvp.ability.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.util.ParticleEffect;

public class Jesus extends Ability implements Listener {

	private ArrayList<String> haveBeenSaved = new ArrayList<String>();

	public Jesus() {
		super("Jesus", "Come back to life!", Material.GOLDEN_APPLE, Scarcity.GOLD, 18, 1);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (haveBeenSaved.contains(event.getPlayer().getName()))
			haveBeenSaved.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onKicked(PlayerKickEvent event) {
		if (haveBeenSaved.contains(event.getPlayer().getName()))
			haveBeenSaved.remove(event.getPlayer().getName());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(event.isCancelled())
				return;
			Damageable dm = player;
			if(dm.getHealth() - event.getDamage() <= 0){
				if (Core.getInstance().getUserManager().getUser(player).getActiveAbilities()
						.contains(Core.getInstance().getAbilityManager().getAbility("Jesus"))) {
					if (!haveBeenSaved.contains(player.getName())) {
						event.setDamage(0);
						player.setHealth(20.0);
						((Player) event.getEntity()).sendMessage(ChatColor.GOLD + "You were revived!");
						this.playJesusEffect(player);
						haveBeenSaved.add(player.getName());
					} else {
						haveBeenSaved.remove(player.getName());
					}
				}
			}
		}
	}

	private void playJesusEffect(final Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 40));
		player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 1, 1);
		for (int i = 0; i < 10; i++) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

				@Override
				public void run() {
					ParticleEffect.CLOUD.display(0, 0, 0, 0, 3, player.getLocation().clone().add(0, -.5, 0), 100);
				}

			}, i * 8);
		}
	}

}
