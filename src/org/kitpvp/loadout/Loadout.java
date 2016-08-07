package org.kitpvp.loadout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;

public class Loadout {
	
	final int maxPoints = 30;
	
	private String name;
	private ArrayList<Ability> abilities = new ArrayList<Ability>();
	
	public Loadout(String name, ArrayList<Ability> abilities){
		this.name = name;
		this.abilities = abilities;
	}
	
	public void apply(Player player){
		player.closeInventory();
		Core.getInstance().getUserManager().getUser(player).resetInventory();
		Inventory inv = player.getInventory();
		net.minecraft.server.v1_10_R1.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.STONE_SWORD));
		NBTTagCompound tag = new NBTTagCompound(); 
		tag.setBoolean("Unbreakable", true);
		stack.setTag(tag);
		ItemStack is = CraftItemStack.asCraftMirror(stack);
		
		inv.setItem(0, is);
		Core.getInstance().getUserManager().getUser(player).getActiveAbilities().clear();
		Core.getInstance().getUserManager().getUser(player).clearCooldowns();
		for(Ability ability : this.abilities){
			Core.getInstance().getUserManager().getUser(player).getActiveAbilities().add(ability);
			if(ability.getClickedItem() != null){
				inv.addItem(ability.getClickedItem());
			}
		}
		do{
			inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}while(!(inv.firstEmpty() < 0));
		Core.getInstance().getUserManager().getUser(player).setSafe(false);
	}
	
	public int getPointValue(){
		int value = 0;
		for(Ability ability : this.abilities){
			value += ability.getPoints();
		}
		return value;
	}
	
	public int getMaxPoints(){
		return this.maxPoints;
	}
	
	public ItemStack getSelectableIcon(){
		ArrayList<String> lore = new ArrayList<>();
		lore.addAll(Arrays.asList("", ChatColor.GRAY + "Points: " + ChatColor.YELLOW + this.getPointValue() + "/" + this.getMaxPoints(), ChatColor.GRAY + "Active abilities:"));
		for(Ability ability : this.abilities){
			lore.add(ChatColor.GRAY + " - " + ability.getScarcity().getColor() + ability.getName());
		}
		return Core.getInstance().getItemManager().createItem(ChatColor.WHITE + this.getName(), Material.PISTON_BASE, (byte)0, 1, lore);
	}
	
	public void openEditScreen(Player player){
		Inventory es = Bukkit.getServer().createInventory(player, 54, "Edit kit: " + this.getPointValue() + "/" + this.getMaxPoints() + " " + this.getName());
		for(int i = 0; i < this.abilities.size(); i++){
			ItemStack item = this.abilities.get(i).getIcon();
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			lore.add("");
			lore.add(ChatColor.GREEN + "Activated");
			im.setLore(lore);
			item.setItemMeta(im);
			
			es.setItem(i, item);
			
		}
		for(int i = 0; i < 18; i++)
			if(es.getItem(i) == null)
					es.setItem(i, Core.getInstance().getItemManager().createItem(" ", Material.STAINED_GLASS_PANE, (byte)15, 1, null));
		for(Ability ability : Core.getInstance().getUserManager().getUser(player).getOwnedAbilities())
			if(!this.abilities.contains(ability))
				es.addItem(ability.getIcon());
		player.openInventory(es);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void addAbility(Ability ability){
		if(!this.abilities.contains(ability))
			this.abilities.add(ability);
	}
	
	public void removeAbility(Ability ability){
		if(this.abilities.contains(ability))
			this.abilities.remove(ability);
	}
	
	public void refreshEditScreen(Player player){
		player.closeInventory();
		this.openEditScreen(player);
	}
	
	public void resetLoadout(){
		this.abilities.clear();
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<Ability> getAbilities(){
		return this.abilities;
	}
	
}
