/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pulp.campaigntracker.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.background.PeriodicService;
import com.pulp.campaigntracker.beans.GCM;
import com.pulp.campaigntracker.beans.UserNotification;
import com.pulp.campaigntracker.ui.PromotorMotherActivity;
import com.pulp.campaigntracker.ui.SplashScreen;
import com.pulp.campaigntracker.ui.SupervisorMotherActivity;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.ObjectSerializer;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	private static final String ID = "id";
	private static final String MESSAGE = "message";
	private static final String TITLE = "title";
	private static final String DATA = "data";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	private UserNotification notification;
	private int num;
	ArrayList<UserNotification> notifyList;

	public GcmIntentService() {
		super("GcmIntentService");

	}

	public static final String TAG = "GCM Auth";
	private static final String TYPE = "type";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		TLog.v(TAG, "extras : " + extras.toString());

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				Log.i(TAG, "Send error: " + extras.toString());

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				Log.i(TAG, "Deleted messages on server: " + extras.toString());

				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.

				if (!UtilityMethods
						.getLoginPreferences(getApplicationContext())
						.getString(ConstantUtils.USER_EMAIL, "").isEmpty()
						&& !UtilityMethods
								.getLoginPreferences(getApplicationContext())
								.getString(ConstantUtils.LOGIN_ID, "")
								.isEmpty()) {

					GCM mMessage = parseMessageObject(extras.getString(MESSAGE));
					// Post notification of received message.
					sendNotification(mMessage);
					Log.i(TAG, "Received: " + extras.getString(MESSAGE));
				}
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private GCM parseMessageObject(String string) {

		GCM gcmObject = null;
		gcmObject = new GCM();

		try {

			JSONObject messageObject = (JSONObject) new JSONTokener(string)
					.nextValue();

			if (!messageObject.isNull(TITLE))
				gcmObject.setTitle(messageObject.getString(TITLE));

			if (!messageObject.isNull(MESSAGE))
				gcmObject.setMessage(messageObject.getString(MESSAGE));

			if (!messageObject.isNull(ID))
				gcmObject.setId(messageObject.getInt(ID));

			if (!messageObject.isNull(TYPE))
				gcmObject.setType(messageObject.getString(TYPE));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gcmObject;

	}

	// Put the message into a notification and post it.
	@SuppressWarnings("unchecked")
	private void sendNotification(GCM msg) {

		if (msg != null) {
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
			bigText.bigText(msg.getMessage());
			bigText.setBigContentTitle(msg.getTitle());

			Uri uri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			num = getApplicationContext().getSharedPreferences("NOTIFI_NUMBER",
					0).getInt("Notif_Number_Constant", 0);
			int x = num + 1;
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.notification_app_icon)
					.setContentTitle(msg.getTitle()).setStyle(bigText)
					.setContentText(msg.getMessage()).setSound(uri)
					.setNumber(x);
			getApplicationContext().getSharedPreferences("NOTIFI_NUMBER", 0)
					.edit().putInt("Notif_Number_Constant", x).commit();
			// Save Notification To Preferences

			try {
				if ((ArrayList<UserNotification>) ObjectSerializer
						.deserialize(getApplicationContext()
								.getSharedPreferences(
										ConstantUtils.NOTIFICATION_CACHE, 0)
								.getString(ConstantUtils.NOTIFICATION, "")) == null) {
					notifyList = new ArrayList<UserNotification>();
				} else {
					notifyList = (ArrayList<UserNotification>) ObjectSerializer
							.deserialize(getApplicationContext()
									.getSharedPreferences(
											ConstantUtils.NOTIFICATION_CACHE, 0)
									.getString(ConstantUtils.NOTIFICATION, ""));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			notification = new UserNotification();
			Calendar calendar = Calendar.getInstance();
			notification.setTitle(msg.getTitle());
			notification.setMessage(msg.getMessage());
			notification.setNotifyTime(mBuilder.getNotification().when);
			notifyList.add(notification);
			try {
				getApplicationContext()
						.getSharedPreferences(ConstantUtils.NOTIFICATION_CACHE,
								0)
						.edit()
						.putString(ConstantUtils.NOTIFICATION,
								ObjectSerializer.serialize(notifyList))
						.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent newIntent = null;

		
			ConstantUtils.SYNC_INTERVAL = 5 * 1000;

			if (UtilityMethods.getLoginPreferences(getApplicationContext())
					.getString(ConstantUtils.USER_ROLE, "")
					.equals(LoginType.supervisor)) {
				newIntent = new Intent(getBaseContext(),
						SupervisorMotherActivity.class);
				Intent i = new Intent(this, PeriodicService.class);
				getApplicationContext()
						.getSharedPreferences("NOTIFI_NUMBER", 0).edit()
						.putInt("Notif_Number_Constant", 0).commit();
				this.startService(i);
			} else if (UtilityMethods
					.getLoginPreferences(getApplicationContext())
					.getString(ConstantUtils.USER_ROLE, "")
					.equals(LoginType.promotor)) {
				newIntent = new Intent(getBaseContext(),
						PromotorMotherActivity.class);
				Intent i = new Intent(this, PeriodicService.class);
				getApplicationContext()
						.getSharedPreferences("NOTIFI_NUMBER", 0).edit()
						.putInt("Notif_Number_Constant", 0).commit();
				this.startService(i);
			} else {
				newIntent = new Intent(getBaseContext(), SplashScreen.class);
				Intent i = new Intent(this, PeriodicService.class);
				getApplicationContext()
						.getSharedPreferences("NOTIFI_NUMBER", 0).edit()
						.putInt("Notif_Number_Constant", 0).commit();
				this.startService(i);
			}
			PendingIntent contentIntent = PendingIntent.getActivity(
					GcmIntentService.this, 0, newIntent, 0);

			mBuilder.setAutoCancel(true);
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(1, mBuilder.build());
			
			// nManager.notify((int)System.currentTimeMillis(),ncomp.build());
		}
	}
}
