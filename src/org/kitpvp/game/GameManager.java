package org.kitpvp.game;

public class GameManager {

	private Game activeGame;
	private boolean isGameActive = false;
	
	public GameManager(){
	}
	
	public void setActiveGame(Game game){
		this.activeGame = game;
		this.isGameActive = true;
	}
	
	public void endActiveGame(){
		this.isGameActive = false;
	}
	
	public boolean isGameActive(){
		return this.isGameActive;
	}
	
	public Game getActiveGame(){
		return this.activeGame;
	}
	
}
