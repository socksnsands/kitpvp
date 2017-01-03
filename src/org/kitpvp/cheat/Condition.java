package org.kitpvp.cheat;

import java.util.ArrayList;

public class Condition {

	private ArrayList<Check> checks = new ArrayList<Check>();
	
	public Condition(ArrayList<Check> checks){
		this.checks = checks;
	}
	
	public ArrayList<Check> getChecks(){
		return checks;
	}
	
}
