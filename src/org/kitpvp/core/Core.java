package org.kitpvp.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitpvp.ability.AbilityManager;
import org.kitpvp.ability.abilities.objects.JetManager;
import org.kitpvp.ability.abilities.objects.TotemManager;
import org.kitpvp.commands.BalanceCommand;
import org.kitpvp.commands.PayCommand;
import org.kitpvp.commands.SetRankCommand;
import org.kitpvp.damage.DamageManager;
import org.kitpvp.loadout.LoadoutManager;
import org.kitpvp.unlockable.UnlockableManager;
import org.kitpvp.user.UserManager;
import org.kitpvp.util.ItemManager;

public class Core extends JavaPlugin implements Listener {

	private static Core instance;

	private Connection connection;
	private String conString = "jdbc:mysql://s21.hosthorde.com:3306/32694?user=32694&password=c6aea7813b&autoReconnect=true";
	private UnlockableManager unlockableManager;
	private AbilityManager abilityManager;
	private UserManager userManager;
	private TotemManager totemManager;
	private JetManager jetManager;
	private ItemManager itemManager;
	private DamageManager damageManager;

	public void onEnable() {
		instance = this;

		unlockableManager = new UnlockableManager();
		abilityManager = new AbilityManager();
		userManager = new UserManager();
		totemManager = new TotemManager();
		jetManager = new JetManager();
		itemManager = new ItemManager();
		damageManager = new DamageManager();
		
		if (!getConfig().contains("con.useConfigConnection")) {
			getConfig().set("con.useConfigConnection", true);
			saveConfig();
		}

		if (getConfig().getBoolean("con.useConfigConnection"))
		if (getConfig().contains("con.ip") && getConfig().contains("con.port")
				&& getConfig().contains("con.databaseName") && getConfig().contains("con.user")
				&& getConfig().contains("con.password"))
			conString = "jdbc:mysql://" + this.getConfig().getString("con.ip") + ":"
					+ this.getConfig().getString("con.port") + "/" + this.getConfig().getString("con.databaseName")
					+ "?user=" + this.getConfig().getString("con.user") + "&password="
					+ this.getConfig().getString("con.password") + "&autoReconnect=true";

		this.establishConnection();

		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new LoadoutManager(), this);

		registerCommands();
	}

	public Connection getConnection() {
		return this.connection;
	}

	private void establishConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(conString);
			this.connection.setAutoCommit(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void onPing(ServerListPingEvent event) {
		event.setMaxPlayers(100);
//		int currentAbilities = this.getAbilityManager().getAbilities().size();
//		int abilitiesToLaunch = 115;
//		double p = currentAbilities/abilitiesToLaunch;
//		p*=100;
//		DecimalFormat df = new DecimalFormat("#.##");
//		String percent = df.format(p);
		event.setMotd(ChatColor.RED + "kitpvp.org \n" + ChatColor.GOLD.toString() + ChatColor.MAGIC + "K" + ChatColor.GRAY + " " + "Public alpha testing!");
	}

	public void onDisable() {
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(!(entity instanceof Player) && !(entity instanceof ItemFrame)){
					entity.remove();
				}
			}
		}
		this.stopServer("Server restarting!");
	}

	public void stopServer(String reason) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.kickPlayer(ChatColor.RED + reason);
		}
		Bukkit.getServer().shutdown();
	}

	private void registerCommands() {
		getCommand("dev").setExecutor(new DeveloperCommand());
		getCommand("bal").setExecutor(new BalanceCommand());
		getCommand("setrank").setExecutor(new SetRankCommand());
		getCommand("pay").setExecutor(new PayCommand());
	}

	public DamageManager getDamageManager() {
		return damageManager;
	}

	public UnlockableManager getUnlockableManager() {
		return unlockableManager;
	}

	public AbilityManager getAbilityManager() {
		return this.abilityManager;
	}

	public UserManager getUserManager() {
		return this.userManager;
	}

	public TotemManager getTotemManager() {
		return this.totemManager;
	}

	public JetManager getJetManager() {
		return this.jetManager;
	}

	public ItemManager getItemManager() {
		return this.itemManager;
	}

	public static Core getInstance() {
		if (instance == null)
			System.out.println("Instance is null");
		return instance;
	}

}
