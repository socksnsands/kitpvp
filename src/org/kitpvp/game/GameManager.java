package org.kitpvp.game;

import org.kitpvp.game.Game.GameState;

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
		this.activeGame = null;
	}
	
	public boolean isGameActive(){
		if(this.activeGame != null){
			if(this.activeGame.getCurrentState().equals(GameState.OVER)){
				this.isGameActive = false;
				this.activeGame = null;
			}
		}
		return this.isGameActive;
	}
	
	public Game getActiveGame(){
		return this.activeGame;
	}
	
}
