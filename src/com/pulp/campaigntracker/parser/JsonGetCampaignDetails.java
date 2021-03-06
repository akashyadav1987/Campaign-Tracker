package com.pulp.campaigntracker.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;


import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.CampaignDetailsRecieved;

import com.pulp.campaigntracker.utils.ConstantUtils;

import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class JsonGetCampaignDetails {

	private final String TAG = JsonGetCampaignDetails.class.getSimpleName();
	private CampaignDetailsRecieved listener;
	// JSON Response node names
	private final String KEY_ID = "id";
	private final String KEY_COMPANY = "compDesc"; 
	private final String KEY_PHONE = "phone";
	private final String KEY_NAME = "name";
	private final String KEY_ADDRESS = "address";
	private final String KEY_CAMPAIGN_LIST = "campaign_list";
	private final String KEY_CAMPAIGN_DETAILS = "campaign_details";
	private final String KEY_STORE_LIST = "store_list";
	private final String KEY_CITY = "city";
	private final String KEY_STATE = "state";
	private final String KEY_REGION = "region";
	private final String KEY_AGENT = "agent";
	private ArrayList<CampaignDetails> mCampaignDetailsList;
	private final String KEY_PINCODE = "pincode";
	private final String KEY_LATITUDE = "lattitude";
	private final String KEY_LONGITUDE = "longitude";
	private final String KEY_FORM_LIST = "form_list";
	private final String KEY_FIELD_NAME = "field_name";
	private final String KEY_FIELD_TYPE = "field_type";
	private final String KEY_FIELD_LENGTH = "field_length";
	private final String KEY_IMAGE = "store_image";
	private String KEY_CATEGORY = "category";
	private Context mContext;
	GetJson getJson;
	// Single instance for Login
	public static JsonGetCampaignDetails instance;

	public static JsonGetCampaignDetails getInstance() {
		if (instance == null)
			instance = new JsonGetCampaignDetails();
		return instance;
	}

	private JsonGetCampaignDetails() {
	}

	public void killAsyncTask() {
		if (getJson != null) {

			mCampaignDetailsList = null;
			listener.onCampaignDetailsRecieved(mCampaignDetailsList);

			getJson.cancel(true);
		}
	}

	/**
	 * 
	 * @param url
	 *            : String
	 * @param listener
	 *            : CampaignDetailsRecieved Current context/activity to return
	 *            the object
	 * @param role
	 *            : Login type enum for promotor/supervisor
	 */

	public void getCampaignDetailsFromURL(String url,
			CampaignDetailsRecieved listener, String id, String AuthToken,
			String role, Context mContext) {
		this.listener = listener;
		this.mContext = mContext;
		getJson = new GetJson();

		// new api url

		url += "?user_id=" + id + "&auth_token=" + AuthToken + "&role="
				+ role;

		if (UtilityMethods.isHoneycombOrHigher())
			getJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		else
			getJson.execute(url);

	}

	private class GetJson extends AsyncTask<String, String, String> implements
			OnDismissListener {

		@Override
		protected String doInBackground(String... params) {

			JSONObject jCampaignObject = null;
			SharedPreferences pref= UtilityMethods.getAppPreferences(mContext);

			/*
			 * Check if the last cache time and current time is same. If the
			 * time stamp is same i.e. 1 day,get the data from cache. Otherwise
			 * clear the cache and get the data from the HTTP call.
			 */

			if (!(HTTPConnectionWrapper.isNetworkAvailable(mContext))) {
				try {
					jCampaignObject = new JSONObject(pref
								.getString(ConstantUtils.CACHED_DATA, ""));
					
					buildCampaignJson(jCampaignObject);

				} catch (Exception e) {
					killAsyncTask();
					

				}
			}
			/*
			 * Make the HTTP call and fetch the new data. Cache the data to the
			 * shared preference for the reference later.
			 */
			else {
				try {
					jCampaignObject = JsonResponseAdapter.campaignJsonResponse(
							params[0], mContext);
					buildCampaignJson(jCampaignObject);
					//buildCampaignJson(UtilityMethods.AssetJSONFile("jsonSupervisor",mContext));
					
					if (jCampaignObject == null) {				
						jCampaignObject = new JSONObject(
										pref
										.getString(ConstantUtils.CACHED_DATA,
												""));
						buildCampaignJson(jCampaignObject);
						
					} else {
								pref
								.edit()
								.putString(ConstantUtils.CACHED_DATA,
										jCampaignObject.toString()).commit();
					}
				} catch (Exception e) {
					
					killAsyncTask();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

				listener.onCampaignDetailsRecieved(mCampaignDetailsList);

		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			// TODO Auto-generated method stub
			this.cancel(true);

		}

	}

	/**
	 * Builds the campaign details/list from the json returned from the server.
	 * 
	 * @param jsonFullObject
	 */
	private void buildCampaignJson(JSONObject jsonFullObject) {

		try {

				if (!jsonFullObject.isNull(KEY_CAMPAIGN_LIST)){

					JSONArray jsonArray = jsonFullObject
							.getJSONArray(KEY_CAMPAIGN_LIST);
					
					mCampaignDetailsList = new ArrayList<CampaignDetails>();
					for (int i = 0; i < jsonArray.length(); i++) {
						
						JSONObject jsonObject = jsonArray
								.getJSONObject(i);
						CampaignDetails singleCampaignDetails = new CampaignDetails();
						singleCampaignDetails = getCampainObject(jsonObject);
						mCampaignDetailsList.add(singleCampaignDetails);
					}

				}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the campaign details for a particular campaign json.
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private CampaignDetails getCampainObject(JSONObject jsonObject)
			throws JSONException {

		CampaignDetails mCampaignDetails = new CampaignDetails();

		// Parse the campaign details Object
		if (!jsonObject.isNull(KEY_CAMPAIGN_DETAILS)
				&& jsonObject.getJSONObject(KEY_CAMPAIGN_DETAILS) instanceof JSONObject) {
			JSONObject jsonCampaignObject = (JSONObject) jsonObject
					.getJSONObject(KEY_CAMPAIGN_DETAILS);

			if (!jsonCampaignObject.isNull(KEY_ID))
				mCampaignDetails.setId(jsonCampaignObject.getString(KEY_ID));


			if (!jsonCampaignObject.isNull(KEY_NAME))
				mCampaignDetails
						.setName(jsonCampaignObject.getString(KEY_NAME));

			if (!jsonCampaignObject.isNull(KEY_COMPANY))
				mCampaignDetails.setCompany(jsonCampaignObject
						.getString(KEY_COMPANY));

		}



		// Parse the list of stores
		if (!jsonObject.isNull(KEY_STORE_LIST)
				&& jsonObject.getJSONArray(KEY_STORE_LIST) instanceof JSONArray) {
			JSONArray jsonStoreListArray = (JSONArray) jsonObject
					.getJSONArray(KEY_STORE_LIST);
			List<StoreDetails> mStoreList = new ArrayList<StoreDetails>();
			for (int i = 0; i < jsonStoreListArray.length(); i++) {
				JSONObject jStoreObject = jsonStoreListArray.getJSONObject(i);
				StoreDetails storeDetails = new StoreDetails();
				
				if (!jStoreObject.isNull(KEY_NAME))
					storeDetails.setName(jStoreObject.getString(KEY_NAME));

				if (!jStoreObject.isNull(KEY_PHONE))
					storeDetails
							.setContactNo(jStoreObject.getString(KEY_PHONE));

				if (!jStoreObject.isNull(KEY_ADDRESS))
					storeDetails
							.setAddress(jStoreObject.getString(KEY_ADDRESS));

				if (!jStoreObject.isNull(KEY_REGION))
					storeDetails.setRegion(jStoreObject.getString(KEY_REGION));

				if (!jStoreObject.isNull(KEY_CATEGORY))
					storeDetails.setStoreCategory(jStoreObject
							.getString(KEY_CATEGORY));

				if (!jStoreObject.isNull(KEY_CITY))
					storeDetails.setCity(jStoreObject.getString(KEY_CITY));

				if (!jStoreObject.isNull(KEY_STATE))
					storeDetails.setState(jStoreObject.getString(KEY_STATE));

				if (!jStoreObject.isNull(KEY_AGENT))
					storeDetails.setAgent(jStoreObject.getInt(KEY_AGENT));

				if (!jStoreObject.isNull(KEY_PINCODE))
					storeDetails
							.setPincode(jStoreObject.getString(KEY_PINCODE));

				if (!jStoreObject.isNull(KEY_LATITUDE))
					storeDetails.setLatitude(jStoreObject
							.getDouble(KEY_LATITUDE));

				if (!jStoreObject.isNull(KEY_LONGITUDE))
					storeDetails.setLongitude(jStoreObject
							.getDouble(KEY_LONGITUDE));

				if (!jStoreObject.isNull(KEY_IMAGE)) {

					String encodedImage = jStoreObject.getString(KEY_IMAGE);
					byte[] decodedString = Base64.decode(encodedImage,
							Base64.DEFAULT);
					Bitmap decodedByte = BitmapFactory.decodeByteArray(
							decodedString, 0, decodedString.length);
					storeDetails.setStoreImage(decodedByte);
				}

				mStoreList.add(storeDetails);
			}
			mCampaignDetails.setStoreList(mStoreList);
		}

		
		if (!jsonObject.isNull(KEY_FORM_LIST)
				&& jsonObject.getJSONArray(KEY_FORM_LIST) instanceof JSONArray) {
			JSONArray jMessageList = jsonObject.getJSONArray(KEY_FORM_LIST);
			TLog.v(TAG, "KEY_FORM_LIST" + jMessageList);
			List<UserFormDetails> userFormDetailsList = new ArrayList<UserFormDetails>();
			for (int i = 0; i < jMessageList.length(); i++) {
				JSONObject jMessage = jMessageList.getJSONObject(i);
				UserFormDetails userFormDetails = new UserFormDetails();

				if (!jMessage.isNull(KEY_FIELD_NAME))
					userFormDetails.setFieldName(jMessage
							.getString(KEY_FIELD_NAME));

				if (!jMessage.isNull(KEY_FIELD_TYPE))
					userFormDetails.setFieldType(jMessage
							.getString(KEY_FIELD_TYPE));

				if (!jMessage.isNull(KEY_FIELD_LENGTH))
					userFormDetails.setFieldLength(jMessage
							.getString(KEY_FIELD_LENGTH));

				userFormDetailsList.add(userFormDetails);

				userFormDetails = null;
				jMessage = null;
			}
			jMessageList = null;
			mCampaignDetails.setUserFormDetailsList(userFormDetailsList);
		}

		return mCampaignDetails;
	}

	/**
	 * Takes the role and returns the List/Object accordingly
	 * 
	 * @param role
	 * @return : Campaign Details/List
	 * 
	 */
	public Object getCampaignDetails() {
	
			return mCampaignDetailsList;
	}

	public void noNetworkCache() {

	}

	

}
