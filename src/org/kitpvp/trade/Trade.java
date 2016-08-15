package org.kitpvp.trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.util.ItemManager;

public class Trade {

	private Player p1, p2;
	
	private HashMap<Player, ArrayList<Ability>> abilities = new HashMap<Player, ArrayList<Ability>>();
	private HashMap<Player, ArrayList<UnlockableSeries>> series = new HashMap<Player, ArrayList<UnlockableSeries>>();
	private HashMap<Player, Integer> money = new HashMap<Player, Integer>();

	private HashMap<Player, Integer> confirmationLevel = new HashMap<Player, Integer>();
	
	
	public Trade(Player p1, Player p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public void displayTradeMenu(Player player){
		if(player.equals(p1) || player.equals(p2)){
			Inventory inventory = Bukkit.getServer().createInventory(player, 54, ChatColor.UNDERLINE + "Trade");
			ItemManager im = Core.getInstance().getItemManager();
			inventory.setItem(0, im.createItem(ChatColor.GOLD + player.getName(), Material.SKULL_ITEM, (byte)3, 1, null));
			inventory.setItem(1, im.createItem(ChatColor.YELLOW + "Add abilities", Material.ENCHANTMENT_TABLE, (byte)0, 1, null));
			inventory.setItem(2, im.createItem(ChatColor.YELLOW + "Add series", Material.CHEST, (byte)0, 1, null));
			inventory.setItem(3, im.createItem(ChatColor.YELLOW + "Add money", Material.GOLD_INGOT, (byte)0, 1, null));

			ItemStack blank = im.createItem(" ", Material.STAINED_GLASS_PANE, (byte)15, 1, null);
			
			for(int i = 0; i < 9; i++)
				inventory.setItem(i+9, blank);
			for(int i = 0; i < 5; i++)
				inventory.setItem(i*9 + 9 + 5, blank);
			
			inventory.setItem(8, im.createItem(ChatColor.GOLD + this.getOtherPlayer(player).getName(), Material.SKULL_ITEM, (byte)3, 1, null));
			
			inventory.setItem(8+5, this.getConfirmationItem(player));
			
			List<Integer> ints = Arrays.asList(18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48);
			
			for(int i = 0; i < abilities.get(player).size(); i++){
				Ability ability = abilities.get(player).get(i);
				int slot = 18+i;
				if(!ints.contains(slot)){
					if(!(slot > ints.get(ints.size() - 1))){
						do{
							slot++;
							if(slot > 60)
								break;
						}while(!ints.contains(slot));
					}else{
						break;
					}
				}
				inventory.setItem(slot, ability.getIcon());
			}
			
			player.openInventory(inventory);
		}
	}
	
	private ItemStack getConfirmationItem(Player player){
		ItemManager im = Core.getInstance().getItemManager();
		if(this.confirmationLevel.get(player) == 0){
			if(this.confirmationLevel.get(this.getOtherPlayer(player)) == 0){
				return im.createItem(ChatColor.RED + "Confirm trade", Material.REDSTONE_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has not yet confirmed their contents!"));
			}else{
				return im.createItem(ChatColor.RED + "Confirm trade", Material.REDSTONE_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has confirmed their contents!"));
			}
		}else if(this.confirmationLevel.get(player) == 1){
			if(this.confirmationLevel.get(this.getOtherPlayer(player)) == 0){
				return im.createItem(ChatColor.GREEN + "Finalize trade", Material.EMERALD_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has not yet confirmed their contents!"));
			}else if(this.confirmationLevel.get(this.getOtherPlayer(player)) == 1){
				return im.createItem(ChatColor.GREEN + "Finalize trade", Material.EMERALD_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has confirmed their contents!"));
			}else{
				return im.createItem(ChatColor.GREEN + "Finalize trade", Material.EMERALD_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has agreed to finalize!"));
			}
		}else{
			if(this.confirmationLevel.get(this.getOtherPlayer(player)) == 1){
				return im.createItem(ChatColor.AQUA + "Waiting", Material.DIAMOND_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has not yet agreed to finalize!"));
			}else{
				return im.createItem(ChatColor.GOLD + "Trade success!", Material.GOLD_BLOCK, (byte)0, 1, Arrays.asList("", ChatColor.GRAY + this.getOtherPlayer(player).getName() + " has confirmed their contents!"));
			}
		}
	}
	
	private Player getOtherPlayer(Player player){
		if(player.equals(p1)){
			return p2;
		}else if(player.equals(p2)){
			return p1;
		}
		System.out.println("Other player not found!");
		return null;
	}
	
	public List<Player> getPlayers(){
		return Arrays.asList(p1, p2);
	}
	
}
