package org.kitpvp.cheat;

public class Check {

	private Object object, response;
	
	public Check(Object object, Object response){
		this.object = object;
		this.response = response;
	}
	
	public Object getObject(){
		return this.object;
	}
	
	public Object getResponse(){
		return this.response;
	}
	
	public String getObjectAsString(){
		return this.object instanceof String ? (String) this.object : "";
	}
	
	public boolean isObjectString(){
		return this.object instanceof String;
	}
	
	public boolean isReponseInt(){
		return this.object instanceof Integer;
	}
	
	public boolean isResponseBool(){
		return this.object instanceof Boolean;
	}
	
	public int getResponseAsInt(){
		return this.response instanceof Integer ? (int) this.object : 0;
	}
	
	public boolean getResponseAsBool(){
		return this.response instanceof Boolean ? (boolean) this.object : false;
	}
	
}
