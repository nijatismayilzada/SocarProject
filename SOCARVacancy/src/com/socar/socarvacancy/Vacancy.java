//Constructors for Vacancy objects
package com.socar.socarvacancy;

public class Vacancy {

	// columns
	// id
	int _vacId;
	// Name of the vacancy
	String _vacName;
	// Code of the vacancy
	String _vacNumber;
	String _company;
	String _department;
	String _vacantCount;
	String _applicantCount;
	String _vacancyStatus;

	// Constructors
	public Vacancy() {
	}

	public Vacancy(int id, String name, String number, String company,
			String department, String vacantCount, String applicantCount,
			String vacancyStatus) {
		this._vacId = id;
		this._vacName = name;
		this._vacNumber = number;
		this._company = company;
		this._department = department;
		this._vacantCount = vacantCount;
		this._applicantCount = applicantCount;
		this._vacancyStatus = vacancyStatus;
	}

	public Vacancy(int id, String name, String number) {
		this._vacId = id;
		this._vacName = name;
		this._vacNumber = number;
	}

	public Vacancy(String name, String number) {
		this._vacName = name;
		this._vacNumber = number;
	}

	// get set

	public int getID() {
		return this._vacId;
	}

	public void setID(int id) {
		this._vacId = id;
	}

	public String getName() {
		return this._vacName;
	}

	public void setName(String name) {
		this._vacName = name;
	}

	public String getNumber() {
		return this._vacNumber;
	}

	public void setNumber(String number) {
		this._vacNumber = number;
	}

	public String getCompany() {
		return this._company;
	}

	public void setCompany(String company) {
		this._company = company;
	}

	public String getDepartment() {
		return this._department;
	}

	public void setDepartment(String department) {
		this._department = department;
	}

	public String getVacantCount() {
		return this._vacantCount;
	}

	public void setVacantCount(String vacantCount) {
		this._vacantCount = vacantCount;
	}

	public String getApplicantCount() {
		return this._applicantCount;
	}

	public void setApplicantCount(String applicantCount) {
		this._applicantCount = applicantCount;
	}

	public String getVacancyStatus() {
		return this._vacancyStatus;
	}

	public void setVacancyStatus(String vacancyStatus) {
		this._vacancyStatus = vacancyStatus;
	}
}
