package com.socar.socarvacancy;

public class Login {

	int _id;
	String _number;
	
	public Login(){
		
	}
	public Login(int id, String number){
		this._id = id;
		this._number = number;
	}
	public Login(String number){
		this._number = number;
	}
	public int getID(){
		return this._id;
	}
	public void setID(int id){
		this._id = id;
	}
	public String getNumber(){
		return this._number;
	}
	public void setNumber(String number){
		this._number = number;
	}
}
