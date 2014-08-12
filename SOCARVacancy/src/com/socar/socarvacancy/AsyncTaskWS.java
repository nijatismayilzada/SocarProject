package com.socar.socarvacancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class configures connection to the database, gets required method name,
 * parses JSONArray and returns ArrayList.
 * 
 * @constructor AsyncTaskWS(Activity activity, String method, Context context)
 * 
 **/
public class AsyncTaskWS extends
		AsyncTask<String, String, ArrayList<Map<String, String>>> {

	// connection strings
	private static final String WSDL_TARGET_NAMESPACE = "Vac/";
	private static final String TRANSPORT_CALL = "Vac/";
	private static final String SOAP_ADDRESS = "http://10.23.14.94/VacancyAndroid/Service1.asmx";
	ArrayList<Map<String, String>> vacancyList = new ArrayList<Map<String, String>>();

	ArrayList<Map<String, String>> getApplicantsList = new ArrayList<Map<String, String>>();

	private Activity activity;
	private String method;
	private String value1;
	private String value2;
	private int vacID;
	private PropertyInfo pi1;
	ProgressDialog pDialog;
	private PropertyInfo pi2;
	Context context;

	// constructor
	public AsyncTaskWS(Activity activity, String method, Context context) {
		this.activity = activity;
		this.method = method;
		this.context = context;
	}

	public AsyncTaskWS(Activity activity, String method, Context context,
			String value1, String value2) {
		this.activity = activity;
		this.method = method;
		this.context = context;
		this.value1 = value1;
		this.value2 = value2;
	}

	public AsyncTaskWS(Activity activity, String method, Context context,
			int vacID) {
		this.activity = activity;
		this.method = method;
		this.context = context;
		this.vacID = vacID;
	}

	@Override
	protected void onPreExecute() {
		// Firstly, preExecute starts working
		super.onPreExecute();

	}

	@Override
	protected void onPostExecute(ArrayList<Map<String, String>> result) {
		// After execution, dismiss dialog and toast the JSON
		super.onPostExecute(result);
	}

	@Override
	protected ArrayList<Map<String, String>> doInBackground(String... params) {
		// String vacancyId= params[0];
		// Background execution
		String response = null;

		// check which method to run
		if (method == "getVacancyList") {

			// request details
			// standard java ksoap2 library
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, method);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
			try {
				// Send request
				httpTransport.call(TRANSPORT_CALL + method, envelope);
				// Get response
				response = envelope.getResponse().toString();

				// parsing JSONArray
				JSONArray array = new JSONArray(response);
				for (int i = 0; i < array.length(); i++) {
					Map<String, String> vacancy = new HashMap<String, String>();
					JSONObject row = array.getJSONObject(i);
					vacancy.put("number", row.getString("ID"));
					vacancy.put("vacancyName", row.getString("NAME"));
					vacancy.put("companyName", row.getString("NAME1"));
					vacancy.put("departmentName", row.getString("NAME2"));
					vacancy.put("vacantCount", row.getString("COUNT"));
					vacancy.put("applicantCount", row.getString("COUNT_INF"));
					vacancy.put("status", row.getString("status"));
					vacancyList.add(vacancy);
				}// for
			} catch (Exception e) {
				e.printStackTrace();
			}// catch
			return vacancyList;
		} else if (method == "getLoginPassword") {
			ArrayList<Map<String, String>> loginInfo = new ArrayList<Map<String, String>>();
			// request details
			// standard java ksoap2 library
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, method);
			pi1 = new PropertyInfo();

			pi1.setName("LoginNameUser");
			pi1.setValue(value1);
			pi1.setType(String.class);
			request.addProperty(pi1);

			pi2 = new PropertyInfo();
			pi2.setName("passwordUser");
			pi2.setValue(value2);
			pi2.setType(String.class);
			request.addProperty(pi2);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
			try {
				// Send request
				httpTransport.call(TRANSPORT_CALL + method, envelope);
				// Get response
				response = envelope.getResponse().toString();

				Map<String, String> login = new HashMap<String, String>();

				// parsing JSONArray
				JSONArray array = new JSONArray(response);
				for (int i = 0; i < array.length(); i++) {
					JSONObject row = array.getJSONObject(i);
					login.put("SuccessResult", row.getString("SuccessResult"));
					loginInfo.add(login);
				}// for
			} catch (Exception e) {
				e.printStackTrace();
			}// catch
			return loginInfo;
		}

		else if (method == "getApplicantStatus") {
			// request details
			// standard java ksoap2 library
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, method);

			ArrayList<Map<String, String>> applicantStatusList = new ArrayList<Map<String, String>>();
			pi1 = new PropertyInfo();

			pi1.setName("applicantID");
			pi1.setValue(value1);
			pi1.setType(String.class);
			request.addProperty(pi1);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
			try {
				// Send request
				httpTransport.call(TRANSPORT_CALL + method, envelope);
				// Get response
				response = envelope.getResponse().toString();

				// parsing JSONArray
				JSONArray array = new JSONArray(response);
				for (int i = 0; i < array.length(); i++) {
					JSONObject row = array.getJSONObject(i);
					Map<String, String> applicantStatusMap = new HashMap<String, String>();
					applicantStatusMap.put("name", row.getString("NAME"));
					applicantStatusMap.put("status", row.getString("status"));

					applicantStatusList.add(applicantStatusMap);
				}// for
			} catch (Exception e) {
				e.printStackTrace();
			}// catch
			return applicantStatusList;
		}

		else if (method == "getApplicants") {
			// request details
			// standard java ksoap2 library
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, method);
			pi1 = new PropertyInfo();

			pi1.setName("vacancyID");
			pi1.setValue(vacID);
			pi1.setType(String.class);
			request.addProperty(pi1);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
			try {
				// Send request
				httpTransport.call(TRANSPORT_CALL + method, envelope);
				// Get response
				response = envelope.getResponse().toString();

				// parsing JSONArray
				JSONArray array = new JSONArray(response);
				for (int i = 0; i < array.length(); i++) {
					Map<String, String> getApplicantsMap = new HashMap<String, String>();
					JSONObject row = array.getJSONObject(i);
					getApplicantsMap.put("id", row.getString("ID"));
					getApplicantsMap.put("name", row.getString("NAME"));
					getApplicantsMap.put("surname", row.getString("SURNAME"));
					getApplicantsMap.put("faname", row.getString("FANAME"));
					getApplicantsMap.put("sex", row.getString("SEX"));
					getApplicantsMap.put("email", row.getString("EMAIL"));
					getApplicantsMap.put("vacStatusID",
							row.getString("Vac_Status_ID"));
					getApplicantsList.add(getApplicantsMap);
				}// for
			} catch (Exception e) {
				e.printStackTrace();
			}// catch
			return getApplicantsList;
		}
		return null;
	}// doInBackground
}// class AsyncTaskWS