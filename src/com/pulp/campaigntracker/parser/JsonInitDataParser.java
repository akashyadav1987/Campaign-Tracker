package com.pulp.campaigntracker.parser;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.pulp.campaigntracker.beans.InitData;
import com.pulp.campaigntracker.listeners.InitializeApp;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonInitDataParser {


	private final String TAG = JsonInitDataParser.class.getSimpleName();
	private InitializeApp listener;

	private String url;

	// JSON Response node names
	private final String KEY_LOCATION_TIME_START = "location_time_start";
	private final String KEY_LOCATION_TIME_END = "location_time_end";
	private final String KEY_LOCATION_INTERVAL = "location_interval";
	private final String KEY_BATTERY_STATUS = "battery_status";
	private final String KEY_INIT_CONFIG = "init_config";
	private final String KEY_SYNC_INTERVAL = "sync_interval";



	private InitData mInitData =  InitData.getInstance();
	private Context mContext;



	/**
	 * 
	 * @param url : String
	 * @param listener :  CampaignDetailsRecieved Current context/activity to return the object
	 * @param role : Login type enum for promotor/supervisor
	 */
	@SuppressLint("NewApi")
	public void getInitDataFromURL(String url,InitializeApp listener,Context mContext)
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
				// initData();
				buildInitJson(UtilityMethods
						.AssetJSONFile("jsonInit", mContext));

			} catch (Exception e) {

			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			listener.onInitDataRecieved(mInitData);
		}
	}
	/**
	 * Builds the campaign details/list from the json returned from the server.
	 * @param jsonFullObject
	 */
	private void buildInitJson(JSONObject jsonFullObject) {

		try {

			if (!jsonFullObject.isNull(KEY_INIT_CONFIG)) {
				JSONObject jInitObject = jsonFullObject
						.getJSONObject(KEY_INIT_CONFIG);

				if (!jInitObject.isNull(KEY_BATTERY_STATUS))
					mInitData.setLocationBatteryStatus(jInitObject
							.getInt(KEY_BATTERY_STATUS));

				if (!jInitObject.isNull(KEY_LOCATION_INTERVAL))
					mInitData.setLocationPeriodicInterval(jInitObject
							.getInt(KEY_LOCATION_INTERVAL));

				if (!jInitObject.isNull(KEY_LOCATION_TIME_START))
					mInitData.setLocationStartInterval(jInitObject
							.getString(KEY_LOCATION_TIME_START));

				if (!jInitObject.isNull(KEY_LOCATION_TIME_END))
					mInitData.setLocationEndInterval(jInitObject
							.getString(KEY_LOCATION_TIME_END));

				if (!jInitObject.isNull(KEY_SYNC_INTERVAL))
					mInitData.setSyncUnsentDataInterval(jInitObject
							.getInt(KEY_SYNC_INTERVAL));

			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Builds the campaign details/list from the json returned from the server.
	 * @param jsonFullObject
	 */
	private void initData() {

				mInitData.setLocationPeriodicInterval(30);
				mInitData.setLocationStartInterval("0900");
				mInitData.setLocationEndInterval("1800");
				mInitData.setSyncUnsentDataInterval(30);

			}

	

}

