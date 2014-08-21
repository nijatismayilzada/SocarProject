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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ApplicantListActivity extends Activity {

	DatabaseHandler db = new DatabaseHandler(this);
	List<Map<String, String>> applicantList;
	ExpandableListAdapterApplicant listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	int vacID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applicant_list);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			vacID = extras.getInt("vacID");
		}
		// Toast.makeText(getBaseContext(), "" + vacID,
		// Toast.LENGTH_LONG).show();

		expListView = (ExpandableListView) findViewById(R.id.lvExp1);
		prepareListData();
		listAdapter = new ExpandableListAdapterApplicant(this, listDataHeader,
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

					int applicantID = Integer.parseInt(db.getApplicant(
							groupPosition).getNumber());
					String appName = db.getApplicant(
							groupPosition).getName();
					String appSurname = db.getApplicant(
							groupPosition).getSurname();
					String appFaname = db.getApplicant(
							groupPosition).getFaname();
					String appEmail = db.getApplicant(
							groupPosition).getEmail();
					String appSex = db.getApplicant(
							groupPosition).getSex();

					// start login activity
					Intent theIntent = new Intent(getApplication(),
							ApplicantDetailActivity.class);
					theIntent.putExtra("vacID", vacID);
					theIntent.putExtra("appName", appName);
					theIntent.putExtra("appID", applicantID);
					theIntent.putExtra("appSurname", appSurname);
					theIntent.putExtra("appFaname", appFaname);
					theIntent.putExtra("appEmail", appEmail);
					theIntent.putExtra("appSex", appSex);
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
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		remakeDB();
		List<Applicant> allApplicants = db.getAllApplicant();
		int count = allApplicants.size();
		for (int i = 0; i < count; i++) {
			String header = allApplicants.get(i).getName() + " "
					+ allApplicants.get(i).getSurname();
			listDataHeader.add(header);
			List<String> children = new ArrayList<String>();
			children.add(allApplicants.get(i).getNumber());
			children.add(allApplicants.get(i).getFaname());
			children.add(allApplicants.get(i).getEmail());
			if (allApplicants.get(i).getSex().equals("0"))
				children.add("Male");
			else
				children.add("Female");
			listDataChild.put(listDataHeader.get(i), children);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.applicant_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
		// case R.id.action_settings:
		// return true;
		// logout
		case R.id.action_logout1:
			logout();
			return true;
		case R.id.action_refresh1:
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		db.recrateTableApplicant();
		// AsyncTask. Connection between asp.net web services and the machine
		AsyncTaskWS newTask = new AsyncTaskWS(ApplicantListActivity.this,
				"getApplicants", getApplicationContext(), vacID);
		newTask.execute();

		try {
			// Get vacancy list from AsyncTask
			applicantList = newTask.get();
			// the number of vacancies
			int noOfApplicants = applicantList.size();
			// add all vacancies to the database
			for (int i = 0; i < noOfApplicants; i++) {
				// Get map from AsyncTask
				Map<String, String> applicant = (Map<String, String>) applicantList
						.get(i);

				db.addApplicant(new Applicant(i, applicant.get("name"),
						applicant.get("number"), applicant.get("surname"),
						applicant.get("faname"), applicant.get("email"),
						applicant.get("sex")));
			}// for

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		Intent restart = new Intent(getApplication(),
				ApplicantListActivity.class);
		startActivity(restart);
		finish();
	}

	private void remakeDB() {
		db.recrateTableApplicant();
		// AsyncTask. Connection between asp.net web services and the machine
		AsyncTaskWS newTask = new AsyncTaskWS(ApplicantListActivity.this,
				"getApplicants", getApplicationContext(), vacID);
		newTask.execute();

		AsyncTaskWS newTask2 = new AsyncTaskWS(ApplicantListActivity.this,
				"getApplicantStatus", getApplicationContext(), vacID);
		newTask2.execute();

		try {
			// Get vacancy list from AsyncTask
			applicantList = newTask.get();
			// the number of vacancies
			int noOfApplicants = applicantList.size();
			// add all vacancies to the database
			for (int i = 0; i < noOfApplicants; i++) {
				// Get map from AsyncTask
				Map<String, String> applicant = (Map<String, String>) applicantList
						.get(i);

				db.addApplicant(new Applicant(i, applicant.get("name"),
						applicant.get("number"), applicant.get("surname"),
						applicant.get("faname"), applicant.get("email"),
						applicant.get("sex")));
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

}