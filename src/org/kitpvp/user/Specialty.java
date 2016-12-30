package org.kitpvp.user;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Specialty {

	SOUPER, CHEMIST, COOK, BRUTE;
	
	public Specialty getDefaultSpecialty(){
		return CHEMIST;
	}
	
	public ItemStack getIcon(Specialty specialty){
		Material m = Material.MUSHROOM_SOUP;
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		if(specialty.equals(SOUPER)){
			lore.addAll(Arrays.asList(ChatColor.GRAY + "Get soups that heal", ChatColor.RED + "3.5 " + ChatColor.GRAY + "hearts."));
		}
		if(specialty.equals(CHEMIST)){
			m = Material.GLASS_BOTTLE;
			lore.addAll(Arrays.asList(ChatColor.GRAY + "Get splash potions that", ChatColor.GRAY + "heal up to " + ChatColor.RED + "4" + ChatColor.DARK_GRAY + " hearts."));
		}
		if(specialty.equals(COOK)){
			m = Material.COOKIE;
			lore.addAll(Arrays.asList(ChatColor.GRAY + "Get " + ChatColor.RED + "40" + ChatColor.GRAY + " stacked cookies", ChatColor.GRAY + "that heal " + ChatColor.RED + "2 " + ChatColor.GRAY + "hearts."));
		}
		if(specialty.equals(BRUTE)){
			m = Material.DIAMOND_CHESTPLATE;
			lore.addAll(Arrays.asList(ChatColor.GRAY + "Get " + ChatColor.RED + "140" + ChatColor.GRAY + " health."));
		}
		ItemStack item = new ItemStack(m);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
}
