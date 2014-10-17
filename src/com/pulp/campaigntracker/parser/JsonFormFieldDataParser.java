package com.pulp.campaigntracker.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.listeners.UserFormFieldRecieved;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;
import com.pulp.campaigntracker.utils.ParserKeysConstants;
;
public class JsonFormFieldDataParser {


	private final String TAG = JsonFormFieldDataParser.class.getSimpleName();
	private UserFormFieldRecieved listener;

	private String url;

	// JSON Response node names


	private Context mContext;
	private List<UserFormDetails> userFormDetailsList;

	/**
	 * 
	 * @param url
	 * @param listener
	 * @param mContext
	 */
	@SuppressLint("NewApi")
	public void getFormDetailsFromURL(String url,UserFormFieldRecieved listener,Context mContext)
	{
		this.url = url;
		this.listener = listener;
		this.mContext = mContext;
		GetJson getJson = new GetJson();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
		else
			getJson.execute();

	}
	private class GetJson extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) {

			try {
				buildFormListJson(UtilityMethods.AssetJSONFile("jsonFormList",mContext));


			} catch (IOException e) {
				TLog.v(TAG, "Exception" + e.toString());
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			listener.onFormDataRecieved(userFormDetailsList);

		}
	}
	/**
	 * Builds the campaign details/list from the json returned from the server.
	 * @param jsonFullObject
	 */
	private void buildFormListJson(JSONObject jsonFullObject) {

		try {

			if(!jsonFullObject.isNull(ParserKeysConstants.KEY_FORM_LIST)  && jsonFullObject.getJSONArray(ParserKeysConstants.KEY_FORM_LIST) instanceof JSONArray)
			{
				JSONArray jMessageList = jsonFullObject.getJSONArray(ParserKeysConstants.KEY_FORM_LIST);
				userFormDetailsList = new ArrayList<UserFormDetails>();
				for(int i=0;i<jMessageList.length();i++)
				{
					JSONObject jMessage = jMessageList.getJSONObject(i);
					userFormDetailsList.add(getFormObject(jMessage));
					jMessage = null;
				}
				jMessageList = null;
				jsonFullObject = null;
			}


		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the campaign details for a particular campaign json.
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private UserFormDetails getFormObject(JSONObject jsonObject) throws JSONException {

		UserFormDetails userFormDetails = new UserFormDetails();

		if(!jsonObject.isNull(ParserKeysConstants.KEY_FIELD_NAME))
			userFormDetails.setFieldName(jsonObject.getString(ParserKeysConstants.KEY_FIELD_NAME));

		if(!jsonObject.isNull(ParserKeysConstants.KEY_FIELD_TYPE))
			userFormDetails.setFieldType(jsonObject.getString(ParserKeysConstants.KEY_FIELD_TYPE));

		if(!jsonObject.isNull(ParserKeysConstants.KEY_FIELD_LENGTH))
			userFormDetails.setFieldLength(jsonObject.getString(ParserKeysConstants.KEY_FIELD_LENGTH));

		return userFormDetails;
	}
}

