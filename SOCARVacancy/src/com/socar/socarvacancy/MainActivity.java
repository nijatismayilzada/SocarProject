package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {

	//Connection strings
	private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	private static final String SOAP_METHOD = "GetVacancy";
	private static final String TRANSPORT_CALL = "Vac/GetVacancy";
	private static final String SOAP_ADDRESS = "http://192.168.1.195:83/VacancyAndroid/Service1.asmx";

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
				//Calling Asynctask method for web service request
				new GetJson().execute();
			}
		});//click listener
	}//onCreate

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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class GetJson extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			//Firstly, preExecute starts working
			super.onPreExecute();
			//Set up progress dialog during accessing details
			//Loading
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			//After execution, dismiss dialog and toast the json
			super.onPostExecute(result);
			pDialog.dismiss();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		protected String doInBackground(String... params) {

			//Background execution
			String response = null;
            ArrayList<String> name = new ArrayList<String>();
			//request details
			//standard java ksoap library
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
					SOAP_METHOD);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
			try {
				//Send request
				httpTransport.call(TRANSPORT_CALL, envelope);
				//Get response
				response = envelope.getResponse().toString();
				//parsing JSONArray
				JSONArray array = new JSONArray(response);
				for (int i = 0; i < array.length(); i++) {
				   JSONObject row = array.getJSONObject(i);
				   name.add(row.getString("name"));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return name.get(0) + " " + name.get(1);
		}//doInBackground
	}//GetJson

}//Main
