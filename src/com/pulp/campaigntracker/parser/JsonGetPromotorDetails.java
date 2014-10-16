package com.pulp.campaigntracker.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.pulp.campaigntracker.beans.FetchData;
import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.UserDetailsRecieved;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonGetPromotorDetails {

	private final String TAG = JsonGetPromotorDetails.class.getSimpleName();
	private UserDetailsRecieved listener;
	private String url;

	// JSON Response node names
	private final String KEY_ID = "id";
	private final String KEY_NAME = "name";
	private final String KEY_ADDRESS = "address";
	private final String KEY_USER_LIST = "user_list";
	private final String KEY_STORE_ID = "store_id";
	private final String KEY_ROLE = "role";
	private final String KEY_CONTACT_NO = "contact_no";
	private final String KEY_GENDER = "gender";
	private final String KEY_CAMPAGIN = "campagin";
	private final String KEY_STORE = "store";
	private String KEY_EMAIL = "email";

	private String KEY_STATUS = "status";
	private String KEY_CAMPAIGN_START_DATE = "campaign_start_date";
	private String KEY_CAMPAIGN_END_DATE = "campaign_end_date";
	private String KEY_STORE_START_DATE = "store_start_date";
	private String KEY_STORE_END_DATE = "store_end_date";

	private Context mContext;
	private ArrayList<UserProfile> userProfile;
	SinglePromotorData mSinglePromotorData;

	public JsonGetPromotorDetails() {
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
	public void getPromotorDetailsFromURL(String url,
			UserDetailsRecieved listener, Context mContext,
			String campagin_id, String store_id, int start_count, int fetchCount) {
		this.mContext = mContext;
		this.listener = listener;
		GetJson getJson = new GetJson();


		// new api url

		String auth = UtilityMethods.getAppPreferences(mContext).getString(
				ConstantUtils.AUTH_TOKEN, "");

		url += "?auth_token=" + auth + "&camp_id=" + campagin_id + "&store_id="
				+ store_id + "&start=" + String.valueOf(start_count) + "&num="
				+ String.valueOf(fetchCount) + "&role=" + "promoter";

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		else
			getJson.execute(url);

	}

	private class GetJson extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			JSONObject jPromoterObject = null;
			System.out.println("******************************************");
			System.out.println("********************params[0]::: "+params[0]);
			// If network is not available

			try {
				

				jPromoterObject = JsonResponseAdapter.campaignJsonResponse(
						params[0], mContext);

				buildFetchPromotorJson(jPromoterObject);

			} catch (Exception e) {
				Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
			}


			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			listener.onUserDetailsRecieved(mSinglePromotorData);

		}
	}

	/**
	 * Builds the campaign details/list from the json returned from the server.
	 * 
	 * @param jsonFullObject
	 */
	private void buildFetchPromotorJson(JSONObject jsonFullObject) {

		try {
			// userProfile = getUserList(jsonFullObject);

			if (jsonFullObject != null)
				getUserList(jsonFullObject);
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
	private void getUserList(JSONObject jsonObject) throws JSONException {
		mSinglePromotorData = new SinglePromotorData();
		// if (jsonObject != null && !jsonObject.isNull(KEY_FETCH)) { //Ritu
		// JSONObject jPromoterObject = jsonObject.getJSONObject(KEY_FETCH);

		// if (!jPromoterObject.isNull(KEY_TOTAL))
		// mSinglePromotorData.setTotal(jPromoterObject.getInt(KEY_TOTAL));
		// if (!jPromoterObject.isNull(KEY_LAST_COUNT))
		// mSinglePromotorData.setLast_count(jPromoterObject
		// .getInt(KEY_LAST_COUNT));
		// if (!jPromoterObject.isNull(KEY_START_COUNT))
		// mSinglePromotorData.setStart_count(jPromoterObject
		// .getInt(KEY_TOTAL));
		if (!jsonObject.isNull(KEY_USER_LIST)
				&& jsonObject.getJSONArray(KEY_USER_LIST) instanceof JSONArray) {
			// Parse the User Details in a campaign

			JSONArray jsonUserListArray = (JSONArray) jsonObject
					.getJSONArray(KEY_USER_LIST);
			userProfile = new ArrayList<UserProfile>();
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

				if (!jUserObject.isNull(KEY_CAMPAGIN))
					userDetails.setCurrentCampagin(jUserObject
							.getString(KEY_CAMPAGIN));

				if (!jUserObject.isNull(KEY_STORE))
					userDetails.setCurrentStore(jUserObject
							.getString(KEY_STORE));

				if (!jUserObject.isNull(KEY_ROLE))
					userDetails.setRole(jUserObject.getString(KEY_ROLE));

				if (!jUserObject.isNull(KEY_STATUS))
					userDetails.setStatus(jUserObject.getString(KEY_STATUS));
				if (!jUserObject.isNull(KEY_CAMPAIGN_START_DATE))
					userDetails.setCampaign_start_date(jUserObject
							.getString(KEY_CAMPAIGN_START_DATE));

				if (!jUserObject.isNull(KEY_CAMPAIGN_END_DATE))
					userDetails.setCampaign_end_date(jUserObject
							.getString(KEY_CAMPAIGN_END_DATE));

				if (!jUserObject.isNull(KEY_STORE_START_DATE))
					userDetails.setStore_start_date(jUserObject
							.getString(KEY_STORE_START_DATE));

				if (!jUserObject.isNull(KEY_STORE_END_DATE))
					userDetails.setStore_end_date(jUserObject
							.getString(KEY_STORE_END_DATE));

				// if(!jUserObject.isNull(KEY_STORE_ID))
				// userDetails.setAddress(jUserObject.getString(KEY_STORE_ID));
				userProfile.add(userDetails);
				mSinglePromotorData.setPersonalDetails(userProfile);
			}
		}
	}
	// return mSinglePromotorData;

}
