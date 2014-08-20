package com.socar.socarvacancy;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	DatabaseHandler db = new DatabaseHandler(this);
	Button loginButton;
	EditText loginName;
	EditText password;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginName = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Run AsyncTask
				AsyncTaskWS newTask = new AsyncTaskWS(LoginActivity.this,
						"getLoginPassword", getApplicationContext(), loginName
								.getText().toString(), password.getText()
								.toString());

				newTask.execute();

				try {
					// get first row
					Map<String, String> login = newTask.get().get(0);
					// get the value whose key is "successresult"
					String loginOK = login.get("SuccessResult");
					// if 1, write it to the database
					if (loginOK.equals("1")) {
						db.updateLogin(new Login(0, loginOK));
						// And start main activity
						Intent theIntent = new Intent(getApplication(),
								MainActivity.class);
						startActivity(theIntent);
						finish();
					} else {
						Toast.makeText(getBaseContext(),
								"Name or password is incorrect",
								Toast.LENGTH_LONG).show();
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});// click listener
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}
}