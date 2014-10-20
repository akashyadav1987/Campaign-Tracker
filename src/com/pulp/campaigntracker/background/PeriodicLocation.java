package com.pulp.campaigntracker.background;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.dao.LocationDatabase;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.listeners.UpdateLocation;
import com.pulp.campaigntracker.listeners.UserLocationManager;
import com.pulp.campaigntracker.parser.JsonLocationDataParser;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ConstantUtils.LocationSyncType;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class PeriodicLocation extends IntentService {

	private static final String AUTH_TOKEN = "auth_token";
	private static final String LONGITIUE = "Longitiue";
	private static final String LATITUDE = "Latitude";
	private static final String CID = "cid";
	private static final String LAC = "lac";
	private static final String TYPE = "type";
	private static final String TIME = "time";

	private static final String TAG = PeriodicLocation.class.getSimpleName();
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String DEVICEID = "device_id";

	public PeriodicLocation() {
		super("PeriodicLocation");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		TLog.v(TAG, "onHandleIntent ");
		// UserLocationManager ulm = new UserLocationManager(this,
		// getApplicationContext());
		// ulm.getAddress();
	}

	//
	// @Override
	// public void showMap(double latitude, double longitude) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void showLocation(MyLocation loc) {
	//
	// if (loc != null) {
	// loc.setType(LocationSyncType.general);
	// JsonLocationDataParser jsonLocationDataParser = new
	// JsonLocationDataParser();
	// jsonLocationDataParser.postLocationDataToURL(
	// getApplicationContext(), LoginData.getInstance()
	// .getAuthToken(), loc.getCellId(), loc.getLacId(),
	// loc.getType(), loc.getTimeStamp(), loc.getLatitude(), loc
	// .getLongitude(), false);
	//
	// }
	// }

	// public List<NameValuePair> getJSONObjectToPost(MyLocation loc,
	// LocationSyncType type) {
	// SharedPreferences pref = UtilityMethods
	// .getAppPreferences(getApplicationContext());
	//
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	// nameValuePairs.add(new BasicNameValuePair(TIME, loc.getTimeStamp()));
	// nameValuePairs.add(new BasicNameValuePair(LATITUDE, Double.toString(loc
	// .getLatitude())));
	// nameValuePairs.add(new BasicNameValuePair(LONGITIUE, Double
	// .toString(loc.getLongitude())));
	// nameValuePairs.add(new BasicNameValuePair(CID, loc.getCellId()));
	// nameValuePairs.add(new BasicNameValuePair(LAC, loc.getLacId()));
	// nameValuePairs.add(new BasicNameValuePair(AUTH_TOKEN, LoginData
	// .getInstance().getAuthToken()));
	// nameValuePairs.add(new BasicNameValuePair(TYPE, type.toString()));
	// return nameValuePairs;
	// }
	//
	// public void pushAllUnsentData() {
	// LocationDatabase locationDatabase = new LocationDatabase(
	// getBaseContext());
	// locationDatabase.open();
	// Cursor cursor = locationDatabase.getAllInfo();
	//
	// if (cursor.getCount() > 0) {
	// cursor.moveToPosition(-1);
	// while (cursor.moveToNext()) {
	// MyLocation loc = new MyLocation();
	// loc.setTimeStamp(cursor.getString(1));
	//
	// loc.setLatitude(cursor.getDouble(6));
	// loc.setLongitude(cursor.getDouble(7));
	// List<NameValuePair> nameValuePair = getJSONObjectToPost(loc,
	// LocationSyncType.general);
	// int response = UtilityMethods.postJsonToServer(
	// ConstantUtils.POST_LOCATION_URL, nameValuePair);
	// if (response == 1)
	// locationDatabase.updateSentStatus(cursor.getString(0));
	// }
	// }
	// locationDatabase.close();
	//
	// }

}
