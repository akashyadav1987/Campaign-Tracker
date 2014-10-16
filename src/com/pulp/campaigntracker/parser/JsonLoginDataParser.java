package com.pulp.campaigntracker.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.LoginErrorData;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.http.PulpHTTPTask;
import com.pulp.campaigntracker.listeners.LoginDataRecieved;
import com.pulp.campaigntracker.ui.LoginActivity;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonLoginDataParser {

	private final String TAG = JsonLoginDataParser.class.getSimpleName();
	private LoginDataRecieved listener;
	private LoginData mLoginData;
	private LoginErrorData mLoginErrorData;

	// JSON Response node names
	private final String KEY_LOGIN = "login";
	private final String KEY_SUCCESS = "success";
	private final String KEY_ERROR = "error";
	private final String KEY_USER = "users";
	private final String KEY_ERROR_MSG = "error_message";
	private final String KEY_ID = "id";
	private final String KEY_NAME = "name";
	private final String KEY_EMAIL = "email";
	private final String KEY_NUMBER = "number";
	private final String KEY_AUTH_TOKEN = "auth_token";
	private final String KEY_ROLE = "role";
	String userRole;
	private PulpHTTPTask pulpHTTPTask;

	private boolean isSuccess;

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

	@SuppressWarnings("unchecked")
	public void getLoginDataFromURL(String url, LoginDataRecieved listener,
			String email, String password, String mobile,
			String gcm_Token, String device_id) {
		this.listener = listener;
	
		GetJson getJson = new GetJson();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("gcm_token", gcm_Token));
		params.add(new BasicNameValuePair("device_id", device_id));

		

		if (UtilityMethods.isHoneycombOrHigher())
			getJson.executeForHoneyComb(params);
		else
			getJson.execute(params);
	}

	private class GetJson extends AsyncTask<List<NameValuePair>, Void, Void> {

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void executeForHoneyComb(List<NameValuePair>... params) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		}

		@Override
		protected Void doInBackground(List<NameValuePair>... params) {

			JSONObject loginObject = JsonResponseAdapter.getJSONFromUrl(
					ConstantUtils.LOGIN_URL, params[0]);

			if (loginObject != null)
			{
				mLoginData = LoginData.getInstance();
				mLoginData.setPhoneNo(params[0].get(0).toString());
				buildLoginObject(loginObject);
			}
			else {
				isSuccess = false;
				mLoginData = null;
				mLoginErrorData = new LoginErrorData();
				mLoginErrorData
						.setMessage("Please check your network settings");

			}



			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isSuccess)
				listener.onLoginDataRecieved(mLoginData);
			else
				listener.onLoginErrorDataRecieved(mLoginErrorData);
		}
	}

	/**
	 * This is to build the json for login from the api.
	 * 
	 * @param jsonObject
	 */
	private void buildLoginObject(JSONObject jsonObject) {

		try {
			if (jsonObject != null && !jsonObject.isNull(KEY_LOGIN)) {

				JSONObject jLoginObject = jsonObject.getJSONObject(KEY_LOGIN);

				if (jLoginObject.getInt(KEY_SUCCESS) == 200) {
					isSuccess = true;
					mLoginErrorData = null;
					mLoginData = LoginData.getInstance();

					if (!jLoginObject.isNull(KEY_USER)
							&& jLoginObject.getJSONArray(KEY_USER) instanceof JSONArray) {
						JSONArray jsonLoginArray = (JSONArray) jLoginObject
								.getJSONArray(KEY_USER);
						for (int i = 0; i < jsonLoginArray.length(); i++) {
							JSONObject jUser = jsonLoginArray.getJSONObject(i);

							if (!jUser.isNull(KEY_NAME))
								mLoginData.setUsername(jUser
										.getString(KEY_NAME));

						if (!jUser.isNull(KEY_EMAIL))
							mLoginData.setEmail(jUser.getString(KEY_EMAIL));

							if (!jUser.isNull(KEY_NUMBER))
								mLoginData.setPhoneNo(jUser
										.getString(KEY_NUMBER));

							if (!jUser.isNull(KEY_ID))
								mLoginData.setId(jUser.getString(KEY_ID));

							if (!jUser.isNull(KEY_AUTH_TOKEN))
								mLoginData.setAuthToken(jUser
										.getString(KEY_AUTH_TOKEN));

							if (!jUser.isNull(KEY_ROLE))
								mLoginData.setRole(jUser.getString(KEY_ROLE));

						}
					}
				} else if (!jLoginObject.isNull(KEY_ERROR)) {
					isSuccess = false;
					mLoginData = null;
					mLoginErrorData = new LoginErrorData();

					int error = jLoginObject.getInt(KEY_ERROR);
					if (!jLoginObject.isNull(KEY_ERROR_MSG)) {
						mLoginErrorData.setMessage(jLoginObject
								.getString(KEY_ERROR_MSG));

					}

				}
			} else if (HTTPConnectionWrapper
					.isNetworkAvailable((LoginActivity) listener)) {
				isSuccess = false;
				mLoginData = null;
				mLoginErrorData = new LoginErrorData();
				mLoginErrorData.setMessage("No Internet Connection.");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
