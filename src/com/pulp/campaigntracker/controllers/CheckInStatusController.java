package com.pulp.campaigntracker.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import com.pulp.campaigntracker.utils.ConstantUtils;



public class CheckInStatusController {


	private SharedPreferences preferences;

	/**
	 * class to get check in status for promoter and supervisor
	 * @param store_id
	 */
	public CheckInStatusController(Context mContext)
	{
		
		
		preferences = mContext.getSharedPreferences(ConstantUtils.CHECKIN_DETAILS,
				Context.MODE_PRIVATE);

	}
	
	/**
	 * update check in Status for store . This would be for promotor
	 * @param store_id
	 */
	public boolean updateCheckInStatusForStore(String store_id, boolean status)
	{
		
		
		String store_id_str = preferences.getString(ConstantUtils.STORE_CHECKIN,"");
		
		boolean isAlreadyCheckedIn =  preferences.getBoolean(store_id_str, false);
		
		if(isAlreadyCheckedIn && !store_id_str.equals(store_id))
			return false;
		
			
		preferences.edit()
		.putString(ConstantUtils.STORE_CHECKIN, store_id)
		.commit();
		
		preferences.edit()
		.putBoolean(String.valueOf(store_id), status )
		.commit();
		
		return true;
		
	}
	/**
	 * update check in Status . This would be for promotor
	 * @param store_id
	 */
	
	public void updateCheckInStatus( boolean status)
	{
		
		
		preferences.edit()
		.putBoolean(ConstantUtils.STATUS, status)
		.commit();
	}

	/**
	 * get check in Status . This would be for promotor
	 * @param store_id
	 */
	public boolean getCheckInStatus()
	{
		
		
		return preferences.getBoolean(ConstantUtils.STATUS, false);
		
	}

	/**
	 * get check in Status for Store
	 * @param store_id
	 */
	public boolean getCheckInStatusForStore(String store_id)
	{
		String store_id_str = preferences.getString(ConstantUtils.STORE_CHECKIN,"");
		
		if(store_id_str.equals(store_id))
			return preferences.getBoolean(store_id_str, false);
		else
			return false;
	}
	/**
	 * Clear check in Status for Store
	 * @param store_id
	 */
	public void clearCheckInStatusForStore(int store_id)
	{
		
		preferences.edit()
		.putString(ConstantUtils.STORE_CHECKIN, "" )
		.commit();
		
		preferences.edit()
		.putBoolean(String.valueOf(store_id), false )
		.commit();
		
	}
	
}

