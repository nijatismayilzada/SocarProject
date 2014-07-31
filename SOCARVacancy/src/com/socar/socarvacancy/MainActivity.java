package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	DatabaseHandler db = new DatabaseHandler(this);
	DatabaseHandlerVacancies db2 = new DatabaseHandlerVacancies(this);
	ArrayList vacancyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			if (db.getLogin(0).getNumber().toString().equals("0")) {
				Intent theIntent = new Intent(getApplication(),
						LoginActivity.class);
				startActivity(theIntent);
				finish();
			}
		} catch (Exception e) {
			db.addLogin(new Login(0, "0"));
			Intent theIntent = new Intent(getApplication(), LoginActivity.class);
			startActivity(theIntent);
			finish();
		}

		AsyncTaskWS newTask = new AsyncTaskWS(MainActivity.this,
				"getVacancyList", getApplicationContext());
		newTask.execute();

		try {
			vacancyList = newTask.get();
			int noOfVacancies = vacancyList.size();
			for (int i = 0; i < noOfVacancies; i++) {
				Map<String, String> vacancy = newTask.get().get(i);
				db2.addVacancy(new Vacancy(vacancy.get("ID"), vacancy
						.get("name")));
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void logout() {
		db.updateLogin(new Login(0, "0"));
		Intent theIntent = new Intent(getApplication(), LoginActivity.class);
		startActivity(theIntent);
		finish();
	}

}// Main