package org.kitpvp.unlockable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.ability.Ability;
import org.kitpvp.ability.abilities.AntiStomper;
import org.kitpvp.ability.abilities.Aphrodite;
import org.kitpvp.ability.abilities.BulkUp;
import org.kitpvp.ability.abilities.DamageLaser;
import org.kitpvp.ability.abilities.DarkLaser;
import org.kitpvp.ability.abilities.EarthLaser;
import org.kitpvp.ability.abilities.ElectricLaser;
import org.kitpvp.ability.abilities.FeatherBoots;
import org.kitpvp.ability.abilities.FireLaser;
import org.kitpvp.ability.abilities.FlameAura;
import org.kitpvp.ability.abilities.Flash;
import org.kitpvp.ability.abilities.FrostLaser;
import org.kitpvp.ability.abilities.Frosty;
import org.kitpvp.ability.abilities.Hades;
import org.kitpvp.ability.abilities.Ignite;
import org.kitpvp.ability.abilities.Jesus;
import org.kitpvp.ability.abilities.MythicalLaser;
import org.kitpvp.ability.abilities.Noob;
import org.kitpvp.ability.abilities.ParalysisLaser;
import org.kitpvp.ability.abilities.Poseidon;
import org.kitpvp.ability.abilities.QuickShot;
import org.kitpvp.ability.abilities.Shocker;
import org.kitpvp.ability.abilities.SnowballShotgun;
import org.kitpvp.ability.abilities.Stomper;
import org.kitpvp.ability.abilities.SwapLaser;
import org.kitpvp.ability.abilities.Switcher;
import org.kitpvp.ability.abilities.Totem;
import org.kitpvp.ability.abilities.Trampoline;
import org.kitpvp.ability.abilities.Vovl;
import org.kitpvp.ability.abilities.WindStorm;
import org.kitpvp.ability.abilities.Zeus;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.Unlockable.Scarcity;
import org.kitpvp.user.User;

public class UnlockableManager implements Listener {

	private ArrayList<Unlockable> unlockables = new ArrayList<Unlockable>();

	public UnlockableManager() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
		registerUnlockables();
		registerAllListeners();
	}

	public void addUnlockable(Unlockable unlockable) {
		if (!unlockable.isRegistered())
			unlockables.add(unlockable);
	}

	public boolean isSeries(String name) {
		for (UnlockableSeries series : UnlockableSeries.values()) {
			if (ChatColor.stripColor(series.getDisplay()).equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public UnlockableSeries getSeries(String name) {
		for (UnlockableSeries series : UnlockableSeries.values()) {
			if (ChatColor.stripColor(series.getDisplay()).equalsIgnoreCase(name)) {
				return series;
			}
		}
		return null;
	}

	public void removeUnlockable(Unlockable unlockable) {
		if (unlockable.isRegistered())
			unlockables.remove(unlockable);
	}

	public ArrayList<Unlockable> getRegisteredUnlockables() {
		return unlockables;
	}

	private void registerUnlockables() {
		// Abilities
		unlockables.add(new FeatherBoots());
		unlockables.add(new FlameAura());
		unlockables.add(new QuickShot());
		unlockables.add(new Flash());
		unlockables.add(new Shocker());
		unlockables.add(new Jesus());
		unlockables.add(new WindStorm());
		unlockables.add(new Ignite());
		unlockables.add(new BulkUp());
		unlockables.add(new Totem());
		unlockables.add(new Frosty());
		unlockables.add(new SnowballShotgun());
		unlockables.add(new AntiStomper());
		unlockables.add(new Stomper());
//		unlockables.add(new Trampoline());
		unlockables.add(new Aphrodite());
		unlockables.add(new Noob());
		unlockables.add(new DamageLaser());
		unlockables.add(new DarkLaser());
		unlockables.add(new FireLaser());
		unlockables.add(new FrostLaser());
		unlockables.add(new MythicalLaser());
		unlockables.add(new ParalysisLaser());
		unlockables.add(new SwapLaser());
		unlockables.add(new Switcher());
		unlockables.add(new Zeus());
		unlockables.add(new Poseidon());
		unlockables.add(new Hades());
		unlockables.add(new Vovl());
		unlockables.add(new ElectricLaser());
		unlockables.add(new EarthLaser());
	}

	public Unlockable getUnlockable(String name) {
		for (Unlockable unlockable : this.unlockables) {
			if (unlockable.getName().equalsIgnoreCase(name)) {
				return unlockable;
			}
		}
		return null;
	}

	public boolean isUnlockable(String name) {
		for (Unlockable unlockable : this.unlockables) {
			if (unlockable.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isMoreRare(Unlockable toCheck, Unlockable toCheckAgainst) {
		if (toCheck.getScarcity().getChance() < toCheckAgainst.getScarcity().getChance()) {
			return true;
		}
		return false;
	}

	private void registerAllListeners() {
		// If an ability implements Listener..
		Bukkit.getServer().getPluginManager().registerEvents(new FeatherBoots(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Shocker(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Jesus(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new WindStorm(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Frosty(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new SnowballShotgun(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Noob(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Stomper(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Trampoline(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Aphrodite(), Core.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new Switcher(), Core.getInstance());
	}

	public Scarcity getRandom() {
		Random random = new Random();
		double d = random.nextDouble();
		Scarcity result = Scarcity.WHITE;
		for (Scarcity scarcity : Scarcity.values()) {
			if (scarcity.getChance() > d) {
				if (!(result.getChance() < scarcity.getChance())) {
					result = scarcity;
				}
			}
		}
		return result;
	}

	private void openUnlockableChestInventory(Player player) {
		Inventory inv = Bukkit.getServer().createInventory(player, 27, ChatColor.UNDERLINE + "Unlockable Chests");
		User user = Core.getInstance().getUserManager().getUser(player);
		for (UnlockableSeries series : UnlockableSeries.values()) {
			if (user.getQuantityOfSeries(series) > 0) {
				ItemStack item = series.getIcon();
				item.setAmount(user.getQuantityOfSeries(series));
				inv.addItem(item);
			}
		}
		player.openInventory(inv);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getPlayer().getInventory().getItemInMainHand()
					.equals(Core.getInstance().getItemManager().getUnlockableOpener())) {
				this.openUnlockableChestInventory(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}

	public Unlockable getRandomUnlockable(UnlockableSeries series) {
		double total = 0, current = 0;
		for (Unlockable unlockable : series.getUnlockables()) {
			total += unlockable.getScarcity().getChance();
		}
		double random = ThreadLocalRandom.current().nextDouble(0, total);
		for (Unlockable unlockable : series.getUnlockables()) {
			current += unlockable.getScarcity().getChance();
			if (current > random) {
				return unlockable;
			}
		}
		return null;
	}

	public void openSeries(Player player, UnlockableSeries series) {
		Inventory inv = Bukkit.getServer().createInventory(player, 27, ChatColor.UNDERLINE + "Rolling...");

		ItemStack f = new ItemStack(Material.STAINED_GLASS, 1, (byte) 3);
		ItemMeta finalim = f.getItemMeta();
		finalim.setDisplayName(ChatColor.AQUA + "Selector");
		f.setItemMeta(finalim);

		ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta blankim = blank.getItemMeta();
		blankim.setDisplayName(" ");
		blank.setItemMeta(blankim);

		for (int i = 0; i < 9; i++) {
			if (i != 4)
				inv.setItem(i, blank);
			else
				inv.setItem(i, f);
			inv.setItem(i + 18, blank);

			inv.setItem(i + 9, this.getRandomUnlockable(series).getIcon());
		}

		Random random = new Random();
		int t = random.nextInt(30)+30;
		for (int i = 0; i < t; i++) {
			final int q = i;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {

				@Override
				public void run() {
					tickSeriesInventory(player, inv, series);
					player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1, 1);
					if (q == t - 1) {
						String name = inv.getItem(13).getItemMeta().getDisplayName();
						player.sendMessage(ChatColor.GRAY + "You unlocked "
								+ name + ChatColor.GRAY + "!");
						
						Ability ability = Core.getInstance().getAbilityManager().getAbility(ChatColor.stripColor(name));
						Color start = Color.WHITE;
						Color finish = Color.GRAY;
						if(ability.getScarcity().equals(Scarcity.BLUE)){
							start = Color.BLUE;
							finish = Color.GREEN;
						}else if(ability.getScarcity().equals(Scarcity.PURPLE)){
							start = Color.PURPLE;
							finish = Color.MAROON;
						}else if(ability.getScarcity().equals(Scarcity.RED)){
							start = Color.RED;
							finish = Color.ORANGE;
						}else if(ability.getScarcity().equals(Scarcity.DARK_RED)){
							start = Color.fromBGR(0, 0, 255);
							finish = Color.FUCHSIA;
						}else if(ability.getScarcity().equals(Scarcity.GOLD)){
							start = Color.ORANGE;
							finish = Color.YELLOW;
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " found a " + ChatColor.GOLD + "gold " + ChatColor.GRAY + "ability!");
							Bukkit.broadcastMessage("");
						}else if(ability.getScarcity().equals(Scarcity.BLACK)){
							start = Color.BLACK;
							finish = Color.SILVER;
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " found a " + ChatColor.DARK_GRAY + "black " + ChatColor.GRAY + "ability!");
							Bukkit.broadcastMessage("");
						}
						FireworkEffect effect = FireworkEffect.builder().trail(false).flicker(true).withColor(start).withFade(finish).with(FireworkEffect.Type.BURST).build();
						Firework fw = player.getWorld().spawn(player.getLocation(), Firework.class);
						FireworkMeta meta = fw.getFireworkMeta();
						meta.clearEffects();
						meta.addEffect(effect);
						meta.setPower(0);
						fw.setFireworkMeta(meta);
						
						//TODO give the user the ability
						Core.getInstance().getUserManager().getUser(player).addUnlockable(ability);
						Core.getInstance().getUserManager().getUser(player).removeSeries(series);
					}
				}

			}, i * (2));
		}

	}

	private void tickSeriesInventory(Player player, Inventory inventory, UnlockableSeries series) {
		if (player.getOpenInventory() != inventory)
			player.openInventory(inventory);
		if (inventory.getSize() < 18)
			return;
		for (int i = 0; i >= -7; i--) 
			inventory.setItem(17 + i, inventory.getItem(16 + i));

		inventory.setItem(9, this.getRandomUnlockable(series).getIcon());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Rolling...")) {
			event.setCancelled(true);
		}
		if (event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Unlockable Chests")) {
			if (event.getCurrentItem() != null) {
				if (event.getCurrentItem().getItemMeta() != null) {
					if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
						String current = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
						if (this.isSeries(current)) {
							if (event.getWhoClicked() instanceof Player) {
								this.openSeries((Player) event.getWhoClicked(), this.getSeries(current));
							}
						}
					}
				}
			}
			event.setCancelled(true);
		}
	}

}
