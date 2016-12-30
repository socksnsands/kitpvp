package org.kitpvp.util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemManager {

	public ItemManager() {

	}

	public ItemStack createItem(String name, Material material, byte data, int amount, List<String> lore) {
		ItemStack item = new ItemStack(material, amount, data);
		ItemMeta im = item.getItemMeta();
		if (name != "")
			im.setDisplayName(name);
		if (lore != null)
			im.setLore(lore);
		item.setItemMeta(im);
//		net.minecraft.server.v1_10_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
//		NBTTagCompound tag = new NBTTagCompound();
//		tag.setBoolean("Unbreakable", true);
//		stack.setTag(tag);
//		ItemStack is = CraftItemStack.asCraftMirror(stack);
		return item;
	}

	public ItemStack getFFAItem() {
		return this.createItem(ChatColor.GREEN + "FFA " + ChatColor.GRAY + "(Right click)", Material.COMPASS, (byte) 0,
				1, null);
	}

	public ItemStack getUnlockableOpener() {
		return this.createItem(ChatColor.GREEN + "Unlockables " + ChatColor.GRAY + "(Right click)", Material.TRIPWIRE_HOOK,
				(byte) 0, 1, null);
	}

}
