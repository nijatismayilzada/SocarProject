package com.socar.socarvacancy;

public class Vacancy {

	int _vacId;
	String _vacName;
	String _vacNumber;
	public Vacancy(){
	}
	
	public Vacancy(int id, String name, String number)
	{
		this._vacId = id;
		this._vacName = name;
		this._vacNumber = number;
	}
	
	public Vacancy(String name, String number)
	{
		this._vacName = name;
		this._vacNumber = number;
	}
	
	public int getID(){
		return this._vacId;
	}
	
	public void setID(int id){
		this._vacId = id;
	}
	
	public String getName(){
		return this._vacName;
	}
	public void setName(String name){
		this._vacName = name;
	}
	public String getNumber(){
		return this._vacNumber;
	}
	public void setNumber(String number){
		this._vacNumber = number;
	}
}
