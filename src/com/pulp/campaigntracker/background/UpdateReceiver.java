package com.pulp.campaigntracker.background;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class UpdateReceiver extends BroadcastReceiver {

	private static final String TAG = UpdateReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
		NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();   
		if (isConnected)       
		{
			TLog.i(TAG, "connecte" +isConnected);   
			
			
			/*
			 * Check if app is initialized
			 */
			SharedPreferences prefs = UtilityMethods.getAppPreferences(context);
			
			if(prefs.getBoolean(ConstantUtils.INIT, false))
			{
				TLog.v(TAG, "App Initialized : ");

				/*
				 * Check if service is not already running.
				 * If not running check if elapsed time period is more than the Periodic Interval
				 */

				if(!ServiceCheck.isMyServiceRunning(PeriodicallySyncData.class, context))
				{
					TLog.v(TAG, "isMyServiceRunning : ");

					/*
					 * First time the service is running
					 */
					Intent syncIntent = new Intent(context,PeriodicallySyncData.class);

					if(UtilityMethods.getAppPreferences(context).getLong(ConstantUtils.LAST_SYNC_TIME, 0) == 0)
					{
						TLog.v(TAG, "First time");

						UtilityMethods.getAppPreferences(context).edit().putLong(ConstantUtils.LAST_SYNC_TIME, System.currentTimeMillis()).commit();
						context.startService(syncIntent);
					}
					else
					{
						long lastSyncTime = UtilityMethods.getAppPreferences(context).getLong(ConstantUtils.LAST_SYNC_TIME, 0);
						long diff = System.currentTimeMillis() - lastSyncTime;
						TLog.v(TAG, "diff : "+diff);

						/*
						 * Check if the sync interval is not set.
						 */

//						if(prefs.getInt(ConstantUtils.SYNC_INTERVAL, 1)!=1)
//						{
//							TLog.v(TAG, "SYNC_INTERVAL Initialized : ");
//
//							/*
//							 * Check the difference if more than the expected time .
//							 */
//							long syncInterval = ConstantUtils.INTERVAL_UPDATE * prefs.getInt(ConstantUtils.SYNC_INTERVAL, 1);
//
//							if (diff > syncInterval) {
//								TLog.v(TAG, "diff Started: "+diff);
//								context.startService(syncIntent);
//							}
//
//						}
					}
				}

			}
		}
		else 
			TLog.i(TAG, "not connecte" +isConnected);
	}
}