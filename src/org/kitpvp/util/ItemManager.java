package org.kitpvp.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {

	public ItemManager(){
		
	}
	
	public ItemStack getUnlockableOpener(){
		ItemStack uO = new ItemStack(Material.CHEST);
		ItemMeta uOM = uO.getItemMeta();
		uOM.setDisplayName(ChatColor.GREEN + "Unlockables " + ChatColor.GRAY + "(Right click)");
		uO.setItemMeta(uOM);
		return uO;
	}
	
}
