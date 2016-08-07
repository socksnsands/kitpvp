package org.kitpvp.loadout;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.AnvilGui;

public class LoadoutManager implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(event.getClickedBlock().getType().equals(Material.ANVIL)){
					if(Core.getInstance().getUserManager().getUser(event.getPlayer()).isSafe()){
					event.setCancelled(true);
					Core.getInstance().getUserManager().getUser(event.getPlayer()).openKitEditor();
					}
				}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			User user = Core.getInstance().getUserManager().getUser(player);
			if(event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Kit Editor")){
				event.setCancelled(true);
				if(event.getCurrentItem() != null){
				if(event.getCurrentItem().getType().equals(Material.PISTON_BASE)){
					String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
					if(user.hasLoadout(name)){
						user.getLoadout(name).openEditScreen(player);
					}
				}else if(event.getCurrentItem().getType().equals(Material.ANVIL)){
					player.sendMessage(ChatColor.GRAY + "Creating a new kit!");

					AnvilGui gui = new AnvilGui(player, new AnvilGui.AnvilClickEventHandler(){
						@Override
						public void onAnvilClick(AnvilGui.AnvilClickEvent event){
							String name = "random";
						if(event.getSlot() == AnvilGui.AnvilSlot.OUTPUT){
						event.setWillClose(true);
						event.setWillDestroy(true);
						 
						name = ChatColor.translateAlternateColorCodes('&', event.getName());
						} else {
						event.setWillClose(false);
						event.setWillDestroy(false);
						}
						
						if(name.equals("random")){
						Random random = new Random();
						name = "" + random.nextInt(1000);
						player.sendMessage(ChatColor.GRAY + "Temporary name: \"" + ChatColor.GREEN + name + ChatColor.GRAY + "\" chosen!");
						}else{
							player.sendMessage(ChatColor.GRAY + "Kit: \"" + ChatColor.GREEN + name + ChatColor.GRAY + "\" created!");
						}
						
						
						Loadout loadout = new Loadout(name, new ArrayList<Ability>());
						user.addLoadout(loadout);
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable(){

							@Override
							public void run() {
								loadout.openEditScreen(player);
							}
							
						}, 3);
						
						}
						});
						gui.setSlot(AnvilGui.AnvilSlot.INPUT_LEFT, Core.getInstance().getItemManager().createItem("Kit Name", Material.NAME_TAG, (byte)0, 1, null));
						 
						gui.open();
					
				}
				}
			}
			if(event.getInventory().getTitle().startsWith("Edit kit: ")){
				event.setCancelled(true);
				String title = event.getInventory().getTitle();
				String loadoutName = ChatColor.stripColor(title.split("/30 ")[1]);
				if(!user.hasLoadout(loadoutName)){
					player.sendMessage(ChatColor.DARK_RED + "Loadout not found! Please contact an admin!");
					player.closeInventory();
					return;
				}
				Loadout loadout = user.getLoadout(loadoutName);
				if(event.getCurrentItem().getItemMeta() == null)
					return;
				if(event.getCurrentItem() != null)
					if(event.getCurrentItem().getItemMeta() != null)
						if(event.getCurrentItem().getItemMeta().getLore() != null)
				if(event.getCurrentItem().getItemMeta().getLore().contains(ChatColor.GREEN + "Activated")){
					loadout.removeAbility(Core.getInstance().getAbilityManager().getAbility(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())));
					loadout.refreshEditScreen(player);
				}else if(Core.getInstance().getAbilityManager().isAbility(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))){
					Ability t2a = Core.getInstance().getAbilityManager().getAbility(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
					if(loadout.getPointValue() + t2a.getPoints() <= 30){
						loadout.addAbility(t2a);
						loadout.refreshEditScreen(player);
					}else{
						player.sendMessage(ChatColor.RED + "This will make your loadout have too many points! (" + (loadout.getPointValue() + t2a.getPoints()) + "/" + loadout.getMaxPoints() + ")");
					}
				}
			}
		}
	}
	
}
