//Constructors for Login objects
package com.socar.socarvacancy;

public class Login {

	// id and number
	// id is always 0. Because we have just 1 row
	// Number is 0 or 1. 0 is logged out, 1 is logged in
	int _id;
	String _number;

	public Login() {
	}

	public Login(int id, String number) {
		this._id = id;
		this._number = number;
	}

	public Login(String number) {
		this._number = number;
	}

	// Method for getting id of the Login
	public int getID() {
		return this._id;
	}

	// Change the id of the Login
	public void setID(int id) {
		this._id = id;
	}

	// Get 0 or 1
	public String getNumber() {
		return this._number;
	}

	// Set 0 or 1
	public void setNumber(String number) {
		this._number = number;
	}
}
