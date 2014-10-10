package com.pulp.campaigntracker.background;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.internal.cu;
import com.pulp.campaigntracker.dao.LocationDatabase;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.listeners.UpdateLocation;
import com.pulp.campaigntracker.listeners.UserLocationManager;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class PeriodicLocation extends IntentService implements UpdateLocation {



	private static final String HASH = "hash";
	private static final String LONGITIUE = "Longitiue";
	private static final String LATITUDE = "Latitude";
	private static final String LOCALITY = "Locality";
	private static final String ADDRES_LINE = "AddresLine";
	private static final String SUBADMIN = "subadmin";
	private static final String ADMIN = "admin";
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
		UserLocationManager ulm = new UserLocationManager(this,
				getApplicationContext());
		ulm.getAddress();
	}

	@Override
	public void showMap(double latitude, double longitude) {
		// TODO Auto-generated method stub

	}
	@Override
	public void showLocation(MyLocation loc) {
	
		if (loc != null) {
			
			Log.e("Service","Location    "+loc.toString());

			List<NameValuePair> nameValuePair = getJSONObjectToPost(loc);
			int response = UtilityMethods.postJsonToServer(ConstantUtils.POST_LOCATION_URL, nameValuePair);
			if(response!=1)
			{
				LocationDatabase locationDatabase = new LocationDatabase(getBaseContext());
				locationDatabase.open();
				locationDatabase.insertInfo(loc.getTimeStamp(), loc.getAdmin(), loc.getSubAdmin(), loc.getAddressLine(), loc.getLocality(), loc.getLatitude(), loc.getLongitude());
				locationDatabase.close();
			}
			else
				pushAllUnsentData();
		}	
	}

	public List<NameValuePair> getJSONObjectToPost(MyLocation loc)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(TIME,loc.getTimeStamp()));
		nameValuePairs.add(new BasicNameValuePair(ADMIN,loc.getAdmin()));
		nameValuePairs.add(new BasicNameValuePair(SUBADMIN,loc.getSubAdmin()));
		nameValuePairs.add(new BasicNameValuePair(ADDRES_LINE,loc.getAddressLine()));
		nameValuePairs.add(new BasicNameValuePair(LOCALITY,loc.getLocality()));
		nameValuePairs.add(new BasicNameValuePair(LATITUDE,Double.toString(loc.getLatitude())));
		nameValuePairs.add(new BasicNameValuePair(LONGITIUE,Double.toString(loc.getLongitude())));
		nameValuePairs.add(new BasicNameValuePair(ID,getSharedPreferences(ConstantUtils.LOGIN, 0).getString(ConstantUtils.LOGIN_ID, "login_id")));
		nameValuePairs.add(new BasicNameValuePair(NAME,getSharedPreferences(ConstantUtils.LOGIN, 0).getString(ConstantUtils.LOGIN_NAME, "login_name")));
		nameValuePairs.add(new BasicNameValuePair(DEVICEID,getSharedPreferences(ConstantUtils.LOGIN, 0).getString(ConstantUtils.DEVICEID, "device_id")));
		nameValuePairs.add(new BasicNameValuePair(HASH, UtilityMethods
				.calculateSyncHash(nameValuePairs)));
		Log.e("Service", "nameValuePairs : "+nameValuePairs);
		return nameValuePairs;
	}

	public void pushAllUnsentData()
	{
		LocationDatabase locationDatabase = new LocationDatabase(getBaseContext());
		locationDatabase.open();
		Cursor cursor = locationDatabase.getAllInfo();

		if (cursor.getCount() > 0) {
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				MyLocation loc = new MyLocation();
				loc.setTimeStamp(cursor.getString(1));
				loc.setAdmin(cursor.getString(2));
				loc.setSubAdmin(cursor.getString(3));
				loc.setAddressLine(cursor.getString(4));
				loc.setLocality(cursor.getString(5));
				loc.setLatitude(cursor.getDouble(6));
				loc.setLongitude(cursor.getDouble(7));
				List<NameValuePair> nameValuePair = getJSONObjectToPost(loc);
				int response = UtilityMethods.postJsonToServer(ConstantUtils.POST_LOCATION_URL, nameValuePair);
				if(response==1)
					locationDatabase.updateSentStatus(cursor.getString(0));
			}
		}
		locationDatabase.close();

	}


}
