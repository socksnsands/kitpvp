package org.kitpvp.game;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kitpvp.core.Core;
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
	
	private ArrayList<Object> objects = new ArrayList<Object>();
	
	public Game(String name, int minPlayers, int maxPlayers){
		this.name = name;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
	}
	
	public void join(Player player){
		if(state.equals(WAITING) || state.equals(STARTING)){
			if(this.players.size() < this.maxPlayers){
				if(!this.players.contains(player)){
					this.players.add(player);
					player.sendMessage(ChatColor.GREEN + "You have joined " + name + "!");
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
		this.state = OVER;
		for(Player p : players){
			exit(p);
		}
	}
	
	public void lose(Player player){
		player.sendMessage(ChatColor.RED + "You lost " + name + "!");
		this.exit(player);
	}
	
	public void exit(Player player){
		if(this.players.contains(player)){
			this.players.remove(player);
		}
		Core.getInstance().getUserManager().getUser(player).giveSpawnInventory();
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
