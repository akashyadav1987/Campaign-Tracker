package com.pulp.campaigntracker.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.widget.Toast;

import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.PromoterActivityFinish;
import com.pulp.campaigntracker.listeners.CampaignDetailsRecieved;
import com.pulp.campaigntracker.ui.CampaignDetailsActivity;
import com.pulp.campaigntracker.ui.SupervisorMotherActivity;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonGetCampaignDetails {

	private final String TAG = JsonGetCampaignDetails.class.getSimpleName();
	private CampaignDetailsRecieved listener;
	private PromoterActivityFinish promoterFinishListner;

	private String url;

	// JSON Response node names
	private final String KEY_ID = "id";
	private final String KEY_CODE = "code";
	private final String KEY_IS_ACTIVE = "isActive";
	private final String KEY_COMPANY = "company";
	private final String KEY_PHONE = "phone";
	private final String KEY_NAME = "name";
	private final String KEY_ADDRESS = "address";
	private final String KEY_REGISTERED = "registered";
	private final String KEY_CAMPAIGN_LIST = "campaign_list";
	private final String KEY_CAMPAIGN_DETAILS = "campaign_details";
	private final String KEY_STORE_LIST = "store_list";
	private final String KEY_USER_LIST = "user_list";
	private final String KEY_IMMEDIATE_MANAGER_DETAILS = "immediate_manager_details";
	private final String KEY_CITY = "city";
	private final String KEY_STATE = "state";
	private final String KEY_REGION = "region";
	private final String KEY_AGENT = "agent";
	private final String KEY_CAEGORY = "category";
	private final String KEY_STORE_ID = "store_id";
	private final String KEY_ROLE = "role";
	private final String KEY_CONTACT_NO = "contact_no";
	private final String KEY_GENDER = "gender";
	private ArrayList<CampaignDetails> mCampaignDetailsList;
	private final String KEY_PINCODE = "pincode";
	private final String KEY_LATITUDE = "lattitude";
	private final String KEY_LONGITUDE = "longitude";
	private final String KEY_FORM_LIST = "form_list";
	private final String KEY_FIELD_NAME = "field_name";
	private final String KEY_FIELD_TYPE = "field_type";
	private final String KEY_FIELD_LENGTH = "field_length";
	private final String KEY_IMAGE = "store_image";

	private CampaignDetails mCampaignDetails;

	private String KEY_CATEGORY = "category";
	private String KEY_EMAIL = "email";
	private boolean isList;
	private LoginType role;
	private Context mContext;
	GetJson getJson;
	private SupervisorMotherActivity mSupervisorMotherActivity = null;

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
		if (getJson != null)
			getJson.cancel(true);
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
	@SuppressLint("NewApi")
	public void getCampaignDetailsFromURL(String url,
			CampaignDetailsRecieved listener, String id, String AuthToken,
			ConstantUtils.LoginType role, Context mContext) {
		this.url = url;
		this.listener = listener;
		this.role = role;
		this.mContext = mContext;
		getJson = new GetJson();

		List<NameValuePair> promotorCampaginParams = new ArrayList<NameValuePair>();

		promotorCampaginParams.add(new BasicNameValuePair("user_id", id));
		promotorCampaginParams.add(new BasicNameValuePair("Auth_Token",
				AuthToken));
		promotorCampaginParams.add(new BasicNameValuePair("role", role
				.toString()));

		String promotorCampaginParamsString = (URLEncodedUtils.format(
				promotorCampaginParams, "utf-8")).toLowerCase();
		url += promotorCampaginParamsString;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		else
			getJson.execute(url);

	}

	private class GetJson extends AsyncTask<String, String, String> implements
			OnDismissListener {

		@Override
		protected String doInBackground(String... params) {

			JSONObject jCampaignObject = null;

			/*
			 * Check if the last cache time and current time is same. If the
			 * time stamp is same i.e. 1 day,get the data from cache. Otherwise
			 * clear the cache and get the data from the HTTP call.
			 */

			// if (UtilityMethods.getCurrentTimeStampInDays().equals(
			// mContext.getSharedPreferences(
			// ConstantUtils.CAMPAIGN_DETAILS_CACHE, 0).getString(
			// ConstantUtils.CACHED_TIME, ""))&&
			// !(UtilityMethods.isNetworkAvailable(mContext)))

			if (!(HTTPConnectionWrapper.isNetworkAvailable(mContext))) {
				try {
					jCampaignObject = new JSONObject(mContext
							.getSharedPreferences(
									ConstantUtils.CAMPAIGN_DETAILS_CACHE, 0)
							.getString(ConstantUtils.CACHED_DATA, ""));
					buildCampaignJson(jCampaignObject);
			
				} catch (Exception e) {
					killAsyncTask();
					UtilityMethods.ShowAlertDialog(mContext);
					
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
					mContext.getSharedPreferences(
							ConstantUtils.CAMPAIGN_DETAILS_CACHE, 0)
							.edit()
							.putString(ConstantUtils.CACHED_DATA,
									jCampaignObject.toString()).commit();
				} catch (Exception e) {
					UtilityMethods.ShowAlertDialog(mContext);
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (role == LoginType.promotor)
				listener.onCampaignDetailsRecieved(mCampaignDetails);
			else if (role == LoginType.supervisor)
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

			if (role == LoginType.promotor) {
				if (!jsonFullObject.isNull(KEY_CAMPAIGN_LIST)
						&& jsonFullObject.getJSONObject(KEY_CAMPAIGN_LIST) instanceof JSONObject) {
					isList = false;
					mCampaignDetails = new CampaignDetails();

					JSONObject jsonUserObject = jsonFullObject
							.getJSONObject(KEY_CAMPAIGN_LIST);

					int i = 1;
					String campaingTag = "c" + i;
					while (jsonUserObject.getJSONObject(campaingTag) != null) {
						jsonUserObject = jsonUserObject
								.getJSONObject(campaingTag);
						mCampaignDetails = getCampainObject(jsonUserObject);
						i++;
						campaingTag = "c" + i;
					}
					
				}

			}
			if (role == LoginType.supervisor) {
				if (!jsonFullObject.isNull(KEY_CAMPAIGN_LIST)
						&& jsonFullObject.getJSONObject(KEY_CAMPAIGN_LIST) instanceof JSONObject) {
					int j = 1;
					String campaingTag = "c" + j;

					JSONObject jsonArray = jsonFullObject
							.getJSONObject(KEY_CAMPAIGN_LIST);
					mCampaignDetails = null;
					mCampaignDetailsList = new ArrayList<CampaignDetails>();
					isList = true;
					for (int i = 0; i < jsonArray.length(); i++, j++) {
						campaingTag = "c" + j;
						JSONObject jsonObject = jsonArray
								.getJSONObject(campaingTag);
						CampaignDetails singleCampaignDetails = new CampaignDetails();
						singleCampaignDetails = getCampainObject(jsonObject);
						mCampaignDetailsList.add(singleCampaignDetails);
					}

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

			if (!jsonCampaignObject.isNull(KEY_CODE))
				mCampaignDetails
						.setCode(jsonCampaignObject.getString(KEY_CODE));

			if (!jsonCampaignObject.isNull(KEY_NAME))
				mCampaignDetails
						.setName(jsonCampaignObject.getString(KEY_NAME));

			if (!jsonCampaignObject.isNull(KEY_COMPANY))
				mCampaignDetails.setCompany(jsonCampaignObject
						.getString(KEY_COMPANY));

			if (!jsonCampaignObject.isNull(KEY_PHONE))
				mCampaignDetails.setContactNumber(jsonCampaignObject
						.getString(KEY_PHONE));

			if (!jsonCampaignObject.isNull(KEY_ADDRESS))
				mCampaignDetails.setAddress(jsonCampaignObject
						.getString(KEY_ADDRESS));

			if (!jsonCampaignObject.isNull(KEY_REGISTERED))
				mCampaignDetails.setRegistered(jsonCampaignObject
						.getString(KEY_REGISTERED));
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

				if (!jStoreObject.isNull(KEY_ID))
					storeDetails.setId(jStoreObject.getString(KEY_ID));

				if (!jStoreObject.isNull(KEY_CODE))
					storeDetails.setStoreCode(jStoreObject.getString(KEY_CODE));

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

		// Parse the User Details in a campaign
		if (!jsonObject.isNull(KEY_USER_LIST)
				&& jsonObject.getJSONArray(KEY_USER_LIST) instanceof JSONArray) {
			JSONArray jsonUserListArray = (JSONArray) jsonObject
					.getJSONArray(KEY_USER_LIST);
			List<UserProfile> mUserList = new ArrayList<UserProfile>();
			for (int i = 0; i < jsonUserListArray.length(); i++) {
				JSONObject jUserObject = jsonUserListArray.getJSONObject(i);
				UserProfile userDetails = new UserProfile();

				if (!jUserObject.isNull(KEY_ID))
					userDetails.setUid(jUserObject.getString(KEY_ID));

				if (!jUserObject.isNull(KEY_NAME))
					userDetails.setName(jUserObject.getString(KEY_NAME));

				if (!jUserObject.isNull(KEY_CONTACT_NO))
					userDetails.setContactNumber(jUserObject
							.getString(KEY_CONTACT_NO));

				if (!jUserObject.isNull(KEY_ADDRESS))
					userDetails.setAddress(jUserObject.getString(KEY_ADDRESS));

				if (!jUserObject.isNull(KEY_EMAIL))
					userDetails.setEmail(jUserObject.getString(KEY_EMAIL));

				if (!jUserObject.isNull(KEY_GENDER))
					userDetails.setGender(jUserObject.getString(KEY_GENDER));

				if (!jUserObject.isNull(KEY_STORE_ID))
					userDetails.setStoreId(jUserObject.getString(KEY_STORE_ID));

				if (!jUserObject.isNull(KEY_ROLE))
					userDetails.setRole(jUserObject.getString(KEY_ROLE));

				// if(!jUserObject.isNull(KEY_STORE_ID))
				// userDetails.setAddress(jUserObject.getString(KEY_STORE_ID));
				mUserList.add(userDetails);
			}
			mCampaignDetails.setUserList(mUserList);
		}
		if (!jsonObject.isNull(KEY_IMMEDIATE_MANAGER_DETAILS)
				&& jsonObject.getJSONObject(KEY_IMMEDIATE_MANAGER_DETAILS) instanceof JSONObject) {
			JSONObject jUserObject = jsonObject
					.getJSONObject(KEY_IMMEDIATE_MANAGER_DETAILS);
			UserProfile userDetails = new UserProfile();

			if (!jUserObject.isNull(KEY_ID))
				userDetails.setUid(jUserObject.getString(KEY_ID));

			if (!jUserObject.isNull(KEY_NAME))
				userDetails.setName(jUserObject.getString(KEY_NAME));

			if (!jUserObject.isNull(KEY_CONTACT_NO))
				userDetails.setContactNumber(jUserObject
						.getString(KEY_CONTACT_NO));

			if (!jUserObject.isNull(KEY_ADDRESS))
				userDetails.setAddress(jUserObject.getString(KEY_ADDRESS));

			if (!jUserObject.isNull(KEY_EMAIL))
				userDetails.setEmail(jUserObject.getString(KEY_EMAIL));

			if (!jUserObject.isNull(KEY_GENDER))
				userDetails.setGender(jUserObject.getString(KEY_GENDER));

			if (!jUserObject.isNull(KEY_ROLE))
				userDetails.setRole(jUserObject.getString(KEY_ROLE));

			mCampaignDetails.setImmediateManager(userDetails);
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
	public Object getCampaignDetails(LoginType role) {
		if (role == LoginType.promotor)
			return mCampaignDetails;
		else
			return mCampaignDetailsList;
	}

	public void noNetworkCache() {

	}

}
