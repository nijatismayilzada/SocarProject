package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerVacancies extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "vacancies";
	private static final String TABLE_VACANCY = "vacancyList";
	private static final String KEY_ID = "vacId";
	private static final String KEY_NAME = "vacName";
	private static final String KEY_NUMBER = "vacNumber";
	

	public DatabaseHandlerVacancies(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
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

	public void addVacancy(Vacancy vacancy) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, vacancy.getName());
		values.put(KEY_NUMBER, vacancy.getNumber());
		db.insert(TABLE_VACANCY, null, values);
		db.close();
	}

	public Vacancy getVacancy(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_VACANCY, new String[] { KEY_ID,
	            KEY_NAME, KEY_NUMBER }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Vacancy vacancy = new Vacancy(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		return vacancy;
	}
	
	public List<Vacancy> getAllVacancy(){
		List<Vacancy> vacancyList = new ArrayList<Vacancy>();
		String selectQuery = "SELECT * FROM " + TABLE_VACANCY;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do {
				Vacancy vacancy = new Vacancy();
				vacancy.setID(Integer.parseInt(cursor.getString(0)));
				vacancy.setName(cursor.getString(1));
				vacancy.setNumber(cursor.getString(2));
				vacancyList.add(vacancy);
			} while(cursor.moveToNext());
		}
		return vacancyList;
	}

	public int updateVacancy(Vacancy vacancy) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, vacancy.getName());
		values.put(KEY_NUMBER, vacancy.getNumber());
		return db.update(TABLE_VACANCY, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(vacancy.getID()) });
	}
	
	public void deleteVacancy(Vacancy vacancy){
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_VACANCY, KEY_ID + " = ?",
	            new String[] { String.valueOf(vacancy.getID()) });
	    db.close();
	}
}