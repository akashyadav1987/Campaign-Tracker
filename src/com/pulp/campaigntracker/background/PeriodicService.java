package com.pulp.campaigntracker.background;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class PeriodicService extends Service {
	private static final int TRIGGER_TIME = 10000;
	private static final String TAG = PeriodicService.class.getSimpleName();
	Calendar calendar = Calendar.getInstance();


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		/*
		 * Check if app is initialized
		 */
		SharedPreferences prefs = UtilityMethods
				.getAppPreferences(getApplicationContext());
		TLog.d(TAG, "AlarmManager");

		// if(prefs.getBoolean(ConstantUtils.INIT, false))
		{

			Log.e("Service", "Intailised");

			/*
			 * Check if the location interval is set.
			 */
			// if(prefs.getInt(ConstantUtils.LOCATION_INTERVAL, 1)!=1)
			// {

			long updateInterval = ConstantUtils.LOCATION_UPDATE_INTERVAL
					* prefs.getInt(ConstantUtils.LOCATION_INTERVAL, 1);

			Intent serviceIntent = new Intent(PeriodicService.this,
					PeriodicLocation.class);
			PendingIntent pendingIntent = PendingIntent.getService(
					PeriodicService.this, 0, serviceIntent, 0);
			AlarmManager alarmManager = (AlarmManager) PeriodicService.this
					.getSystemService(PeriodicService.this.ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), ConstantUtils.SYNC_INTERVAL, pendingIntent);
			// }
		}
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
