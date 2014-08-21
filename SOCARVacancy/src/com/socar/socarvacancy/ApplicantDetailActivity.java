package com.socar.socarvacancy;

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
	DatabaseHandler db = new DatabaseHandler(this);
	TextView name;
	TextView surname;
	TextView faname;
	TextView email;
	TextView sex;
	Spinner appStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applicant_detail);
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			vacNumber = extras.getInt("vacID");
			appName = extras.getString("appName");
			appNumber = extras.getInt("appID");
			appSurname = extras.getString("appSurname");
			appFaname = extras.getString("appFaname");
			appEmail = extras.getString("appEmail");
			appSex = extras.getString("appSex");
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
		default:
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