//Database handler for Login
package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// SQLite details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "vacancyAndroid";
	private static final String TABLE_LOGIN = "login";
	private static final String TABLE_VACANCY = "vacancy";
	// 2 columns in the table
	private static final String KEY_ID_LOGIN = "loginID";
	private static final String KEY_LOGIN = "loginDetail";
	//
	private static final String KEY_ID_VACANCY = "vacID";
	private static final String KEY_NAME_VACANCY = "vacName";
	private static final String KEY_NUMBER_VACANCY = "vacNumber";
	private static final String KEY_COMPANY_VACANCY = "companyName";
	private static final String KEY_DEPARTMENT_VACANCY = "departmentName";
	private static final String KEY_VACANTCOUNT_VACANCY = "count";
	private static final String KEY_APPLICANTCOUNT_VACANCY = "count_inf";
	private static final String KEY_STATUS_VACANCY = "status";

	//

	// Constructor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Native methods of the SQLiteOpenHelper
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create table to store id and number
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID_LOGIN + " INTEGER PRIMARY KEY," + KEY_LOGIN + " TEXT)";
		// Run query
		db.execSQL(CREATE_LOGIN_TABLE);

		String CREATE_VACANCY_TABLE = "CREATE TABLE " + TABLE_VACANCY + "("
				+ KEY_ID_VACANCY + " INTEGER PRIMARY KEY," + KEY_NAME_VACANCY
				+ " TEXT," + KEY_NUMBER_VACANCY + " TEXT,"
				+ KEY_COMPANY_VACANCY + " TEXT," + KEY_DEPARTMENT_VACANCY
				+ " TEXT," + KEY_VACANTCOUNT_VACANCY + " TEXT,"
				+ KEY_APPLICANTCOUNT_VACANCY + " TEXT," + KEY_STATUS_VACANCY
				+ " TEXT" + ")";
		db.execSQL(CREATE_VACANCY_TABLE);
	}// onCreate

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACANCY);
		// Create tables again
		onCreate(db);
	}// onUpgrade

	// Add login detail
	// 1 or 0
	public void addLogin(Login login) {
		// open database
		SQLiteDatabase db = this.getWritableDatabase();
		// Add new values
		ContentValues values = new ContentValues();
		values.put(KEY_ID_LOGIN, login.getID());
		values.put(KEY_LOGIN, login.getNumber());
		db.insert(TABLE_LOGIN, null, values);
		db.close();
	}// addLogin

	// get Login detail
	// return login object
	public Login getLogin(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		// Android cursor object for reading database
		Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_ID_LOGIN,
				KEY_LOGIN }, KEY_ID_LOGIN + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Login login = new Login(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1));
		return login;
	}// getLogin

	// Update login
	// Change 1 to 0, or 0 to 1
	public int updateLogin(Login login) {
		// Open database
		SQLiteDatabase db = this.getWritableDatabase();
		// Change values
		ContentValues values = new ContentValues();
		values.put(KEY_ID_LOGIN, login.getID());
		values.put(KEY_LOGIN, login.getNumber());
		// Update
		return db.update(TABLE_LOGIN, values, KEY_ID_LOGIN + " = ?",
				new String[] { String.valueOf(login.getID()) });
	}// update Login

	// Delete login
	public void deleteLogin(Login login) {
		// Open database
		SQLiteDatabase db = this.getWritableDatabase();
		// delete query
		db.delete(TABLE_LOGIN, KEY_ID_LOGIN + " = ?",
				new String[] { String.valueOf(login.getID()) });
		db.close();
	}// deleteLogin

	// =================================================================

	// Add vacancy to the database
	public void addVacancy(Vacancy vacancy) {
		// open database
		SQLiteDatabase db = this.getWritableDatabase();
		// add new values
		ContentValues values = new ContentValues();
		values.put(KEY_NAME_VACANCY, vacancy.getName());
		values.put(KEY_NUMBER_VACANCY, vacancy.getNumber());
		values.put(KEY_COMPANY_VACANCY, vacancy.getCompany());
		values.put(KEY_DEPARTMENT_VACANCY, vacancy.getDepartment());
		values.put(KEY_VACANTCOUNT_VACANCY, vacancy.getVacantCount());
		values.put(KEY_APPLICANTCOUNT_VACANCY, vacancy.getApplicantCount());
		values.put(KEY_STATUS_VACANCY, vacancy.getVacancyStatus());
		db.insert(TABLE_VACANCY, null, values);
		db.close();
	}

	// Get vacancy
	public Vacancy getVacancy(int id) {
		// Open database
		SQLiteDatabase db = this.getReadableDatabase();
		// Android cursor for database reading
		Cursor cursor = db.query(TABLE_VACANCY, new String[] { KEY_ID_VACANCY,
				KEY_NAME_VACANCY, KEY_NUMBER_VACANCY, KEY_COMPANY_VACANCY,
				KEY_DEPARTMENT_VACANCY, KEY_VACANTCOUNT_VACANCY,
				KEY_APPLICANTCOUNT_VACANCY, KEY_STATUS_VACANCY },
				KEY_ID_VACANCY + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		// Reading
		if (cursor != null)
			cursor.moveToFirst();

		Vacancy vacancy = new Vacancy(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		return vacancy;
	}// getVacancy

	// Get all vacancies
	public List<Vacancy> getAllVacancy() {
		// create list for storing vacancies
		List<Vacancy> vacancyList = new ArrayList<Vacancy>();
		String selectQuery = "SELECT * FROM " + TABLE_VACANCY;
		// Open database, run query and get all vacancies
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Read them and add to the list
		if (cursor.moveToFirst()) {
			do {
				Vacancy vacancy = new Vacancy();
				vacancy.setID(Integer.parseInt(cursor.getString(0)));
				vacancy.setName(cursor.getString(1));
				vacancy.setNumber(cursor.getString(2));
				vacancy.setCompany(cursor.getString(3));
				vacancy.setDepartment(cursor.getString(4));
				vacancy.setVacantCount(cursor.getString(5));
				vacancy.setApplicantCount(cursor.getString(6));
				vacancy.setVacancyStatus(cursor.getString(7));
				vacancyList.add(vacancy);
			} while (cursor.moveToNext());
		}
		return vacancyList;
	}

	// update vacancy
	public int updateVacancy(Vacancy vacancy) {
		// open database and read
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		// add updated values
		values.put(KEY_NAME_VACANCY, vacancy.getName());
		values.put(KEY_NUMBER_VACANCY, vacancy.getNumber());
		values.put(KEY_COMPANY_VACANCY, vacancy.getCompany());
		values.put(KEY_DEPARTMENT_VACANCY, vacancy.getDepartment());
		values.put(KEY_VACANTCOUNT_VACANCY, vacancy.getVacantCount());
		values.put(KEY_APPLICANTCOUNT_VACANCY, vacancy.getApplicantCount());
		values.put(KEY_STATUS_VACANCY, vacancy.getVacancyStatus());
		// select the row and update
		return db.update(TABLE_VACANCY, values, KEY_ID_VACANCY + " = ?",
				new String[] { String.valueOf(vacancy.getID()) });
	}

	// delete vacancy
	public void deleteVacancy(Vacancy vacancy) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_VACANCY, KEY_ID_VACANCY + " = ?",
				new String[] { String.valueOf(vacancy.getID()) });
		db.close();
	}

}// Database handler