package com.socar.socarvacancy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
/**
 * This class configures connection to the database, gets required method name,
 * parses JSONArray and returns ArrayList.
 *  
 * @constructor
 * AsyncTaskWS(Activity activity, String method, Context context)
 * 
 **/
public class AsyncTaskWS extends AsyncTask<String, String, ArrayList<String>>{

	//connection strings
	private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	private static final String TRANSPORT_CALL = "Vac/";
	private static final String SOAP_ADDRESS = "http://192.168.1.195:83/VacancyAndroid/Service1.asmx";
	
	private Activity activity;
	private String method;
	ProgressDialog pDialog;
	Context context;
	
	//constructor 
	public AsyncTaskWS(Activity activity, String method, Context context) {
		this.activity = activity;		
		this.method = method;
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		//Firstly, preExecute starts working
		super.onPreExecute();
		//Set up progress dialog during accessing details
		//Loading
	    pDialog = new ProgressDialog(activity);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}
	
	@Override
	protected void onPostExecute(ArrayList<String> result) {
		//After execution, dismiss dialog and toast the JSON
		super.onPostExecute(result);
		pDialog.dismiss();
	    
		//to prevent NullPointerException(which causes crash of program)
		if(result == null){
		  Toast.makeText(context, "Undefined error occured...", Toast.LENGTH_LONG)
			.show();
		}
		else{
         Toast.makeText(context, result.get(0), Toast.LENGTH_LONG)
				.show();
		}
	}
	
	@Override
	protected ArrayList<String> doInBackground(String... params) {
        //String vacancyId= params[0];		
		//Background execution
		String response = null;

		//check which method to run
		if(method == "GetVacancy")
		{
			ArrayList<String> name = new ArrayList<String>();
			//request details
			//standard java ksoap2 library
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
					method);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
			try {
				//Send request
				httpTransport.call(TRANSPORT_CALL + method, envelope);
				//Get response
				response = envelope.getResponse().toString();
				//parsing JSONArray
				JSONArray array = new JSONArray(response);
				for (int i = 0; i < array.length(); i++) {
				  JSONObject row = array.getJSONObject(i);
				  name.add(row.getString("name"));
				}//for
        	} catch (Exception e) {
				e.printStackTrace();
     		}//catch
    			return name;	
		}			
		return null;
	}//doInBackground
}//class AsyncTaskWS