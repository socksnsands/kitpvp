package org.kitpvp.core;

import java.sql.Connection;
import java.sql.DriverManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitpvp.ability.AbilityManager;
import org.kitpvp.ability.abilities.objects.JetManager;
import org.kitpvp.ability.abilities.objects.TotemManager;
import org.kitpvp.cheat.CheatManager;
import org.kitpvp.cheat.cheats.detection.PvpListener;
import org.kitpvp.commands.BalanceCommand;
import org.kitpvp.commands.ClearChatCommand;
import org.kitpvp.commands.FlyCommand;
import org.kitpvp.commands.InvseeCommand;
import org.kitpvp.commands.JoinCommand;
import org.kitpvp.commands.MessageCommand;
import org.kitpvp.commands.MuteChatCommand;
import org.kitpvp.commands.PayCommand;
import org.kitpvp.commands.PingCommand;
import org.kitpvp.commands.ReportCommand;
import org.kitpvp.commands.SetRankCommand;
import org.kitpvp.commands.VanishCommand;
import org.kitpvp.damage.DamageManager;
import org.kitpvp.game.GameManager;
import org.kitpvp.loadout.LoadoutManager;
import org.kitpvp.tradeup.TradeupManager;
import org.kitpvp.unlockable.UnlockableManager;
import org.kitpvp.user.UserManager;
import org.kitpvp.util.ItemManager;

public class Core extends JavaPlugin implements Listener {

	private static Core instance;

	private boolean debugMode = false;
	
	private Connection connection;
	private String conString = "jdbc:mysql://107.191.107.12/cust_31?user=cust_31&password=e676949eb0&autoReconnect=true";
	private UnlockableManager unlockableManager;
	private AbilityManager abilityManager;
	private UserManager userManager;
	private TotemManager totemManager;
	private JetManager jetManager;
	private ItemManager itemManager;
	private DamageManager damageManager;
	private CheatManager cheatManager;
	private GameManager gameManager;


	public void onEnable() {
		instance = this;

		unlockableManager = new UnlockableManager();
		abilityManager = new AbilityManager();
		userManager = new UserManager();
		totemManager = new TotemManager();
		jetManager = new JetManager();
		itemManager = new ItemManager();
		damageManager = new DamageManager();
		cheatManager = new CheatManager();
		gameManager = new GameManager();

		
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
		Bukkit.getServer().getPluginManager().registerEvents(new TradeupManager(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PvpListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new MuteChatCommand(), this);

		registerCommands();
	}
	
	public boolean getDebug(){
		return this.debugMode;
	}
	
	public void toggleDebug(){
		this.debugMode = !this.debugMode;
	}
	
	public boolean onWhitelist(){
		return Bukkit.getServer().hasWhitelist();
	}
	
	public void setWhitelistReason(String reason){
		getConfig().set("whitelist.reason", reason);
		saveConfig();
	}
	
	public String getWhitelistReason(){
		return getConfig().getString("whitelist.reason");
	}
	
	public void turnOnWhitelist(){
		Bukkit.getServer().setWhitelist(true);
	}

	public boolean hideAbilities(){
		return true;
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
	public void onChange(WeatherChangeEvent event){
//		event.getWorld().setStorm(false);
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		event.setMaxPlayers(100);
		event.setMotd(ChatColor.GOLD + "kitpvp.org \n" + ChatColor.GOLD.toString() + ChatColor.MAGIC + "K" + ChatColor.GRAY + " " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("motd")));
	}

	public void onDisable() {
		for(World world : Bukkit.getWorlds()){
			for(Entity entity : world.getEntities()){
				if(!(entity instanceof Player) && !(entity instanceof ItemFrame)){
					entity.remove();
				}
			}
		}
		this.stopServer("kitpvp.org restarting!");
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
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("message").setExecutor(new MessageCommand());
		getCommand("r").setExecutor(new MessageCommand());
		getCommand("stats").setExecutor(new BalanceCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
		getCommand("v").setExecutor(new VanishCommand());
		getCommand("join").setExecutor(new JoinCommand());
		getCommand("host").setExecutor(new JoinCommand());
		getCommand("cc").setExecutor(new ClearChatCommand());
		getCommand("mc").setExecutor(new MuteChatCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("ms").setExecutor(new PingCommand());
		getCommand("report").setExecutor(new ReportCommand());

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

	public CheatManager getCheatManager() {
		return this.cheatManager;
	}
	
	public GameManager getGameManager() {
		return this.gameManager;
	}

	public static Core getInstance() {
		if (instance == null)
			System.out.println("Instance is null");
		return instance;
	}

}
