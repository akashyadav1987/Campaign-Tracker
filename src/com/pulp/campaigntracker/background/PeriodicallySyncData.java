package com.pulp.campaigntracker.background;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Base64;

import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.dao.LocationDatabase;
import com.pulp.campaigntracker.dao.UserFormUploadDatabase;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class PeriodicallySyncData extends Service{

	private String AUTH_TOKEN = null;
	private final String HASH = "hash";
	private final String LONGITIUE = "Longitiue";
	private final String LATITUDE = "Latitude";
	private final String LOCALITY = "Locality";
	private final String ADDRES_LINE = "AddresLine";
	private final String SUBADMIN = "subadmin";
	private final String ADMIN = "admin";
	private final String TIME = "time";
	private final String IMAGE = "image";
	private final String LOGIN_STATUS = "login_status";
	private final String FORM_KEY = "form_key";
	private final String FORM_VALUE = "form_value";
	private final String TAG = PeriodicallySyncData.class.getSimpleName();
	private final String ID = null;
	private final String NAME = null;
	private final String DEVICEID = null;
	SharedPreferences pref = UtilityMethods.getAppPreferences(getApplicationContext());
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		/*
		 * Get LocationDatabase and push all unsent data to server.
		 */
		
		
		AUTH_TOKEN = pref.getString(ConstantUtils.AUTH_TOKEN, "");
//		pushLocationUnsentData(ConstantUtils.POST_LOCATION_URL + AUTH_TOKEN);
		/*
		 * Get LoginDatabase and push all unsent data to server.
		 */
		pushLoginStatusUnsentData(ConstantUtils.POST_LOGIN_STATUS_URL + AUTH_TOKEN);

		/*
		 * Get FormDatabase and push all unsent data to server.
		 */
		pushFormUnsentData(ConstantUtils.POST_FORM_DATA_URL + AUTH_TOKEN);

		// Put the current time in sync preference after the sync is done.
		UtilityMethods.getAppPreferences(getApplicationContext()).edit().putLong(ConstantUtils.LAST_SYNC_TIME,System.currentTimeMillis()).commit();

		
		return super.onStartCommand(intent, flags, startId);
	}
//
//	public List<NameValuePair> getLocationJSONObjectToPost(MyLocation loc)
//	{
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		nameValuePairs.add(new BasicNameValuePair(TIME,loc.getTimeStamp()));
//		
//		nameValuePairs.add(new BasicNameValuePair(LATITUDE,Double.toString(loc.getLatitude())));
//		nameValuePairs.add(new BasicNameValuePair(LONGITIUE,Double.toString(loc.getLongitude())));
//		
//		nameValuePairs.add(new BasicNameValuePair(ID,pref.getString(ConstantUtils.LOGIN_ID, "")));
//		nameValuePairs.add(new BasicNameValuePair(DEVICEID,pref.getString(ConstantUtils.DEVICEID, "")));
//		nameValuePairs.add(new BasicNameValuePair(HASH, UtilityMethods
//				.calculateSyncHash(nameValuePairs)));
//		TLog.v(TAG, "nameValuePairs : "+nameValuePairs);
//		return nameValuePairs;
//	}
	public List<NameValuePair> getLoginStatusJSONObjectToPost(String time,byte[] image,int loginStatus)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(TIME,time));
		nameValuePairs.add(new BasicNameValuePair(IMAGE,Base64.encodeToString(image, 0)));
		nameValuePairs.add(new BasicNameValuePair(LOGIN_STATUS,Integer.toString(loginStatus)));
		
		nameValuePairs.add(new BasicNameValuePair(ID,pref.getString(ConstantUtils.LOGIN_ID, "")));
		nameValuePairs.add(new BasicNameValuePair(DEVICEID,pref.getString(ConstantUtils.DEVICEID, "")));
		nameValuePairs.add(new BasicNameValuePair(HASH, UtilityMethods
				.calculateSyncHash(nameValuePairs)));
		TLog.v(TAG, "nameValuePairs : "+nameValuePairs);
		return nameValuePairs;
	}
	public List<NameValuePair> getFormDataJSONObjectToPost(UserFormDetails userFormDetails)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair(IMAGE,Base64.encodeToString(userFormDetails.getImage(), 0)));
		nameValuePairs.add(new BasicNameValuePair(FORM_KEY,userFormDetails.getFieldName()));
		nameValuePairs.add(new BasicNameValuePair(FORM_VALUE,userFormDetails.getFieldValue()));
		
		nameValuePairs.add(new BasicNameValuePair(ID,getSharedPreferences(ConstantUtils.CAMPAIGNTRACKER_PREF, 0).getString(ConstantUtils.LOGIN_ID, "")));
		nameValuePairs.add(new BasicNameValuePair(DEVICEID,getSharedPreferences(ConstantUtils.CAMPAIGNTRACKER_PREF, 0).getString(ConstantUtils.DEVICEID, "")));
		nameValuePairs.add(new BasicNameValuePair(HASH, UtilityMethods
				.calculateSyncHash(nameValuePairs)));
		TLog.v(TAG, "nameValuePairs : "+nameValuePairs);
		
		return nameValuePairs;
	}

	// public void pushLocationUnsentData(String url)
	// {
	// LocationDatabase locationDatabase = new
	// LocationDatabase(getBaseContext());
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
	// List<NameValuePair> nameValuePair = getLocationJSONObjectToPost(loc);
	// int response = UtilityMethods.postJsonToServer(url, nameValuePair);
	// if(response==1)
	// locationDatabase.updateSentStatus(cursor.getString(0));
	// }
	// }
	// locationDatabase.close();
	// }
	public void pushFormUnsentData(String url)
	{
		LocationDatabase locationDatabase = new LocationDatabase(getBaseContext());
		locationDatabase.open();
		Cursor cursor = locationDatabase.getAllInfo();

		if (cursor.getCount() > 0) {
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				UserFormDetails userForm = new UserFormDetails();
				userForm.setFieldName(cursor.getString(1));
				userForm.setFieldValue(cursor.getString(2));
				userForm.setImage(cursor.getBlob(3));

				List<NameValuePair> nameValuePair = getFormDataJSONObjectToPost(userForm);
				int response = UtilityMethods.postJsonToServer(url, nameValuePair);
				if(response==1)
					locationDatabase.updateSentStatus(cursor.getString(0));
			}
		}
		locationDatabase.close();
	}
	public void pushLoginStatusUnsentData(String url)
	{
		UserFormUploadDatabase formDatabase = new UserFormUploadDatabase(getBaseContext());
		formDatabase.open();
		Cursor cursor = formDatabase.getAllInfo();

		if (cursor.getCount() > 0) {
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				List<NameValuePair> nameValuePair = getLoginStatusJSONObjectToPost(cursor.getString(1), cursor.getBlob(2), cursor.getInt(3));
				int response = UtilityMethods.postJsonToServer(url, nameValuePair);
				if(response==1)
					formDatabase.updateSentStatus(cursor.getString(0));
			}
		}
		formDatabase.close();
	}
}
