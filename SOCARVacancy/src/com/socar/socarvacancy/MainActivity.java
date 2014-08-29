package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	DatabaseHandler db = new DatabaseHandler(this);
	List<Map<String, String>> vacancyList;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Check if the user logged in or not
		try {
			// Get first row and the number. If it is equal to 0, it means that
			// the user didn't login
			if (db.getLogin(0).getNumber().toString().equals("0")) {
				// So, start login activity for login again
				Intent theIntent = new Intent(getApplication(),
						LoginActivity.class);
				startActivity(theIntent);
				// destroy this activity
				finish();
			}
			// if you open the program for the first time, there is no database.
			// So the exception is thrown and catch clause works
		} catch (Exception e) {
			// add row to the database with 0
			db.addLogin(new Login(0, "0"));
			// start login activity
			Intent theIntent = new Intent(getApplication(), LoginActivity.class);
			startActivity(theIntent);
			// destroy main activity
			finish();
		}

		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		prepareListData();
		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		expListView.setAdapter(listAdapter);

		expListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemType = ExpandableListView.getPackedPositionType(id);

				int groupPosition;
				@SuppressWarnings("unused")
				int childPosition;
				if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
					childPosition = ExpandableListView
							.getPackedPositionChild(id);
					groupPosition = ExpandableListView
							.getPackedPositionGroup(id);

					// do your per-item callback here
					return true; // true if we consumed the click, false if not

				} else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
					groupPosition = ExpandableListView
							.getPackedPositionGroup(id);

					int vacancyID = Integer.parseInt(db.getVacancy(
							groupPosition).getNumber());

					// start login activity
					Intent theIntent = new Intent(getApplication(),
							ApplicantListActivity.class);
					theIntent.putExtra("vacID", vacancyID);
					startActivity(theIntent);
					return true; // true if we consumed the click, false if not
				} else {
					Toast.makeText(getBaseContext(), "null", Toast.LENGTH_LONG)
							.show();
					// null item; we don't consume the click
					return false;
				}
			}
		});

	}// onCreate

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		List<Vacancy> allVacancies = db.getAllVacancy();
		int count = allVacancies.size();
		if (count == 0) {
			remakeDB();
			allVacancies = db.getAllVacancy();
			count = allVacancies.size();
		}
		for (int i = 0; i < count; i++) {
			String header = allVacancies.get(i).getNumber() + " "
					+ allVacancies.get(i).getName();
			listDataHeader.add(header);
			List<String> children = new ArrayList<String>();
			children.add(allVacancies.get(i).getCompany());
			children.add(allVacancies.get(i).getDepartment());
			children.add(allVacancies.get(i).getVacantCount());
			children.add(allVacancies.get(i).getApplicantCount());
			children.add(allVacancies.get(i).getVacancyStatus());
			listDataChild.put(listDataHeader.get(i), children);
		}

	}

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

		if (item.getItemId() == R.id.action_logout) {
			logout();
			return true;
		} else if (item.getItemId() == R.id.action_refresh) {
			refresh();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		db.recrateTableVacancy();
		// AsyncTask. Connection between asp.net web services and the machine
		AsyncTaskWS newTask = new AsyncTaskWS(MainActivity.this,
				"getVacancyList", getApplicationContext());
		newTask.execute();

		try {
			// Get vacancy list from AsyncTask
			vacancyList = newTask.get();
			// the number of vacancies
			int noOfVacancies = vacancyList.size();
			// add all vacancies to the database
			for (int i = 0; i < noOfVacancies; i++) {
				// Get map from AsyncTask
				Map<String, String> vacancy = (Map<String, String>) vacancyList
						.get(i);
				// add it to the database
				db.addVacancy(new Vacancy(i, vacancy.get("number"), vacancy
						.get("vacancyName"), vacancy.get("companyName"),
						vacancy.get("departmentName"), vacancy
								.get("vacantCount"), vacancy
								.get("applicantCount"), vacancy.get("status")));
			}// for

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		Intent restart = new Intent(getApplication(), MainActivity.class);
		startActivity(restart);
		finish();
	}

	private void remakeDB() {
		db.recrateTableVacancy();
		// AsyncTask. Connection between asp.net web services and the machine
		AsyncTaskWS newTask = new AsyncTaskWS(MainActivity.this,
				"getVacancyList", getApplicationContext());
		newTask.execute();

		try {
			// Get vacancy list from AsyncTask
			vacancyList = newTask.get();
			// the number of vacancies
			int noOfVacancies = vacancyList.size();
			// add all vacancies to the database
			for (int i = 0; i < noOfVacancies; i++) {
				// Get map from AsyncTask
				Map<String, String> vacancy = (Map<String, String>) vacancyList
						.get(i);
				// add it to the database
				db.addVacancy(new Vacancy(i, vacancy.get("number"), vacancy
						.get("vacancyName"), vacancy.get("companyName"),
						vacancy.get("departmentName"), vacancy
								.get("vacantCount"), vacancy
								.get("applicantCount"), vacancy.get("status")));
			}// for

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	// logout
	public void logout() {
		// update column to 0
		db.updateLogin(new Login(0, "0"));
		// start login activity
		Intent theIntent = new Intent(getApplication(), LoginActivity.class);
		startActivity(theIntent);
		finish();
	}// logout

}// Main