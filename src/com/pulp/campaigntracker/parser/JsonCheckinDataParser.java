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
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.listeners.UpdateLocation;
import com.pulp.campaigntracker.listeners.UserLocationManager;
import com.pulp.campaigntracker.utils.ConstantUtils.LocationSyncType;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonCheckinDataParser {

	private final String TAG = JsonCheckinDataParser.class.getSimpleName();
	private LoginErrorData mLoginErrorData;
	private CheckIn checkIn;
	private final String KEY_RESPONSE = "response";
	private final String KEY_STATUS = "status";
	private Context mContext;
	private boolean isFromBrodcast;


	@SuppressWarnings("unchecked")
	public void postCheckinDataToURL(Context mContext, String auth_token,
			String role, String encodedimage, String url, String id,
			String time, String campagin_id, String store_id,
			boolean isFromBrodcast) {

		this.mContext = mContext;
		this.isFromBrodcast = isFromBrodcast;
		GetJson getJson = new GetJson();
		checkIn = new CheckIn(auth_token, role, encodedimage, url, id,
				store_id, campagin_id, time);

		List<NameValuePair> params = new ArrayList<NameValuePair>();

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

	private class GetJson extends
			AsyncTask<List<NameValuePair>, String, String> implements
			UpdateLocation {

		private String status = "fail";
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void executeForHoneyComb(List<NameValuePair>... params) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		}

		@Override
		protected String doInBackground(List<NameValuePair>... params) {

			JSONObject responseObject = JsonResponseAdapter.postJSONToUrl(
					checkIn.getUrl(), params[0]);
			if (responseObject != null) {
				buildCheckinObject(responseObject);
			} else {
				mLoginErrorData = new LoginErrorData();
				mLoginErrorData
						.setMessage("Please check your connection settings");
				DataBaseHandler dataBaseHandler = new DataBaseHandler(mContext);
				if (checkIn != null) {
					if (!isFromBrodcast)
						dataBaseHandler.addCheckin(checkIn);
					Log.i("added to database is:  ", checkIn + "");
					Log.i("database checin:  ",
							dataBaseHandler.getAllCheckins() + "");
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

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (status.equals("200"))
				Toast.makeText(mContext, "Image uploaded successfully",
						Toast.LENGTH_LONG).show();
			else
				// listener.onLoginErrorDataRecieved(mLoginErrorData);
				Toast.makeText(mContext, "Image upload Error",
						Toast.LENGTH_SHORT).show();
			UserLocationManager ulm1 = new UserLocationManager(this, mContext);
			ulm1.getAddress();

		}

		@Override
		public void showLocation(MyLocation loc) {
			
			if (loc != null) {
				if (checkIn.getUrl().equals(ConstantUtils.CHECK_IN_URL))
					loc.setType(LocationSyncType.checkIn);
				else
					loc.setType(LocationSyncType.checkOut);
				JsonLocationDataParser jsonLocationDataParser = new JsonLocationDataParser();
				jsonLocationDataParser.postLocationDataToURL(mContext,
						LoginData.getInstance().getAuthToken(),
						loc.getCellId(), loc.getLacId(), loc.getType(),
						loc.getTimeStamp(), loc.getLatitude(),
						loc.getLongitude(), false);
			
				}
		
		}
		
		@Override
		public void showMap(double latitude, double longitude) {
			// TODO Auto-generated method stub

		}
		
   }
}