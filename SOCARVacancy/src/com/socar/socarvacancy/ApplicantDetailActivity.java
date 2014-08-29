package com.socar.socarvacancy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

public class ApplicantDetailActivity extends Activity {

	int vacNumber;
	String appName;
	int appNumber;
	String appSurname;
	String appFaname;
	String appEmail;
	String appSex;
	String status;
	DatabaseHandler db = new DatabaseHandler(this);
	TextView name;
	TextView surname;
	TextView faname;
	TextView email;
	TextView sex;
	Spinner appStatus;
	List<Map<String, String>> applicantStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applicant_detail);
		Bundle extras = getIntent().getExtras();

		vacNumber = extras.getInt("vacancyID");
		appName = extras.getString("appName");
		appNumber = extras.getInt("appID");
		appSurname = extras.getString("appSurname");
		appFaname = extras.getString("appFaname");
		appEmail = extras.getString("appEmail");
		appSex = extras.getString("appSex");
		name = (TextView) findViewById(R.id.textViewName);
		surname = (TextView) findViewById(R.id.textViewSurname);
		faname = (TextView) findViewById(R.id.textViewFaname);
		email = (TextView) findViewById(R.id.textViewEmail);
		sex = (TextView) findViewById(R.id.textViewSex);
		appStatus = (Spinner) findViewById(R.id.statusSpinner);
		name.setText(appName);
		surname.setText(appSurname);
		email.setText(appEmail);
		if (appSex.equals("0")) {
			sex.setText("Male");
			faname.setText(appFaname + " oğlu");
		} else {
			sex.setText("Female");
			faname.setText(appFaname + " qızı");
		}
		AsyncTaskWS newTask = new AsyncTaskWS(ApplicantDetailActivity.this,
				"getApplicantStatus", getApplicationContext(), appNumber,
				vacNumber);
		newTask.execute();
		try {
			// Get vacancy list from AsyncTask
			applicantStatus = newTask.get();
			// the number of vacancies
			int noOfApplicants = applicantStatus.size();
			// add all vacancies to the database
			for (int i = 0; i < noOfApplicants; i++) {
				// Get map from AsyncTask
				Map<String, String> applicant = (Map<String, String>) applicantStatus
						.get(i);

				status = applicant.get("result");
			}// for
			//sex.setText(status);

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.applicant_detail, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logout1) {
			// logout();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
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