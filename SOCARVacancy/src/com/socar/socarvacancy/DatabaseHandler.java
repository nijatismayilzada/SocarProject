//Database handler for Login
package com.socar.socarvacancy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// SQLite details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "login";
	private static final String TABLE_LOGIN = "logInSignOut";
	// 2 columns in the table
	private static final String KEY_ID = "id";
	private static final String KEY_NUMBER = "number";

	// Constructor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Native methods of the SQLiteOpenHelper
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create table to store id and number
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " TEXT)";
		// Run query
		db.execSQL(CREATE_LOGIN_TABLE);
	}// onCreate

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
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
		values.put(KEY_ID, login.getID());
		values.put(KEY_NUMBER, login.getNumber());
		db.insert(TABLE_LOGIN, null, values);
		db.close();
	}// addLogin

	// get Login detail
	// return login object
	public Login getLogin(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		// Android cursor object for reading database
		Cursor cursor = db.query(TABLE_LOGIN,
				new String[] { KEY_ID, KEY_NUMBER }, KEY_ID + "=?",
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
		values.put(KEY_ID, login.getID());
		values.put(KEY_NUMBER, login.getNumber());
		// Update
		return db.update(TABLE_LOGIN, values, KEY_ID + " = ?",
				new String[] { String.valueOf(login.getID()) });
	}// update Login

	// Delete login
	public void deleteLogin(Login login) {
		// Open database
		SQLiteDatabase db = this.getWritableDatabase();
		// delete query
		db.delete(TABLE_LOGIN, KEY_ID + " = ?",
				new String[] { String.valueOf(login.getID()) });
		db.close();
	}// deleteLogin
}// Database handler