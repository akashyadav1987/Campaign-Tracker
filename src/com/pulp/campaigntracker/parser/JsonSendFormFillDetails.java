package com.pulp.campaigntracker.parser;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.internal.lj;
import com.pulp.campaigntracker.beans.LoginErrorData;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.dao.DataBaseHandler;
import com.pulp.campaigntracker.listeners.UpdateLocation;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class JsonSendFormFillDetails {
	private final String KEY_RESPONSE = "response";
	private final String KEY_ERROR = "error";
	private final String KEY_STATUS = "status";
	private boolean isFromBrodcast;
	private Context mContext;
	private boolean isSuccess;
	private LoginErrorData mLoginErrorData;
	private JSONObject formData;

	public void sendFormFillupDetails(Context mContext, JSONObject formDetails,
			boolean isFromBrodcast) {

		this.isFromBrodcast = isFromBrodcast;
		formData = formDetails;
		this.mContext =mContext;
		GetJson getJson = new GetJson();
		if (UtilityMethods.isHoneycombOrHigher())
			getJson.executeForHoneyComb(formDetails);
		else
			getJson.execute(formDetails);
	}

	private class GetJson extends AsyncTask<JSONObject, String, String> {

		private String status = "fail";
		private String error;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void executeForHoneyComb(JSONObject... params) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		}

		@Override
		protected String doInBackground(JSONObject... params) {

			JSONObject responseObject = JsonResponseAdapter.postJSONToUrl(
					ConstantUtils.SUBMIT_FORM, params[0]);
			if (responseObject != null) {
				buildCheckinObject(responseObject);
			} else {
				isSuccess = false;

				mLoginErrorData = new LoginErrorData();
				mLoginErrorData
						.setMessage("Please check your connection settings");
				DataBaseHandler dataBaseHandler = new DataBaseHandler(mContext);
				if (formData != null) {
					if (!isFromBrodcast)
						dataBaseHandler.addFormData(formData);
						// dataBaseHandler.addCheckin(formData);
						Log.i("added to database is:  ", formData + "");
				        Log.i("dataBaseHandler.getAllformdata()", dataBaseHandler.getAllFormData()+"");
					dataBaseHandler.close();
				}
			}

			/*
			 * For Testing purpose replace with the above code
			 */

			return status;
		}

		private void buildCheckinObject(JSONObject jsonObject) {

			try {
				if (jsonObject != null && !jsonObject.isNull(KEY_RESPONSE)) {

					JSONObject jCheckinObject = jsonObject
							.getJSONObject(KEY_RESPONSE);
					status = jCheckinObject.getString(KEY_STATUS);
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

	}

}
