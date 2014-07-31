package com.socar.socarvacancy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "login";
	private static final String TABLE_LOGIN = "logInSignOut";
	private static final String KEY_ID = "id";
	private static final String KEY_NUMBER = "number";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " TEXT)";
		db.execSQL(CREATE_LOGIN_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		onCreate(db);

	}

	public void addLogin(Login login) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, login.getID());
		values.put(KEY_NUMBER, login.getNumber());
		db.insert(TABLE_LOGIN, null, values);
		db.close();
	}

	public Login getLogin(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_LOGIN,
				new String[] { KEY_ID, KEY_NUMBER }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Login login = new Login(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1));
		return login;
	}

	public int updateLogin(Login login) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, login.getID());
		values.put(KEY_NUMBER, login.getNumber());
		return db.update(TABLE_LOGIN, values, KEY_ID + " = ?",
				new String[] { String.valueOf(login.getID()) });
	}

	public void deleteLogin(Login login) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOGIN, KEY_ID + " = ?",
				new String[] { String.valueOf(login.getID()) });
		db.close();
	}
}