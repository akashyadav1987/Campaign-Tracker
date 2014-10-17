package com.pulp.campaigntracker.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.pulp.campaigntracker.beans.LoginErrorData;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.utils.ParserKeysConstants;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonCheckinDataParser {

	private final String TAG = JsonCheckinDataParser.class.getSimpleName();
	private LoginErrorData mLoginErrorData;
	
	private Context mContext;
	@SuppressWarnings("unchecked")
	public void postCheckinDataToURL(Context mContext, String auth_token,
			String role, String encodedimage, String url, String id,
			String time, String campagin_id, String store_id) {
		// // this.listener = listener;
		// this.campagin_id = campagin_id;
		// this.store_id = store_id;
		// this.id = id;
		// this.time = time;
		this.mContext = mContext;
		GetJson getJson = new GetJson();

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		// params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("camp_id", campagin_id));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("user_id", id));
		params.add(new BasicNameValuePair("store_id", store_id));
		params.add(new BasicNameValuePair("auth_token", auth_token));
		params.add(new BasicNameValuePair("img", encodedimage));

		if (UtilityMethods.isHoneycombOrHigher())
			getJson.executeForHoneyComb(params);
		else
			getJson.execute(params);
	}

	private class GetJson extends AsyncTask<List<NameValuePair>, Void, Void> {

		private String status;
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void executeForHoneyComb(List<NameValuePair>... params) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		}

		@Override
		protected Void doInBackground(List<NameValuePair>... params) {

			JSONObject responseObject = JsonResponseAdapter.postJSONToUrl(
					ConstantUtils.CHECK_IN_URL, params[0]);

			if (responseObject != null)
				buildCheckinObject(responseObject);
			else {
				mLoginErrorData = new LoginErrorData();
				mLoginErrorData
						.setMessage("Please check your connection settings");

			}

			/*
			 * For Testing purpose replace with the above code
			 */

			return null;
		}

		private void buildCheckinObject(JSONObject jsonObject) {

			try {
				if (jsonObject != null && !jsonObject.isNull(ParserKeysConstants.KEY_RESPONSE)) {

					JSONObject jCheckinObject = jsonObject
							.getJSONObject(ParserKeysConstants.KEY_RESPONSE);
					status = jCheckinObject.getString(ParserKeysConstants.KEY_STATUS);
					// error = jCheckinObject.getString(KEY_ERROR);

				}
				// else if (HTTPConnectionWrapper
				// .isNetworkAvailable((LoginActivity) listener)) {
				// isSuccess = false;
				// //mCheckinDetails = null;
				// mLoginErrorData = new LoginErrorData();
				// mLoginErrorData.setMessage("No Internet Connection.");
				// }

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (status.equals("200"))

				Toast.makeText(mContext, "Image uploaded successfully",
						Toast.LENGTH_SHORT).show();
			else
				// listener.onLoginErrorDataRecieved(mLoginErrorData);
				Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
		}
	}
}