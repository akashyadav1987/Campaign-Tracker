package com.pulp.campaigntracker.background;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		 Toast.makeText(context, "BAttery's dying!!", Toast.LENGTH_LONG).show();
	     //Stop Location Service
		  intent = new Intent(context, PeriodicLocation.class);
	     PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	     AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	     alarmManager.cancel(pendingIntent);
	}
}
