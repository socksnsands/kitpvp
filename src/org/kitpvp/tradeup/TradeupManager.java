package org.kitpvp.tradeup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;

public class TradeupManager implements Listener {

	private String tradeupGuiName = ChatColor.UNDERLINE + "Tradeup Menu";
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getClickedBlock().getType().equals(Material.WORKBENCH)){
				Player player = event.getPlayer();
				User user = Core.getInstance().getUserManager().getUser(player);
				if(user.isSafe()){
					this.openTradeupMenu(player);
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(!event.getInventory().getName().equals(this.tradeupGuiName))
			return;
		event.setCancelled(true);
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			User user = Core.getInstance().getUserManager().getUser(player);
			ItemStack item = event.getCurrentItem();
			String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
			if(Core.getInstance().getAbilityManager().isAbility(name)){
				Ability ability = Core.getInstance().getAbilityManager().getAbility(name);
				if(this.hasAbilityBelow(ability)){
					Ability below = this.getAbilityBelow(ability);
					int amount =  user.getOwnedUnlockablesAsHashmap().containsKey(below) ? user.getOwnedUnlockablesAsHashmap().get(below) : 0;
					if(amount >= 6){
						for(int i = 0; i < 6; i++)
							user.removeUnlockable(below);
						user.addUnlockable(ability);
						player.sendMessage(ChatColor.GRAY + "Successfully upgraded " + below.getScarcity().getColor() + below.getName() + ChatColor.GRAY +" for " + ability.getScarcity().getColor() + ability.getName() + ChatColor.GRAY + "!");
						player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 1);
						player.closeInventory();
					}else{
						player.sendMessage(ChatColor.GRAY + "Error! You need at least 6 of " + below.getScarcity().getColor() + below.getName() + ChatColor.GRAY +" for " + ability.getScarcity().getColor() + ability.getName() + ChatColor.GRAY + "!");
						player.closeInventory();
					}
				}
			}
		}
	}
	
	private boolean hasAbilityBelow(Ability ability){
		return getAbilityBelow(ability) != null;
	}
	
	private Ability getAbilityBelow(Ability ability){
		String name = ability.getName();
		if(!name.contains(" I"))
			return null;
		name = name.split(" I")[0];
		if(ability.getLevel() != 2){
			if(ability.getLevel() == 3)
				name = name + " II";
			if(ability.getLevel() == 4)
				name = name + " III";
		}
		return Core.getInstance().getAbilityManager().getAbility(name);
	}
	
	private void openTradeupMenu(Player player){
		Inventory gui = Bukkit.getServer().createInventory(player, 54, tradeupGuiName);
		User user = Core.getInstance().getUserManager().getUser(player);
		for(Ability ability : Core.getInstance().getAbilityManager().getAbilities()){
			if(ability.getLevel() > 1){
				if(this.hasAbilityBelow(ability)){
					Ability below = this.getAbilityBelow(ability);
					ItemStack item = ability.getIcon();
					List<String> lore = item.getItemMeta().getLore();
					int amount =  user.getOwnedUnlockablesAsHashmap().containsKey(below) ? user.getOwnedUnlockablesAsHashmap().get(below) : 0;
					if(!(amount < 6)){
						lore.addAll(Arrays.asList("", ChatColor.GRAY + "You have: " + ChatColor.YELLOW + amount + "/6", ChatColor.GRAY + "Of: " + below.getScarcity().getColor() + below.getName()));
						ItemMeta im = item.getItemMeta();
						im.setLore(lore);
						item.setItemMeta(im);
						gui.addItem(item);
					}
				}
			}
		}
		if(gui.getItem(0) == null || gui.getItem(0).getType().equals(Material.AIR)){
			ItemStack n = new ItemStack(Material.REDSTONE_BLOCK);
			ItemMeta nm = n.getItemMeta();
			nm.setDisplayName(ChatColor.DARK_RED + "None Available!");
			nm.setLore(Arrays.asList("", ChatColor.GRAY + "You do not have enough of", ChatColor.GRAY + "any ability to be able", ChatColor.GRAY + "to trade it up!"));
			n.setItemMeta(nm);
			gui.addItem(n);
		}
			
		player.openInventory(gui);
	}
	
}
