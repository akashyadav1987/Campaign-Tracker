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
import android.util.Log;
import android.widget.Toast;

import com.pulp.campaigntracker.beans.CheckIn;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.LoginErrorData;
import com.pulp.campaigntracker.controllers.JsonResponseAdapter;
import com.pulp.campaigntracker.dao.DataBaseHandler;

import com.pulp.campaigntracker.listeners.LoginDataRecieved;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ConstantUtils.LocationSyncType;
import com.pulp.campaigntracker.utils.ParserKeysConstants;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonLocationDataParser {


	private LoginErrorData mLoginErrorData;
	private MyLocation myLoc;
	private boolean isFromBroadcast;
	private Context mContext;
	private String response;


	private boolean isSuccess;

	@SuppressWarnings("unchecked")
	public void postLocationDataToURL(Context context, String auth_token,
			String cid, String lac, LocationSyncType type, String time,
			double latitude, double longitude, boolean isfromBroadcast) {
		this.mContext = context;
		GetJson getJson = new GetJson();
		myLoc = new MyLocation(auth_token, cid, lac, type, latitude, longitude,
				time);

		this.isFromBroadcast = isfromBroadcast;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_TIME, time));
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_LATITUDE, Double.toString(latitude)));
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_LONGITUDE, Double.toString(longitude)));
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_CID, cid));
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_LAC, lac));
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_AUTH_TOKEN, LoginData.getInstance()
				.getAuthToken()));
		params.add(new BasicNameValuePair(ParserKeysConstants.KEY_TYPE, type.toString()));

		if (UtilityMethods.isHoneycombOrHigher())
			getJson.executeForHoneyComb(params);
		else
			getJson.execute(params);
	}

	private class GetJson extends AsyncTask<List<NameValuePair>, Void, Void> {

		private String status;
		private String error;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void executeForHoneyComb(List<NameValuePair>... params) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		}

		@Override
		protected Void doInBackground(List<NameValuePair>... params) {

			JSONObject responseObject = JsonResponseAdapter.postJSONToUrl(
					ConstantUtils.POST_LOCATION_URL, params[0]);

			if (responseObject != null) {
				buildCheckinObject(responseObject);
			} else {
				isSuccess = false;
				mLoginErrorData = new LoginErrorData();
				mLoginErrorData
						.setMessage("Please check your connection settings");
				DataBaseHandler dataBaseHandler = new DataBaseHandler(mContext);
				if (myLoc != null) {
					if (!isFromBroadcast)
						dataBaseHandler.addLocation(myLoc);
					Toast.makeText(mContext, "Location Saved in db",
							Toast.LENGTH_LONG).show();
				}
				dataBaseHandler.close();

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
			

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (status != null) {
				if (status.equals("200"))
					Toast.makeText(mContext, "Location uploaded successfully",
							Toast.LENGTH_SHORT).show();
				else
					// listener.onLoginErrorDataRecieved(mLoginErrorData);
					Toast.makeText(mContext, "Location Error",
							Toast.LENGTH_SHORT).show();
			}
		}
	}
}