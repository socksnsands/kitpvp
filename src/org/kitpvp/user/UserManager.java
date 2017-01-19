package org.kitpvp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitpvp.core.Core;
import org.kitpvp.core.LoadDataTask;
import org.kitpvp.core.PushDataTask;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.user.rank.Rank;

public class UserManager implements Listener {

	private ArrayList<User> users = new ArrayList<User>();

	public UserManager() {
		initialLoad();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}

	public User getUser(Player player) {
		for (User user : users)
			if (user.getPlayer() != null)
				if (user.getPlayer().equals(player))
					return user;
		return null;
	}

	public User getUser(String uuid) {
		for (User user : users)
			if (user.getUUID().toString().equals(uuid))
				return user;
		System.out.println("UUID " + uuid + " not found for a user!");
		return null;
	}

	public boolean isLoadedUser(Player player) {
		for (User user : users)
			if(user.getPlayer() != null)
				if (user.getPlayer().equals(player))
					return true;
			return false;
	}

	public boolean isLoadedUser(String uuid) {
		for (User user : users)
			if (user.getUUID().equals(uuid))
				return true;
		return false;
	}

	public ArrayList<User> getUsers() {
		return this.users;
	}

	private void initialLoad() {
		if (!users.isEmpty())
			return;
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			User user = new User(player.getUniqueId().toString());
			users.add(user);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (this.getUser(player).isSafe()) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Core.getInstance().getDamageManager().setLastDamaged((Player) event.getEntity(),
					(Player) event.getDamager());
		}
		if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
			Projectile p = (Projectile) event.getDamager();
			if (p.getShooter() instanceof Player) {
				Core.getInstance().getDamageManager().setLastDamaged((Player) event.getEntity(),
						(Player) p.getShooter());
			}
		}
	}
	
//	@EventHandler(priority = EventPriority.MONITOR)
//	public void onKb(EntityDamageByEntityEvent event){
//		if(event.getDamager() instanceof Player){
//			Player p = (Player) event.getDamager();
//			if(this.isLoadedUser(p)){
//				if(this.getUser(p).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Anvil"))){
//					return;
//				}
//			}
//		}
//		if(event.getEntity() instanceof Player){
//			Player p = (Player) event.getEntity();
//			if(this.isLoadedUser(p)){
//				if(this.getUser(p).getActiveAbilities().contains(Core.getInstance().getAbilityManager().getAbility("Anvil"))){
//					return;
//				}
//			}
//		}
//		if(event.getEntity() instanceof Damageable){
//			Damageable dm = (Damageable) event.getEntity();
//			event.setCancelled(true);
//			dm.damage(event.getDamage());
//			dm.setVelocity(event.getDamager().getLocation().getDirection().multiply(.4).setY(.3));
//			if(event.getDamager() instanceof Player){
//				Player p =(Player) event.getDamager();
//				applyKnockBack(p, dm, dm.getLocation(), p.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK), p.isSprinting(), false, .5, .35);
//			}
//		}
//	}

	@EventHandler
	public void onLoseHunger(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
//	private void applyKnockBack(Player hitter, final Entity en, final Location loc, int knock, final boolean sprint, final boolean gapple, final double velocity, final double height)
//	  {
//	    if ((en instanceof Player))
//	    {
//	      final Player player = (Player)en;
//	        new BukkitRunnable()
//	        {
//	          public void run()
//	          {
//	            org.bukkit.util.Vector vector = player.getLocation().toVector().subtract(loc.toVector()).normalize();
//	            
//	            double d = velocity + (sprint ? 1 : 0) * 0.05D - 0.05D;
//	            double b = height;
//	            if ((!player.isOnGround()))
//	            {
//	              b -= 0.5D;
//	              if (b < 0.0D) {
//	                b = 0.0D;
//	              }
//	            }
//	            player.setVelocity(vector.multiply(d).setY(b));
//	          }
//	        }.runTask(Core.getInstance());
//	    }
//	  }

	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			if (event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Kit Selector")) {
				Player player = (Player) event.getWhoClicked();
				if (event.getCurrentItem().getItemMeta() != null) {
					String current = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
					User user = Core.getInstance().getUserManager().getUser(player);
					if (user.hasLoadout(current)) {
						boolean tp = true;
						if(Core.getInstance().getGameManager().isGameActive()){
							for(Player p : Core.getInstance().getGameManager().getActiveGame().getPlayers()){
								if(p.equals(player)){
									tp = false;
									break;
								}
							}
						}
						user.getLoadout(current).apply(player, tp);
					}
				}
				event.setCancelled(true);
			}
			if (event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Path Selector")) {
				Player player = (Player) event.getWhoClicked();
				if (event.getCurrentItem()!= null) {
					User user = Core.getInstance().getUserManager().getUser(player);
					if (Specialty.isSpecialty(event.getCurrentItem())) {
						user.setSpecialty(Specialty.getSpecialty(event.getCurrentItem()));
						user.getPlayer().closeInventory();
						user.getPlayer().sendMessage(ChatColor.GREEN + "Path selected: " +event.getCurrentItem().getItemMeta().getDisplayName());
					}
				}
				event.setCancelled(true);
			}
		}
	}

	public void addUser(User user) {
		if (this.users.contains(user))
			this.users.add(user);
	}

	@EventHandler
	public void onPreLogin(AsyncPlayerPreLoginEvent e) {
		boolean allowed = false;
		allowed = !Bukkit.hasWhitelist();
		if(!allowed)
		for(OfflinePlayer p : Core.getInstance().getServer().getWhitelistedPlayers()){
			if(p.getUniqueId().toString().equals(e.getUniqueId().toString())){
				allowed = true;
			}
		}
		if(!allowed)
			e.disallow(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("whitelist.reason")));
		else
			e.allow();
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getPlayer().getItemInHand().equals(Core.getInstance().getItemManager().getFFAItem())) {
				User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
				if (user.getLoadouts().size() > 0) {
					user.openKitSelector();
				} else {
					event.getPlayer()
							.sendMessage(ChatColor.RED + "No loadouts created yet. Open an anvil to create one!");
				}
				event.setCancelled(true);
			}
			if (event.getPlayer().getItemInHand().equals(Core.getInstance().getItemManager().getPathItem())) {
				User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
				user.openPathSelector();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		User user = Core.getInstance().getUserManager().getUser(player);
		user.removeActiveLoadout();
		user.getActiveAbilities().clear();
		user.clearCooldowns();
		user.setSafe(true);

		event.setDroppedExp(0);
		
		if(Core.getInstance().getGameManager().isGameActive()){
			for(Player p : Core.getInstance().getGameManager().getActiveGame().getPlayers()){
				if(p.equals(player)){
					Core.getInstance().getGameManager().getActiveGame().lose(p);
					break;
				}
			}
		}
		
		ArrayList<ItemStack> toRemoveDrops = new ArrayList<>();
		for(ItemStack drop : event.getDrops()){
			if(!drop.getType().equals(Material.MUSHROOM_SOUP) && !drop.getType().equals(Material.WOOD_SWORD) && !drop.getType().equals(Material.BOWL) && !drop.getType().equals(Material.POTION) && !drop.getType().equals(Material.COOKIE)){
				toRemoveDrops.add(drop);
			}
		}
		event.getDrops().removeAll(toRemoveDrops);
		
		String deathMessage = ChatColor.RED + player.getName() + ChatColor.GRAY + " died!";

		if (Core.getInstance().getDamageManager().hasLastDamaged(player)) {
			Player killer = Core.getInstance().getDamageManager().getLastDamaged(player);
			if(Core.getInstance().getUserManager().getUser(killer).getActiveLoadout() != null){
			deathMessage = ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.RED
					+ killer.getName() + ChatColor.GRAY + " with loadout "
					+ Core.getInstance().getUserManager().getUser(killer).getActiveLoadout().getName() + ChatColor.GRAY
					+ "!";
			}
			Random random = new Random();
			//random between 10-20
			int moneyPerKill = random.nextInt(11) + 10;
			killer.sendMessage(ChatColor.GRAY + "You have been awarded with " + ChatColor.GOLD + moneyPerKill
					+ ChatColor.GRAY + " coins!");
							// "and " + ChatColor.DARK_AQUA + "10" + ChatColor.GRAY + " xp!");
//			Core.getInstance().getUserManager().getUser(killer).addExperience(10);
			Core.getInstance().getUserManager().getUser(killer).addMoney(moneyPerKill);
			
			//TODO make random
//			double r = random.nextInt(101*100);
//			if(r*100 < Core.getInstance().getUserManager().getUser(killer).getChestFindChance()){
				this.giveRandomSeries(killer);
//			}
		}

		event.setDeathMessage(deathMessage);

	}
	
	private void giveRandomSeries(Player killer){
		Random random = new Random();
		int i = random.nextInt(5);
		if(i == 0){
			Core.getInstance().getUserManager().getUser(killer).addSeries(UnlockableSeries.LASER_ABILITY);
			killer.sendMessage(ChatColor.GREEN + "Found " + UnlockableSeries.LASER_ABILITY.getDisplay() + ChatColor.GREEN + " series!");
		}
		if(i == 2){
			Core.getInstance().getUserManager().getUser(killer).addSeries(UnlockableSeries.WAR_ABILITY);
			killer.sendMessage(ChatColor.GREEN + "Found " + UnlockableSeries.WAR_ABILITY.getDisplay() + ChatColor.GREEN + " series!");

		}
		if(i == 3){
			Core.getInstance().getUserManager().getUser(killer).addSeries(UnlockableSeries.SUPERHERO_ABILITY);
			killer.sendMessage(ChatColor.GREEN + "Found " + UnlockableSeries.SUPERHERO_ABILITY.getDisplay() + ChatColor.GREEN + " series!");

		}
		if(i == 4){
			Core.getInstance().getUserManager().getUser(killer).addSeries(UnlockableSeries.HEIGHTS_ABILITY);
			killer.sendMessage(ChatColor.GREEN + "Found " + UnlockableSeries.HEIGHTS_ABILITY.getDisplay() + ChatColor.GREEN + " series!");

		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		Player player=  event.getPlayer();
		player.teleport(player.getWorld().getSpawnLocation());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

			@Override
			public void run() {
				Core.getInstance().getUserManager().getUser(player.getUniqueId().toString()).giveSpawnInventory();
				
			}
			
		}, 1);
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
		if (user.isSafe() && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
		if(event.getItem().getItemStack().getType().equals(Material.BOWL) || event.getItem().getItemStack().getType().equals(Material.MUSHROOM_SOUP)){
			if(!user.getSpecialty().equals(Specialty.SOUPER)){
				event.setCancelled(true);
			}
		}
		if(event.getItem().getItemStack().getType().equals(Material.POTION)){
			if(!user.getSpecialty().equals(Specialty.CHEMIST)){
				event.setCancelled(true);
			}
		}
		if(event.getItem().getItemStack().getType().equals(Material.COOKIE)){
			if(!user.getSpecialty().equals(Specialty.COOK)){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if(Core.getInstance().getUserManager().getUser(p).getSpecialty().equals(Specialty.BRUTE)){
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
		if (user.isSafe() && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}else{
			if(!event.getItemDrop().getItemStack().getType().equals(Material.POTION) && !event.getItemDrop().getItemStack().getType().equals(Material.MUSHROOM_SOUP) && !event.getItemDrop().getItemStack().getType().equals(Material.COOKIE) && !event.getItemDrop().getItemStack().getType().equals(Material.BOWL) && !event.getItemDrop().getItemStack().getType().equals(Material.WOOD_SWORD) ){
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && event.getPlayer().isOp())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent event){
		LoadDataTask ldt = new LoadDataTask(event.getUniqueId().toString());
		ldt.runTaskAsynchronously(Core.getInstance());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		int numb = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.networkManager.getVersion();
		if(numb == 47){
			event.getPlayer().kickPlayer(ChatColor.RED + "Please join using 1.7!");
			event.setJoinMessage("");
			return;
		}
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer().getUniqueId().toString());
		if (user == null) {
			user = new User(event.getPlayer().getUniqueId().toString());
			users.add(user);
		}
//		if (user.getRank() != null)
//			event.setJoinMessage(ChatColor.GREEN + "+ " + user.getRank().getColor() + event.getPlayer().getName());
//		else
		if(event.getPlayer().hasPlayedBefore())
			event.setJoinMessage(ChatColor.GREEN + "+ " + ChatColor.WHITE + event.getPlayer().getName());
		else
			event.setJoinMessage(ChatColor.GREEN + "+ " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.LIGHT_PURPLE + " (new!)");
		user.getPlayer().setPlayerListName(user.getRank().getColor() + user.getPlayer().getName());
		user.getPlayer().setCustomName(user.getRank().getColor() + user.getPlayer().getName());
		user.getPlayer().setDisplayName(user.getRank().getColor() + user.getPlayer().getName());
		user.setSafe(true);
		user.giveSpawnInventory();
		if(user != null && user.getRank() != null){
			event.getPlayer().setPlayerListName(user.getRank().getColor() + event.getPlayer().getName());
			event.getPlayer().setCustomName(user.getRank().getColor() + event.getPlayer().getName());
			event.getPlayer().setDisplayName(user.getRank().getColor() + event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				player.sendMessage(
//						ChatColor.DARK_AQUA.toString() + Core.getInstance().getUserManager().getUser(event.getPlayer()).getLevel() + " " + 
						Core.getInstance().getUserManager().getUser(event.getPlayer()).getRank().getColor() +
				Core.getInstance().getUserManager().getUser(event.getPlayer()).getRank().getPrefix() +
				(Core.getInstance().getUserManager().getUser(event.getPlayer()).getRank().equals(Rank.DEFAULT) ? "" : " ")
						+ event.getPlayer().getName() + ": " + ChatColor.GRAY + event.getMessage());
				if (event.getMessage().toUpperCase().contains(player.getName().toUpperCase())) {
					player.playSound(player.getLocation(), Sound.CAT_MEOW, 1, 1);
				}
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onSpawn(EntitySpawnEvent event){
		if(event.getEntity() instanceof Item){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

				@Override
				public void run() {
					event.getEntity().remove();
				}
				
			}, 20*60);
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getItem() == null)
				return;
			if (event.getItem().getType() == null)
				return;
			if (event.getItem().getType().equals(Material.MUSHROOM_SOUP)) {
				Damageable dm = event.getPlayer();
				if(Core.getInstance().getUserManager().getUser(event.getPlayer()).getSpecialty().equals(Specialty.SOUPER)){
					if (dm.getHealth() != dm.getMaxHealth()) {
						this.eatSoup(event.getPlayer());
						event.getItem().setType(Material.BOWL);
						event.getPlayer().updateInventory();
					}
				}
			}
			if (event.getItem().getType().equals(Material.COOKIE)) {
				Damageable dm = event.getPlayer();
				if(Core.getInstance().getUserManager().getUser(event.getPlayer()).getSpecialty().equals(Specialty.COOK)){
					if (dm.getHealth() != dm.getMaxHealth()) {
						this.eatCookie(event.getPlayer());
						if(event.getPlayer().getItemInHand().getAmount()-1 == 0){
							event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
						}else{
							event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
						}
						event.getPlayer().updateInventory();
					}
				}
			}
		}
	}

	private void eatSoup(Player player) {
		Damageable dm = player;
		if (dm.getHealth() < dm.getMaxHealth()) {
			if (dm.getHealth() + 7 > dm.getMaxHealth()) {
				player.setHealth(dm.getMaxHealth());
			} else {
				player.setHealth(dm.getHealth() + 7);
			}
		}
	}
	
	private void eatCookie(Player player) {
		Damageable dm = player;
		if (dm.getHealth() < dm.getMaxHealth()) {
			if (dm.getHealth() + 4 > dm.getMaxHealth()) {
				player.setHealth(dm.getMaxHealth());
			} else {
				player.setHealth(dm.getHealth() + 4);
			}
		}
	}

	public void removeUser(User user) {
		if (users.contains(user))
			users.remove(user);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		event.setQuitMessage(ChatColor.RED + "- " + ChatColor.WHITE + event.getPlayer().getName());
		leave(event.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		leave(event.getPlayer());
	}

	private void leave(Player player) {
		if(Core.getInstance().getGameManager().isGameActive()){
			for(Player p : Core.getInstance().getGameManager().getActiveGame().getPlayers()){
				if(p.equals(player)){
					Core.getInstance().getGameManager().getActiveGame().lose(p);
					break;
				}
			}
		}
		PushDataTask pdt = new PushDataTask(player.getUniqueId().toString());
		pdt.runTaskAsynchronously(Core.getInstance());
		if (!isLoadedUser(player))
			return;
	}

}
