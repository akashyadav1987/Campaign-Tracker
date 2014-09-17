package com.pulp.campaigntracker.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.LoginErrorData;
import com.pulp.campaigntracker.listeners.LoginDataRecieved;
import com.pulp.campaigntracker.ui.LoginActivity;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonLoginDataParser {

	private final String TAG = JsonLoginDataParser.class.getSimpleName();
	private LoginDataRecieved listener;
	private LoginData mLoginData;
	private LoginErrorData mLoginErrorData;

	private String url;


	// JSON Response node names
	private final String KEY_LOGIN = "login";
	private final String KEY_SUCCESS = "success";
	private final String KEY_ERROR = "error";
	private final String KEY_USER = "user";
	private final String KEY_ERROR_MSG = "error_message";
	private final String KEY_ID = "id";
	private final String KEY_NAME = "name";
	private final String KEY_EMAIL = "email";
	private final String KEY_NUMBER = "number";
	private final String KEY_AUTH_TOKEN = "auth_token";
	private final String KEY_ROLE = "role";

	private boolean isSucess;
	private Context mContext;
	// Single instance for Login

	/**
	 * 
	 * @param url
	 *            : String
	 * @param listener
	 *            : LoginDataRecieved Current context/activity to return the
	 *            object
	 * @param email
	 *            : String
	 * @param password
	 *            : String
	 * @param number
	 *            : String
	 * @param role
	 *            : Login type enum for promotor/supervisor
	 */
	@SuppressLint("NewApi")
	public void getLoginDataFromURL(String url, LoginDataRecieved listener,
			String email, String password, String number, LoginType role,String gcm_Token,String device_id) {
		this.url = url;
		this.listener = listener;
		GetJson getJson = new GetJson(); 

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "login"));
		params.add(new BasicNameValuePair("mobile", number));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("gcm_token", gcm_Token));	
		params.add(new BasicNameValuePair("device_id", device_id));

		// To execute the task on multiple(pool Of threads) background threads
		// in android 4.0
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		else
			getJson.execute(params);
	}

	private class GetJson extends AsyncTask<List<NameValuePair>, Void, Void> {

		@Override
		protected Void doInBackground(List<NameValuePair>... params) {

			JSONObject loginObject = UtilityMethods.getJSONFromUrl(
					ConstantUtils.LOGIN_URL, params[0]);
			
			if(loginObject!=null)
				buildLoginObject(loginObject);

			/*
			 * For Testing purpose replace with the above code
			 */
			

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isSucess)
				listener.onLoginDataRecieved(mLoginData);
			else
				listener.onLoginErrorDataRecieved(mLoginErrorData);
		}
	}

	/**
	 *  This is to build the json for login from the api.
	 * @param jsonObject
	 */
	private void buildLoginObject(JSONObject jsonObject) {

		try {
			if (jsonObject!=null && !jsonObject.isNull(KEY_LOGIN)) {
				
				JSONObject jLoginObject = jsonObject.getJSONObject(KEY_LOGIN);
				
				if (jLoginObject.getInt(KEY_SUCCESS) == 1) {
					isSucess = true;
					mLoginErrorData = null;
					mLoginData = LoginData.getInstance();

					// user successfully logged in
					if (!jLoginObject.isNull(KEY_USER)
							&& jLoginObject.get(KEY_USER) instanceof JSONObject) {
						JSONObject jUser =  jLoginObject.getJSONObject(KEY_USER);
						if (!jUser.isNull(KEY_NAME))
							mLoginData.setUsername(jUser.getString(KEY_NAME));

						if (!jUser.isNull(KEY_EMAIL))
							mLoginData.setEmail(jUser.getString(KEY_EMAIL));

						if (!jUser.isNull(KEY_NUMBER))
							mLoginData.setPhoneNo(jUser.getString(KEY_NUMBER));

						if (!jUser.isNull(KEY_ID))
							mLoginData.setId(jUser.getString(KEY_ID));

						if (!jUser.isNull(KEY_AUTH_TOKEN))
							mLoginData.setAuthToken(jUser.getString(KEY_AUTH_TOKEN));
						
						if (!jUser.isNull(KEY_ROLE))
							mLoginData.setRole(jUser.getString(KEY_ROLE));
						
						
					}
				} 
				else if (!jLoginObject.isNull(KEY_ERROR)) {
					isSucess = false;
					mLoginData = null;
					mLoginErrorData = new LoginErrorData();

					int error = jLoginObject.getInt(KEY_ERROR);
					if (!jLoginObject.isNull(KEY_ERROR_MSG)){
							mLoginErrorData.setMessage(jLoginObject.getString(KEY_ERROR_MSG));

					}

				}
			} else if (UtilityMethods
					.isNetworkAvailable((LoginActivity) listener)) {
				isSucess = false;
				mLoginData = null;
				mLoginErrorData = new LoginErrorData();
				mLoginErrorData.setMessage("No Internet Connection.");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
