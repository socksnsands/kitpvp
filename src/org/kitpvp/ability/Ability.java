package org.kitpvp.ability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.core.Core;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.util.ActionBar;

public abstract class Ability extends Unlockable {
		
	private ItemStack clickedItem;
	
	private boolean hasCooldown = false;
	private int cooldownTicks = 10;
		
	public Ability(String name, String description, ItemStack icon, Scarcity scarcity) {
		super(name, description, scarcity);
		this.setIcon(icon);
	}
	
	public Ability(String name, String description, Material icon, Scarcity scarcity) {
		super(name, description, scarcity);
		this.setIcon(new ItemStack(icon));
	}

	protected void setClickedItem(ItemStack clickedItem){
		this.clickedItem = clickedItem;
	}
	
	protected void setClickedItem(Material clickedItem){
		this.clickedItem = new ItemStack(clickedItem);
	}
	
	final public AbilityUseEvent callEvent(Player player, Ability ability){
		AbilityUseEvent event = new AbilityUseEvent(player, ability);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}
	
	protected void setCooldown(int cooldownTicks){
		this.hasCooldown = true;
		this.cooldownTicks = cooldownTicks;
	}
	
	public void onInteract(Player player, Action action){}
	
	public void onFinishCooldown(Player player, Ability ability){
		ActionBar ab = new ActionBar(ChatColor.YELLOW + "You can now use " + ability.getScarcity().getColor() + ability.getName() + ChatColor.YELLOW + " again!");
		ab.sendToPlayer(player);
	}

	
	protected void putOnCooldown(Player player){
		Core.getInstance().getUserManager().getUser(player).addCooldown(this);
	}
	
	public int getCooldownTicks(){
		if(hasCooldown)
			return this.cooldownTicks;
		return -1;
	}

	public ItemStack getClickedItem(){
		ItemStack f = clickedItem;
		if(f == null)
			return null;
		ItemMeta fm = f.getItemMeta();
		fm.setDisplayName(super.getScarcity().getColor() + super.getName());
		f.setItemMeta(fm);
		return f;
	}

}
