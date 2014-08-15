package com.socar.socarvacancy;

public class Applicant 
{
	int _appID;
	String _appName;
	String _appSurname;
	String _appFaname;
	String _appEmail;
	String _appSex;
	
	public Applicant()
	{
	}
	
	public Applicant(int id, String name, String surname, String faname, String email, String sex)
	{
		this._appID = id;
		this._appName = name;
		this._appSurname = surname;
		this._appFaname = faname;
		this._appEmail = email;
		this._appSex = sex;
	}
	
	public int getID()
	{
		return this._appID;
	}
	
	public void setID(int id)
	{
		this._appID = id;
	}
	
	public String getName()
	{
		return this._appName;
	}
	
	public void setName(String name)
	{
		this._appName = name;
	}
	
	public String getSurname()
	{
		return this._appSurname;
	}
	
	public void setSurname(String surname)
	{
		this._appSurname = surname;
	}
	
	public String getFaname()
	{
		return this._appFaname;
	}
	
	public void setFaname(String faname)
	{
		this._appFaname = faname;
	}
	
	public String getEmail()
	{
		return this._appEmail;
	}
	
	public void setEmail(String email )
	{
		this._appEmail = email;
	}
	
	public String getSex()
	{
		return this._appSex;
	}
	
	public void setSex(String Sex)
	{
		this._appSex = Sex;
	}
	
	
}
