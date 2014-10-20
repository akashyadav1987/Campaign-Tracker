package com.pulp.campaigntracker.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.pulp.campaigntracker.beans.CheckIn;
import com.pulp.campaigntracker.beans.FormData;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.utils.ConstantUtils.LocationSyncType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "offlineDataMgr";

	// Contacts table name
	private static final String TABLE_CHECKIN = "checkin";
	private static final String TABLE_LOCATION = "location";
	private static final String TABLE_FROM_DATA="form_data_table";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_AUTH_TOKEN = "auth_token";
	private static final String KEY_ROLE = "role";
	private static final String KEY_ENCODEDIMAGE = "encodedImage";
	private static final String KEY_URL = "url";
	private static final String KEY_ID_CH = "id_ch";
	private static final String KEY_STORE_ID = "store_id";
	private static final String KEY_CAMPAIGN_ID = "campaign_id";
	private static final String KEY_TIME = "time";

	// Location Table Columns NAmes
	private static final String KEY_LOC_ID = "loc_id";

	private static final String LONGITIUE = "Longitiue";
	private static final String LATITUDE = "Latitude";
	private static final String CID = "cid";
	private static final String LAC = "lac";
	private static final String TYPE = "type";
	
	//Form Data values
	private static final String FORM_DATA="form_data";
	private static final String FORM_DATA_ID="form_data_id";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CHECKIN_TABLE = "CREATE TABLE " + TABLE_CHECKIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_AUTH_TOKEN + " TEXT,"
				+ KEY_ROLE + " TEXT," + KEY_ENCODEDIMAGE + " TEXT," + KEY_URL
				+ " TEXT," + KEY_ID_CH + " TEXT," + KEY_STORE_ID + " TEXT,"
				+ KEY_CAMPAIGN_ID + " TEXT," + KEY_TIME + " TEXT" + ")";

		String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
				+ KEY_LOC_ID + " INTEGER PRIMARY KEY," + KEY_AUTH_TOKEN
				+ " TEXT," + LONGITIUE + " TEXT," + LATITUDE + " TEXT," + CID
				+ " TEXT," + LAC + " TEXT," + TYPE + " TEXT," + KEY_TIME
				+ " TEXT" + ")";

		String CREATE_FORM_DATA_TABLE = "CREATE TABLE " + TABLE_FROM_DATA + "("
				+ FORM_DATA_ID + " INTEGER PRIMARY KEY," + FORM_DATA + " TEXT" + ")";
		
		db.execSQL(CREATE_CHECKIN_TABLE);
		db.execSQL(CREATE_LOCATION_TABLE);
		db.execSQL(CREATE_FORM_DATA_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FROM_DATA);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public long addCheckin(CheckIn checkin) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_AUTH_TOKEN, checkin.getAuth_token());
		values.put(KEY_ROLE, checkin.getRole());
		values.put(KEY_ENCODEDIMAGE, checkin.getEncodedImage());
		values.put(KEY_URL, checkin.getUrl());
		values.put(KEY_ID_CH, checkin.getId());
		values.put(KEY_STORE_ID, checkin.getStore_id());
		values.put(KEY_CAMPAIGN_ID, checkin.getCampaign_id());
		values.put(KEY_TIME, checkin.getTime());

		// Inserting Checkin Row
		long id = db.insert(TABLE_CHECKIN, null, values);
		Log.i("inside add", checkin + "");
		db.close(); // Closing database connection
		return id;
	}
	
	// Adding new FormData
	public long addFormData(JSONObject formData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(FORM_DATA, formData.toString());

		// Inserting FormData Row
		long id = db.insert(TABLE_FROM_DATA, null, values);
		Log.i("inside add", formData + "");
		db.close(); // Closing database connection
		return id;
	}

	// Adding new Location
	public void addLocation(MyLocation myLoc) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_AUTH_TOKEN, myLoc.getAuth_token());
		values.put(KEY_LOC_ID, myLoc.getId());
		values.put(LONGITIUE, myLoc.getLongitude());
		values.put(LATITUDE, myLoc.getLatitude());
		values.put(CID, myLoc.getCellId());
		values.put(LAC, myLoc.getLongitude());
		values.put(TYPE, myLoc.getType().toString());
		values.put(KEY_TIME, myLoc.getTimeStamp());

		// Inserting Location Row
		db.insert(TABLE_LOCATION, null, values);

		db.close(); // Closing database connection
	}

	// // Getting single Location
	// public MyLocation getLocation(int id) {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor cursor = db.query(TABLE_LOCATION, new String[] { KEY_LOC_ID,
	// KEY_AUTH_TOKEN, LONGITIUE, LATITUDE, KEY_LOCATION_URL, CID,
	// LAC, KEY_TIME, TYPE, }, KEY_STORE_ID
	// + "=?", new String[] { String.valueOf(id) }, null, null, null,
	// null);
	// if (cursor != null)
	// cursor.moveToFirst();
	//
	// CheckIn checkin = new CheckIn(cursor.getString(1), cursor.getString(2),
	// cursor.getString(3), cursor.getString(4), cursor.getString(5),
	// cursor.getString(6), cursor.getString(7), cursor.getString(8));
	//
	// // return contact
	// return checkin;
	// }
	//

	// Getting single Checkin
	public CheckIn getCheckin(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CHECKIN, new String[] { KEY_ID,
				KEY_AUTH_TOKEN, KEY_ROLE, KEY_URL, KEY_ID_CH, KEY_STORE_ID,
				KEY_CAMPAIGN_ID, KEY_TIME, KEY_ENCODEDIMAGE, }, KEY_STORE_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		CheckIn checkin = new CheckIn(cursor.getString(1), cursor.getString(2),
				cursor.getString(3), cursor.getString(4), cursor.getString(5),
				cursor.getString(6), cursor.getString(7), cursor.getString(8));

		// return contact
		return checkin;
	}

	// Getting All Checkin
	public List<CheckIn> getAllCheckins() {
		List<CheckIn> checkinList = new ArrayList<CheckIn>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_CHECKIN;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CheckIn checkin = new CheckIn(cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7),
						cursor.getString(8));

				// Adding contact to list
				checkinList.add(checkin);
			} while (cursor.moveToNext());
		}

		// return contact list
		return checkinList;
	}

	// Getting All Location
	public List<MyLocation> getAllLocations() {
		List<MyLocation> locationList = new ArrayList<MyLocation>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_LOCATION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				MyLocation myLoc = new MyLocation(cursor.getString(1),
						cursor.getString(3), cursor.getString(4),
						LocationSyncType.valueOf(cursor.getString(6)),
						cursor.getDouble(2), cursor.getDouble(3),
						cursor.getString(7));
				myLoc.setId(cursor.getInt(0));

				// Adding contact to list
				locationList.add(myLoc);
			} while (cursor.moveToNext());
		}

		// return contact list
		return locationList;
	}

	// Getting All Location
	public List<FormData> getAllFormData() {
		List<FormData> formDataList = new ArrayList<FormData>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_FROM_DATA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

			 FormData formData =new FormData();
			 formData.setId(cursor.getLong(0));
			 try {
				formData.setFormDataObject(new JSONObject(cursor.getString(1)));
			} catch (JSONException e) {
				e.printStackTrace();
			}

				// Adding form data to list
				formDataList.add(formData);
			} while (cursor.moveToNext());
		}

		// return contact list
		return formDataList;
	}
	
	// Updating single contact
	public int updateCheckin(CheckIn checkin) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_AUTH_TOKEN, checkin.getAuth_token());
		values.put(KEY_ROLE, checkin.getRole());
		values.put(KEY_CAMPAIGN_ID, checkin.getCampaign_id());
		values.put(KEY_ENCODEDIMAGE, checkin.getEncodedImage());
		values.put(KEY_ID_CH, checkin.getId());
		values.put(KEY_STORE_ID, checkin.getStore_id());
		values.put(KEY_TIME, checkin.getTime());
		values.put(KEY_URL, checkin.getUrl());

		// updating row
		return db.update(TABLE_CHECKIN, values, KEY_STORE_ID + " = ?",
				new String[] { String.valueOf(checkin.getStore_id()) });
	}

	// Deleting single contact
	public void deleteCheckin(CheckIn checkIn) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CHECKIN, KEY_STORE_ID + " = ?",
				new String[] { String.valueOf(checkIn.getStore_id()) });
		db.close();
	}
	// Deleting single Form DATA
		public void deleteFormData(FormData jsonFormData) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_FROM_DATA, FORM_DATA_ID + " = ?",
					new String[] { String.valueOf(jsonFormData.getId()) });
			db.close();
		}
	// Deleting single Location
	public void deletelocation(MyLocation myLoc) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOCATION, KEY_LOC_ID + " = ?",
				new String[] { String.valueOf(myLoc.getId()) });
		db.close();
	}

	// Getting Location Count
	public int getLocationCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Getting contacts Count
	public int getCheckinCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CHECKIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
}