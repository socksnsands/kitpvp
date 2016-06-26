package org.kitpvp.kit;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public abstract class Kit {

	private HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
	
	public Kit(String name){
		//TODO i'll do kits later too.. if we have them.. not sure about it though.. we can talk :P - _Ug
	}
	
	public void addItem(int location, ItemStack item){
		items.put(location, item);
	}
	
	public ItemStack getItem(int location){
		if(items.get(location) != null)
			return items.get(location);
		return null;
	}
	
}
