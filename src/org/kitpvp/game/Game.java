package org.kitpvp.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.kitpvp.ability.Ability;
import org.kitpvp.core.Core;
import org.kitpvp.loadout.Loadout;
import org.kitpvp.unlockable.Unlockable;
import org.kitpvp.unlockable.UnlockableSeries;
import org.kitpvp.user.User;
import org.kitpvp.user.rank.Rank;

import static org.kitpvp.game.Game.GameState.*; 

public abstract class Game {
	
	public enum GameState {
		WAITING,
		STARTING,
		PREGAME,
		GAME,
		OVER;
	}

	private String name;
	private int minPlayers, maxPlayers;
	
	private ArrayList<Player> players = new ArrayList<>();
	
	private GameState state = GameState.WAITING;
	private String winMessage = ChatColor.GRAY + "You won!";
	
	private ArrayList<Object> objects = new ArrayList<Object>();
	
	private Location spawnPoint;
	
	private int time = 0, run = 0;
	
	private final int START_TIME = 60;
	
	public Game(String name, Location spawn, int minPlayers, int maxPlayers){
		this.name = name;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.spawnPoint = spawn;
	}
	
	public void start(){
		this.run = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable(){
			@Override
			public void run() {
				second();
			}
		}, 20, 20);
		
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.GRAY + name + ChatColor.GRAY + " is being hosted, type " + ChatColor.GOLD + "/join" + ChatColor.GRAY + " to join!");
		Bukkit.broadcastMessage("");
		
		Core.getInstance().getGameManager().setActiveGame(this);
	}
	
	protected void setWinMessage(String message){
		this.winMessage = message;
	}
	
	private void sendGameMessage(String message){
		for(Player player : this.players){
			player.sendMessage(message);
		}
	}
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	private Loadout getDefault(){
		return new Loadout("Default", new HashMap<Ability, Material>());
	}
	
	private void second(){
		time++;
		if(this.state.equals(PREGAME)){
			if(this.time == 15){
				this.state = GAME;
				sendGameMessage(ChatColor.RED + "PVP has been enabled!");
				for(Player player : this.players){
					Core.getInstance().getUserManager().getUser(player).setSafe(false);
					if(!Core.getInstance().getUserManager().getUser(player).hasActiveLoadout()){
						player.sendMessage(ChatColor.GREEN + "Given default loadout!");
						getDefault().apply(player, false);
					}
				}
			}else{
				sendGameMessage(ChatColor.GRAY + "PVP will enable in " + ChatColor.GOLD + (15-time) + "s" + ChatColor.GRAY + "!");
			}
		}
		if(this.state.equals(STARTING)){
			if(time != START_TIME && time % 15 == 0){
				Bukkit.broadcastMessage(ChatColor.GRAY + name + ChatColor.GRAY + " is starting in " + ChatColor.GOLD + (START_TIME - time) + "s" + ChatColor.GRAY + "!");
			}else if(time == START_TIME){
				this.state = GameState.PREGAME;
				for(Player player : this.players){
					Core.getInstance().getUserManager().getUser(player).setSafe(true);
					player.teleport(this.spawnPoint);
					if(Core.getInstance().getUserManager().getUser(player).getLoadouts().size() > 0){
						Core.getInstance().getUserManager().getUser(player).openKitSelector();
					}else{
						player.sendMessage(ChatColor.GREEN + "Given default loadout!");
						getDefault().apply(player, false);
					}
				}
				time = 0;
			}
		}
	}
	
	public Location getSpawn(){
		return this.spawnPoint;
	}
	
	public GameState getCurrentState(){
		return this.state;
	}
	
	public void join(Player player){
		if(state.equals(WAITING) || state.equals(STARTING)){
			if(this.players.size() < this.maxPlayers){
				if(!this.players.contains(player)){
					this.players.add(player);
					player.sendMessage(ChatColor.GREEN + "You have joined " + name + "!");
					this.sendGameMessage(Core.getInstance().getUserManager().getUser(player).getRank().getColor() + player.getName() + ChatColor.GRAY + " has joined! (" + this.players.size() + "/" + this.maxPlayers + ")");
					if(this.players.size() >= this.minPlayers && this.state.equals(WAITING)){
						this.state = GameState.STARTING;
						this.time = 0;
						Bukkit.broadcastMessage(ChatColor.GRAY + name + ChatColor.GRAY + " is starting in " + ChatColor.GOLD + (START_TIME - time) + "s" + ChatColor.GRAY + "!");
					}
				}else{
					player.sendMessage(ChatColor.RED + "You are already in the game!");
				}
			}else{
				player.sendMessage(ChatColor.RED + "This game is full!");
			}
		}else{
			player.sendMessage(ChatColor.RED + name + " has already begun!");
		}
	}
	
	public void addReward(Object reward, int amount){
		for(int i = 0; i < amount; i++){
			this.objects.add(reward);
		}
	}
	
	public void giveReward(Player player){
		User user = Core.getInstance().getUserManager().getUser(player);
		for(Object object : this.objects){
			if(object instanceof Unlockable){
				user.addUnlockable((Unlockable) object);
			}
			if(object instanceof Rank){
				user.setRank((Rank) object);
			}
			if(object instanceof UnlockableSeries){
				user.addSeries((UnlockableSeries)object);
			}
		}
	}
	
	public void win(Player player){
		if(Bukkit.getScheduler().isCurrentlyRunning(run)){
			Bukkit.getScheduler().cancelTask(run);
		}
		this.state = OVER;
		player.sendMessage(this.winMessage);
		this.giveReward(player);
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Core.getInstance().getUserManager().getUser(player).getRank().getColor() + player.getName() + ChatColor.GRAY + " has won " + name + ChatColor.GRAY + "!");
		Bukkit.broadcastMessage("");
		ArrayList<Player> pls = new ArrayList<Player>();
		for(Player p : players){
			pls.add(p);
		}
		for(Player p : pls){
			this.exit(p);
		}
		Core.getInstance().getUserManager().getUser(player).giveSpawnInventory();
		Core.getInstance().getUserManager().getUser(player).setSafe(true);
		Core.getInstance().getGameManager().endActiveGame();
	}
	
	public void lose(Player player){
		if(this.state.equals(GAME)){
			player.sendMessage(ChatColor.RED + "You lost " + name + "!");
			this.exit(player);
		}else{
			if(this.state.equals(WAITING) || this.state.equals(STARTING)){
				this.sendGameMessage(Core.getInstance().getUserManager().getUser(player).getRank().getColor() + player.getName() + ChatColor.GRAY + " has left! (" + this.players.size() + "/" + this.maxPlayers + ")");
			}
			player.sendMessage(ChatColor.RED + "You have been removed from the game.");
			if(this.players.contains(player)){
				this.players.remove(player);
			}
		}
	}
	
	public void exit(Player player){
		if(this.players.contains(player)){
			this.players.remove(player);
		}
//		Core.getInstance().getUserManager().getUser(player).giveSpawnInventory();
		if(this.players.size() == 1){
			this.win(players.get(0));
		}
	}
	
	public String getName(){
		return name;
	}
	
	public int getMinPlayers(){
		return this.minPlayers;
	}
	
	public int getMaxPlayers(){
		return this.maxPlayers;
	}
	
}
