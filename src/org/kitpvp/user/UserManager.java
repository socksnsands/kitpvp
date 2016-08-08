package org.kitpvp.user;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.kitpvp.core.Core;
import org.kitpvp.core.LoadDataTask;
import org.kitpvp.core.PushDataTask;
import org.kitpvp.unlockable.UnlockableSeries;

public class UserManager implements Listener {

	private ArrayList<User> users = new ArrayList<User>();
	
	public UserManager(){
		initialLoad();
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	public User getUser(Player player){
		for(User user : users)
			if(user.getPlayer() != null)
				if(user.getPlayer().equals(player))
					return user;
		return null;
	}
	
	public User getUser(String uuid){
		for(User user : users)
			if(user.getUUID().equals(uuid.toString()))
				return user;
		return null;
	}
	
	public boolean isLoadedUser(Player player){
		for(User user : users)
			if(user.getPlayer().equals(player))
				return true;
		return false;
	}
	
	public ArrayList<User> getUsers(){
		return this.users;
	}
	
	private void initialLoad(){
		if(!users.isEmpty())
			return;
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			User user = new User(player.getUniqueId().toString());
			users.add(user);
			user.setPlayer(player);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(this.getUser(player).isSafe()){
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event){
		if(event.isCancelled())
			return;
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Core.getInstance().getDamageManager().setLastDamaged((Player)event.getEntity(), (Player)event.getDamager());
		}
		if(event.getDamager() instanceof Projectile && event.getEntity() instanceof Player){
			Projectile p = (Projectile)event.getDamager();
			if(p.getShooter() instanceof Player){
				Core.getInstance().getDamageManager().setLastDamaged((Player)event.getEntity(), (Player)p.getShooter());
			}
		}
	}
	
	@EventHandler
	public void onLoseHunger(FoodLevelChangeEvent event){
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
		if(event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Kit Selector")){
			Player player = (Player) event.getWhoClicked();
			if(event.getCurrentItem().getItemMeta() != null){
				String current = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				User user = Core.getInstance().getUserManager().getUser(player);
				if(user.hasLoadout(current)){
					user.getLoadout(current).apply(player);
				}
			}
			event.setCancelled(true);
		}
		}
	}
	
	@EventHandler
	public void onPreLogin(AsyncPlayerPreLoginEvent e){
		LoadDataTask ldt = new LoadDataTask(e.getUniqueId().toString());
		ldt.runTaskAsynchronously(Core.getInstance());
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		PushDataTask pdt = new PushDataTask(e.getPlayer().getUniqueId().toString());
		pdt.runTaskAsynchronously(Core.getInstance());
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getPlayer().getItemInHand().equals(Core.getInstance().getItemManager().getFFAItem())){
				User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
				if(user.getLoadouts().size() > 0){
					user.openKitSelector();
				}else{
					event.getPlayer().sendMessage(ChatColor.RED + "No loadouts created yet. Open an anvil to create one!");
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		Core.getInstance().getUserManager().getUser(event.getPlayer()).giveSpawnInventory();
		Core.getInstance().getUserManager().getUser(event.getPlayer()).setSafe(true);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		User user = Core.getInstance().getUserManager().getUser(player);
		user.removeActiveLoadout();
		user.getActiveAbilities().clear();
		user.clearCooldowns();
		
		String deathMessage = ChatColor.RED + player.getName() + ChatColor.GRAY + " died!";
		
		if(Core.getInstance().getDamageManager().hasLastDamaged(player)){
			Player killer = Core.getInstance().getDamageManager().getLastDamaged(player);
			deathMessage = ChatColor.RED + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.RED + killer.getName() + ChatColor.GRAY + " with loadout " + Core.getInstance().getUserManager().getUser(killer).getActiveLoadout().getName() + ChatColor.GRAY + "!";
			int moneyPerKill = 10;
			killer.sendMessage(ChatColor.GRAY + "You have been awarded with " + ChatColor.GOLD + moneyPerKill + ChatColor.GRAY + " coins!");
			Core.getInstance().getUserManager().getUser(killer).addMoney(moneyPerKill);
		}
		
		event.setDeathMessage(deathMessage);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event){
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
		if(user.isSafe() && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
		if(user.isSafe() && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && event.getPlayer().isOp())
			return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		for(User user : users)
			if(user.getPlayer() != null)
				if(user.getPlayer().equals(event.getPlayer()))
				return;
		Core.getInstance().getUserManager().getUser(event.getPlayer().getUniqueId().toString()).setPlayer(event.getPlayer());
		User user = Core.getInstance().getUserManager().getUser(event.getPlayer());
		users.add(user);
		user.setSafe(true);
		user.giveSpawnInventory();
		if(user.getPlayer().getName().equals("_Ug")){
			for(UnlockableSeries series : UnlockableSeries.values()){
				user.addSeries(series);
			}
		}
		event.setJoinMessage(ChatColor.GREEN + "+ " +  Core.getInstance().getUserManager().getUser(event.getPlayer()).getRank().getColor() + event.getPlayer().getName());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		if(!event.isCancelled()){
			for(Player player:  Bukkit.getServer().getOnlinePlayers()){
				player.sendMessage(Core.getInstance().getUserManager().getUser(event.getPlayer()).getRank().getColor() + event.getPlayer().getName() + ": " + ChatColor.GRAY + event.getMessage());
				if (event.getMessage().toUpperCase().contains(player.getName().toUpperCase())) {
					player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
				}
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getItem() == null)
				return;
			if(event.getItem().getType() == null)
				return;
			if(event.getItem().getType().equals(Material.MUSHROOM_SOUP)){
				if(event.getPlayer().getHealth() != event.getPlayer().getMaxHealth()){
					this.eatSoup(event.getPlayer());
					event.getItem().setType(Material.BOWL);
					event.getPlayer().updateInventory();
				}
			}
		}
	}
	
	private void eatSoup(Player player){
			if(player.getHealth() < player.getMaxHealth()){
				if(player.getHealth() + 7 > player.getMaxHealth()){
					player.setHealth(player.getMaxHealth());
				}else{
					player.setHealth(player.getHealth() + 7);
				}
			}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		event.setQuitMessage(ChatColor.RED + "- " +  Core.getInstance().getUserManager().getUser(event.getPlayer()).getRank().getColor() + event.getPlayer().getName());
		leave(event.getPlayer());	
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event){
		leave(event.getPlayer());
	}
	
	private void leave(Player player){
		if(!isLoadedUser(player))
			return;
		users.remove(getUser(player));
	}
	
	
}
