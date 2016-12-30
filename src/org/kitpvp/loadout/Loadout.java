package org.kitpvp.loadout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R4.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.Specialty;
import org.kitpvp.user.User;

public class Loadout {

	final int maxPoints = 30;

	private String name;
	private ArrayList<Ability> abilities = new ArrayList<Ability>();

	public Loadout(String name, ArrayList<Ability> abilities) {
		this.name = ChatColor.translateAlternateColorCodes('&', name);
		this.abilities = abilities;
	}

	private Location randomMapLocation(){
		Random random = new Random();
		int xa = random.nextInt(50);
		int za = random.nextInt(113);
		Location location = new Location(Bukkit.getWorld("world"), 429+xa, 0, -80+za);
		int y = location.getWorld().getHighestBlockYAt(location);
		Location l = new Location(Bukkit.getWorld("world"), location.getX(), y, location.getZ()).clone().add(0,1,0);
		return l;
	}
	
	public void apply(Player player) {
		User user = Core.getInstance().getUserManager().getUser(player);
		player.closeInventory();
		//TODO change map
		player.teleport(randomMapLocation());
		player.sendMessage(ChatColor.GREEN + "This map is temporary!");
		Core.getInstance().getUserManager().getUser(player).resetInventory();
		Inventory inv = player.getInventory();
		net.minecraft.server.v1_7_R4.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.WOOD_SWORD));
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("Unbreakable", true);
		stack.setTag(tag);
		ItemStack is = CraftItemStack.asCraftMirror(stack);
		
		net.minecraft.server.v1_7_R4.ItemStack chest = CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_CHESTPLATE));
		chest.setTag(tag);
		ItemStack hIs = CraftItemStack.asCraftMirror(chest);
		
		player.getInventory().setChestplate(hIs);
		
		net.minecraft.server.v1_7_R4.ItemStack leggings = CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_LEGGINGS));
		leggings.setTag(tag);
		ItemStack lIs = CraftItemStack.asCraftMirror(leggings);
		
		player.getInventory().setLeggings(lIs);
		
		net.minecraft.server.v1_7_R4.ItemStack boots = CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_BOOTS));
		boots.setTag(tag);
		ItemStack bIs = CraftItemStack.asCraftMirror(boots);
		
		player.getInventory().setBoots(bIs);
		
//		ItemStack is = new ItemStack(Material.WOOD_SWORD);
//		is.addUnsafeEnchantment(Enchantment.DURABILITY, 100);

		inv.setItem(0, is);
		user.getActiveAbilities().clear();
		user.clearCooldowns();
		for (Ability ability : this.abilities) {
			Core.getInstance().getUserManager().getUser(player).getActiveAbilities().add(ability);
			if (ability.getClickedItem() != null) {
				inv.addItem(ability.getClickedItem());
			}
		}
		
		Specialty specialty = user.getSpecialty();
		if(specialty.equals(Specialty.CHEMIST) || user.getSpecialty().equals(Specialty.SOUPER)){
			do {
				if(specialty.equals(Specialty.SOUPER))
					inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
				else
					inv.addItem(new ItemStack(Material.POTION, 1, (short) 16421));
			} while (!(inv.firstEmpty() < 0));
		}else{
			if(specialty.equals(Specialty.COOK))
				inv.addItem(new ItemStack(Material.COOKIE, 40, (byte)0));
			if(specialty.equals(Specialty.BRUTE))
				player.setMaxHealth(200);
		}
		Core.getInstance().getUserManager().getUser(player).setSafe(false);
		Core.getInstance().getUserManager().getUser(player).setActiveLoadout(this);
	}

	public int getPointValue() {
		int value = 0;
		for (Ability ability : this.abilities) {
			value += ability.getPoints();
		}
		return value;
	}

	public int getMaxPoints() {
		return this.maxPoints;
	}

	public String toDBString() {
		String s = this.name + "~";
		for (Ability ability : this.abilities) {
			s += ability.getName() + "~";
		}
		return s;
	}
	
	private void cleanupList() {
		boolean runAgain = false;
		for (Ability ability : this.abilities) {
			int index = abilities.lastIndexOf(ability);
			if (index - 1 != -1) {
				if (Core.getInstance().getUnlockableManager().isMoreRare(abilities.get(index - 1), ability)) {
					abilities.set(index, abilities.get(index - 1));
					abilities.set(index - 1, ability);
					runAgain = true;
				}
			}
		}
		if (runAgain)
			cleanupList();
	}

	public ItemStack getSelectableIcon() {
		this.cleanupList();
		ArrayList<String> lore = new ArrayList<>();
		lore.addAll(Arrays.asList("",
				ChatColor.GRAY + "Points: " + ChatColor.YELLOW + this.getPointValue() + "/" + this.getMaxPoints(),
				ChatColor.GRAY + "Active abilities:"));
		for (Ability ability : this.abilities) {
			lore.add(ChatColor.GRAY + " - " + ability.getScarcity().getColor() + ability.getName());
		}
		return Core.getInstance().getItemManager().createItem(ChatColor.WHITE + this.getName(), Material.PISTON_BASE,
				(byte) 0, 1, lore);
	}

	public void openEditScreen(Player player) {
		Inventory es = Bukkit.getServer().createInventory(player, 54,
				"Edit kit: " + this.getPointValue() + "/" + this.getMaxPoints() + " " + this.getName());
		for (int i = 0; i < this.abilities.size(); i++) {
			ItemStack item = this.abilities.get(i).getIcon();
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			lore.add("");
			lore.add(ChatColor.AQUA + "Points: " + abilities.get(i).getPoints());
			lore.add(ChatColor.GREEN + "Activated");
			im.setLore(lore);
			item.setItemMeta(im);

			es.setItem(i, item);

		}
		for (int i = 0; i < 18; i++)
			if (es.getItem(i) == null)
				es.setItem(i, Core.getInstance().getItemManager().createItem(" ", Material.STAINED_GLASS_PANE,
						(byte) 15, 1, null));
		for (Ability ability : Core.getInstance().getUserManager().getUser(player).getOwnedAbilities())
			if (!this.abilities.contains(ability)){
				ItemStack icon = ability.getIcon();
				ItemMeta im = icon.getItemMeta();
				List<String> lore = im.getLore();
				lore.addAll(Arrays.asList("", ChatColor.AQUA + "Points: " + ability.getPoints()));
				im.setLore(lore);
				icon.setItemMeta(im);
				es.addItem(icon);
			}
		player.openInventory(es);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addAbility(Ability ability) {
		if (!this.abilities.contains(ability))
			this.abilities.add(ability);
	}

	public void removeAbility(Ability ability) {
		if (this.abilities.contains(ability))
			this.abilities.remove(ability);
	}

	public void refreshEditScreen(Player player) {
		player.closeInventory();
		this.openEditScreen(player);
	}

	public void resetLoadout() {
		this.abilities.clear();
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Ability> getAbilities() {
		return this.abilities;
	}

}
