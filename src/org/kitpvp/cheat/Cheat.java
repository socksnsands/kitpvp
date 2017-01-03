package org.kitpvp.cheat;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class Cheat {

	public enum Response {
		YES(true), NO(false), AMBIGUOUS(false);
		
		private boolean bool;
		
		Response(boolean bool){
			this.bool = bool;
		}
		
		public boolean toBoolean(){
			return bool;
		}
	}
	
	public enum ActionType {
		STAFF_REPORT, BAN;
	}
	
	private ArrayList<Condition> safe = new ArrayList<Condition>();
	
	private String id;
	private int bannable = -1, report = 1;
	
	public Cheat(String id){
		this.id = id;
	}
	
	protected void setBannable(int offenses){
		bannable = offenses;
	}
	
	protected void setStaffReport(int offenses){
		report = offenses;
	}
	
	public void addSafety(Condition c){
		this.safe.add(c);
	}
	
	public Response eval(Object... objects){
		return Response.AMBIGUOUS;
	}
	
	public String getId(){
		return id;
	}
	
	public ArrayList<Condition> getSafe(){
		return this.safe;
	}
	
	
	
	public int getOffensesFor(ActionType type){
		if(type.equals(ActionType.BAN)){
			return bannable;
		}else if(type.equals(ActionType.STAFF_REPORT)){
			return report;
		}
		return -1;
	}
	
	public boolean createsAction(ActionType type){
		return getOffensesFor(type) == -1 ? false : true;
	}
	
	public boolean isBannable(){
		return bannable == -1 ? false : true;
	}
	
}
