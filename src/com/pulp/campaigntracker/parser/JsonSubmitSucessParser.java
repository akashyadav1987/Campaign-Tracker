package com.pulp.campaigntracker.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.pulp.campaigntracker.beans.ResponseData;
import com.pulp.campaigntracker.listeners.ResponseRecieved;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonSubmitSucessParser {

	private final String KEY_TAG = "response";
	private final String KEY_SUCCESS = "success";
	private final String KEY_ERROR = "error";
	private final String KEY_ERROR_MSG = "error_message";
	private String url;
	ResponseData mResponseData = new ResponseData();;
	private ResponseRecieved listener;
	private Context mContext;
	ArrayList<NameValuePair> formSubmitValues;

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("unchecked")
	public void submitFormToDb(ArrayList<NameValuePair> formSubmitValues,
			Context mContext) {
		this.mContext = mContext;
		this.formSubmitValues = formSubmitValues;
		GetJson getJson = new GetJson();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					formSubmitValues);
		else
			getJson.execute(formSubmitValues);

	}

	private class GetJson extends AsyncTask<List<NameValuePair>, Void, Void> {

		@Override
		protected Void doInBackground(List<NameValuePair>... params) {
			try {
				// JSONObject submitFormResponse = UtilityMethods.submitForm(
				// ConstantUtils.SUBMIT_FORM_URL, params[0]);
				// if (submitFormResponse != null)
				// buildResponseObject(submitFormResponse);

				buildResponseObject(UtilityMethods.AssetJSONFile(
						"jsonSubmitResponse", mContext));

			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * For Testing purpose replace with the above code
			 */

			return null;
		}

		private void buildResponseObject(JSONObject submitFormResponse) {

			try {
				if (submitFormResponse != null
						&& !submitFormResponse.isNull(KEY_TAG)) {
					JSONObject jResponseObject = submitFormResponse
							.getJSONObject(KEY_TAG);
					if (jResponseObject.isNull(KEY_SUCCESS)
							&& jResponseObject.getInt(KEY_SUCCESS) == 1) {
						mResponseData.setSuccess(true);
					}
					if (jResponseObject.isNull(KEY_ERROR)
							&& jResponseObject.getInt(KEY_ERROR) == 1) {
						mResponseData.setError(true);
						mResponseData.setErrorMessage(jResponseObject
								.getString(KEY_ERROR_MSG));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			listener.responseRecieved(mResponseData);
		}
	}

}
