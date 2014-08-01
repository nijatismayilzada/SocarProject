//Database handler for vacancy
package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerVacancies extends SQLiteOpenHelper {

	// SQLite details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "vacancies";
	private static final String TABLE_VACANCY = "vacancyList";
	// 3 columns
	// id of the column
	private static final String KEY_ID = "vacId";
	// Name of the vacancy
	private static final String KEY_NAME = "vacName";
	// Number if the vacancy
	private static final String KEY_NUMBER = "vacNumber";

	// Constructor
	public DatabaseHandlerVacancies(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Native methods of the SQLiteOpenHelper
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create table to store id, vacancy name and number
		String CREATE_VACANCY_TABLE = "CREATE TABLE " + TABLE_VACANCY + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_NUMBER + " TEXT" + ")";
		db.execSQL(CREATE_VACANCY_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACANCY);
		onCreate(db);

	}

	// Add vacancy to the database
	public void addVacancy(Vacancy vacancy) {
		// open database
		SQLiteDatabase db = this.getWritableDatabase();
		// add new values
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, vacancy.getName());
		values.put(KEY_NUMBER, vacancy.getNumber());
		db.insert(TABLE_VACANCY, null, values);
		db.close();
	}

	// Get vacancy
	public Vacancy getVacancy(int id) {
		// Open database
		SQLiteDatabase db = this.getReadableDatabase();
		// Android cursor for database reading
		Cursor cursor = db.query(TABLE_VACANCY, new String[] { KEY_ID,
				KEY_NAME, KEY_NUMBER }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
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
		values.put(KEY_NAME, vacancy.getName());
		values.put(KEY_NUMBER, vacancy.getNumber());
		// select the row and update
		return db.update(TABLE_VACANCY, values, KEY_ID + " = ?",
				new String[] { String.valueOf(vacancy.getID()) });
	}

	// delete vacancy
	public void deleteVacancy(Vacancy vacancy) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_VACANCY, KEY_ID + " = ?",
				new String[] { String.valueOf(vacancy.getID()) });
		db.close();
	}
}