package org.kitpvp.loadout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.user.User;
import org.kitpvp.util.AnvilGui;

public class LoadoutManager implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getType().equals(Material.ANVIL)) {
				if (Core.getInstance().getUserManager().getUser(event.getPlayer()).isSafe()) {
					event.setCancelled(true);
					Core.getInstance().getUserManager().getUser(event.getPlayer()).openKitEditor();
				}
			}
		}
	}
	
	private boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	private boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			User user = Core.getInstance().getUserManager().getUser(player);
			if (event.getInventory().getTitle().equals(ChatColor.UNDERLINE + "Kit Editor")) {
				event.setCancelled(true);
				if (event.getCurrentItem() != null) {
					if (event.getCurrentItem().getType().equals(Material.PISTON_BASE)) {
						String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
						if (user.hasLoadout(name)) {
							user.getLoadout(name).openEditScreen(player);
						}
					} else if (event.getCurrentItem().getType().equals(Material.ANVIL)) {
						player.sendMessage(ChatColor.GRAY + "Creating a new kit!");

						AnvilGui gui = new AnvilGui(player, new AnvilGui.AnvilClickEventHandler() {
							@Override
							public void onAnvilClick(AnvilGui.AnvilClickEvent event) {
								String name = "random";
								if (event.getSlot() == AnvilGui.AnvilSlot.OUTPUT) {
									event.setWillClose(true);
									event.setWillDestroy(true);

									String rName = event.getName();
									if(rName.length() > 17){
										player.sendMessage(ChatColor.RED + "Your kit name can only contain 17 characters (including color codes)!");
										rName = rName.substring(0, 17);
										player.sendMessage(ChatColor.YELLOW + "Kit name changed to: " + ChatColor.translateAlternateColorCodes('&', rName));
									}
									if(rName.equals("")){
										player.sendMessage(ChatColor.RED + "Your kit name can't be empty!");
										rName = "random";
									}
										
									name = ChatColor.translateAlternateColorCodes('&', rName);
								} else {
									event.setWillClose(false);
									event.setWillDestroy(false);
								}

								if (name.contains("~"))
									name = name.replaceAll("~", "-");
								if (name.contains("_"))
									name = name.replaceAll("_", "-");

								if (user.hasLoadout(name)) {
									name = "random";
									player.sendMessage(ChatColor.GRAY + "You already have a loadout called "
											+ user.getLoadout(name).getName() + ChatColor.GRAY + "!");
								}

								if (name.equals("random")) {
									Random random = new Random();
									name = "" + random.nextInt(1000000);
									player.sendMessage(ChatColor.GRAY + "Temporary name: \"" + ChatColor.GREEN + name
											+ ChatColor.GRAY + "\" chosen!");
								} else {
									player.sendMessage(ChatColor.GRAY + "Kit: \"" + ChatColor.GREEN + name
											+ ChatColor.GRAY + "\" created!");
								}
								HashMap<Ability, Material> map = new HashMap<Ability, Material>();
								Loadout loadout = new Loadout(name, map);
								user.addLoadout(loadout);

								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.getInstance(),
										new Runnable() {

											@Override
											public void run() {
												loadout.openEditScreen(player);
											}

										}, 3);

							}
						});
						gui.setSlot(AnvilGui.AnvilSlot.INPUT_LEFT, Core.getInstance().getItemManager()
								.createItem("Kit Name", Material.NAME_TAG, (byte) 0, 1, null));

						gui.open();

					}
				}
			}
			if (event.getInventory().getTitle().startsWith("Edit kit: ")) {
				event.setCancelled(true);
				String title = event.getInventory().getTitle();
				String loadoutName = ChatColor.stripColor(title.split("/30 ")[1]);
				if (!user.hasLoadout(loadoutName)) {
					player.sendMessage(ChatColor.DARK_RED + "Loadout not found! Please contact an admin!");
					player.closeInventory();
					return;
				}
				Loadout loadout = user.getLoadout(loadoutName);
				if (event.getCurrentItem().getItemMeta() == null)
					return;
				if (event.getCurrentItem() != null)
					if (event.getCurrentItem().getItemMeta() != null)
						if (event.getCurrentItem().getItemMeta().getLore() != null)
							if (event.getCurrentItem().getItemMeta().getLore()
									.contains(ChatColor.GREEN + "Activated")) {
								if(!event.getClick().equals(ClickType.RIGHT) && !event.getClick().equals(ClickType.SHIFT_RIGHT)){
									loadout.removeAbility(Core.getInstance().getAbilityManager().getAbility(
											ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())));
									loadout.refreshEditScreen(player);
								}else{
									AnvilGui gui = new AnvilGui(player, new AnvilGui.AnvilClickEventHandler() {
										@Override
										public void onAnvilClick(AnvilGui.AnvilClickEvent ev) {
											String id = "";
											Material mat = Material.AIR;
											if (ev.getSlot() == AnvilGui.AnvilSlot.OUTPUT) {
												ev.setWillClose(true);
												ev.setWillDestroy(true);

												id = ev.getName();
											} else {
												ev.setWillClose(false);
												ev.setWillDestroy(false);
											}

											if(isInteger(id)){
												int i =	Integer.parseInt(id);
												try{
													mat = Material.getMaterial(i);
												}catch(Exception ex){
													player.sendMessage(ChatColor.RED + "Material not found with ID " + id + "!");
												}
											}else{
												String s = id.replaceAll(" ", "_");
												s = s.toUpperCase();
												for(Material m : Material.values()){
													if(m.toString().equalsIgnoreCase(s)){
														mat = m;
														break;
													}
												}
											}
											Ability ability = Core.getInstance().getAbilityManager().getAbility(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
											if(!mat.equals(Material.AIR)){
												loadout.setIcon(ability, mat);
											}else{
												player.sendMessage(ChatColor.RED + "Item not found!");
											}

										}
									});
									gui.setSlot(AnvilGui.AnvilSlot.INPUT_LEFT, Core.getInstance().getItemManager()
											.createItem("Kit Name", Material.NAME_TAG, (byte) 0, 1, null));

									gui.open();
								}
							} else if (Core.getInstance().getAbilityManager().isAbility(
									ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))) {
								Ability t2a = Core.getInstance().getAbilityManager().getAbility(
										ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
								if (loadout.getPointValue() + t2a.getPoints() <= 30) {
									loadout.addAbility(t2a, t2a.hasClickedItem() ? t2a.getClickedItem().getType() : Material.AIR);
									loadout.refreshEditScreen(player);
								} else {
									player.sendMessage(
											ChatColor.RED + "This will make your loadout have too many points! ("
													+ (loadout.getPointValue() + t2a.getPoints()) + "/"
													+ loadout.getMaxPoints() + ")");
								}
							}
			}
		}
	}

}
