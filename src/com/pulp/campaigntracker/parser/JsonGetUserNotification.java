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

import com.pulp.campaigntracker.beans.UserNotification;
import com.pulp.campaigntracker.listeners.UserNotificationsRecieved;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonGetUserNotification {


	private final String TAG = JsonGetUserNotification.class.getSimpleName();
	private UserNotificationsRecieved listener;

	private String url;

	// JSON Response node names
	private final String KEY_MESSAGE_LIST = "message_list";
	private final String KEY_MESSAGE = "message";
	private final String KEY_TITLE = "title";

	private Context mContext;
	private List<UserNotification> notificationList;

	/**
	 * 
	 * @param url
	 * @param listener
	 * @param mContext
	 */
	@SuppressLint("NewApi")
	public void getCampaignDetailsFromURL(String url,UserNotificationsRecieved listener,Context mContext)
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
					buildMessageListJson(UtilityMethods.AssetJSONFile("jsonMessageList",mContext));


			} catch (IOException e) {
				TLog.v(TAG, "Exception" + e.toString());
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			listener.onRecievedNotifcationList(notificationList);

		}
	}
	/**
	 * Builds the campaign details/list from the json returned from the server.
	 * @param jsonFullObject
	 */
	private void buildMessageListJson(JSONObject jsonFullObject) {

		try {

			if(!jsonFullObject.isNull(KEY_MESSAGE_LIST)  && jsonFullObject.getJSONArray(KEY_MESSAGE_LIST) instanceof JSONArray)
			{
				JSONArray jMessageList = jsonFullObject.getJSONArray(KEY_MESSAGE_LIST);
				notificationList = new ArrayList<UserNotification>();
				for(int i=0;i<jMessageList.length();i++)
				{
					JSONObject jMessage = jMessageList.getJSONObject(i);
					notificationList.add(getCampainObject(jMessage));
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
	private UserNotification getCampainObject(JSONObject jsonObject) throws JSONException {

		UserNotification userNotificationDetails = new UserNotification();

		if(!jsonObject.isNull(KEY_MESSAGE))
			userNotificationDetails.setMessage(jsonObject.getString(KEY_MESSAGE));

		if(!jsonObject.isNull(KEY_TITLE))
			userNotificationDetails.setTitle(jsonObject.getString(KEY_TITLE));
		
		return userNotificationDetails;

	}
}

