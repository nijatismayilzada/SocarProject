package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ApplicantListActivity extends Activity {
	
	
	DatabaseHandler db = new DatabaseHandler(this);
	List<Map<String, String>> applicantList;
	ExpandableListAdapterApplicant listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applicant_list);

		Bundle extras = getIntent().getExtras();
		int vacID = 0;

		if (extras != null) {
			vacID = extras.getInt("vacID");
		}
	//	Toast.makeText(getBaseContext(), "" + vacID, Toast.LENGTH_LONG).show();
		
		try {
			expListView = (ExpandableListView) findViewById(R.id.lvExp1);
			prepareListData();
			listAdapter = new ExpandableListAdapterApplicant(this, listDataHeader,
					listDataChild);
			expListView.setAdapter(listAdapter);
			
			
			
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Please, refresh",
					Toast.LENGTH_LONG).show();
		}
	}

	
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		List<Applicant> allApplicants = db.getAllApplicant();
		int count = allApplicants.size();
		for (int i = 0; i < count; i++) {
			String header = allApplicants.get(i).getName() + " "
					+ allApplicants.get(i).getSurname();
			listDataHeader.add(header);
			List<String> children = new ArrayList<String>();
			children.add(allApplicants.get(i).getFaname());
			children.add(allApplicants.get(i).getEmail());
			children.add(allApplicants.get(i).getSex());
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
		// Asynctask. Connection between asp.net web services and the machine
		AsyncTaskWS newTask = new AsyncTaskWS(ApplicantListActivity.this,
				"getApplicants", getApplicationContext());
		newTask.execute();

		try {
			// Get vacancy list from asynctask
			applicantList = newTask.get();
			// the number of vacancies
			int noOfApplicants = applicantList.size();
			// add all vacancies to the database
			for (int i = 0; i < noOfApplicants; i++) {
				// Get map from asynctask
				Map<String, String> applicant = (Map<String, String>) applicantList
						.get(i);
				db.addApplicant(new Applicant(i, applicant.get("applicantName"), applicant
						.get("applicantSurname"), applicant.get("applicantFaname"),
						applicant.get("applicantEmail"), applicant.get("applicantSex") ));
			}// for

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		Intent restart = new Intent(getApplication(), ApplicantListActivity.class);
		startActivity(restart);
		finish();
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
