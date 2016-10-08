package org.kitpvp.loadout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
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
//		printLocation(l);
		return l;
	}
	
	private void printLocation(Location location){
		System.out.println(location.getWorld() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ());
	}
	
	public void apply(Player player) {
		player.closeInventory();
		//TODO change map
		player.teleport(randomMapLocation());
		player.sendMessage(ChatColor.GREEN + "This map is temporary!");
		Core.getInstance().getUserManager().getUser(player).resetInventory();
		Inventory inv = player.getInventory();
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.WOOD_SWORD));
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("Unbreakable", true);
		stack.setTag(tag);
		ItemStack is = CraftItemStack.asCraftMirror(stack);

		inv.setItem(0, is);
		Core.getInstance().getUserManager().getUser(player).getActiveAbilities().clear();
		Core.getInstance().getUserManager().getUser(player).clearCooldowns();
		for (Ability ability : this.abilities) {
			Core.getInstance().getUserManager().getUser(player).getActiveAbilities().add(ability);
			if (ability.getClickedItem() != null) {
				inv.addItem(ability.getClickedItem());
			}
		}
		do {
			inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
		} while (!(inv.firstEmpty() < 0));
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
