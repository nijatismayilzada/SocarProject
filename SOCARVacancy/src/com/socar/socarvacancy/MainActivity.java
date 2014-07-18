package com.socar.socarvacancy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	ProgressDialog pDialog;
	Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        //Calling Aysnctask method for web service request
				//arguments: activity, method, context
				new AsyncTaskWS(MainActivity.this, "GetVacancy",getApplicationContext()).execute();;
			}
//          ArrayList array1 = new AsyncTaskWSS().execute().get(3000, TimeUnit.MILLISECONDS);
		});//click listener
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}//Main
