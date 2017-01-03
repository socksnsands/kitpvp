package org.kitpvp.cheat;

import java.util.ArrayList;

import org.kitpvp.cheat.cheats.ReachCheatModule;

public class CheatManager {

	private ArrayList<Cheat> cheats = new ArrayList<Cheat>();
	
	public CheatManager(){
		registerCheats();
	}
	
	private void registerCheats(){
		this.cheats.add(new ReachCheatModule());
	}
	
	public ArrayList<Cheat> getCheats(){
		return cheats;
	}
	
	public Cheat getCheat(String id){
		for(Cheat cheat : getCheats()){
			if(cheat.getId().equalsIgnoreCase(id)){
				return cheat;
			}
		}
		return null;
	}
	
	public boolean isCheat(String id){
		for(Cheat cheat : getCheats()){
			if(cheat.getId().equalsIgnoreCase(id)){
				return true;
			}
		}
		return false;
	}
	
}
