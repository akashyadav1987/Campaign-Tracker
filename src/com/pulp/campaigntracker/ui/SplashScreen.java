/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pulp.campaigntracker.ui;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.InitData;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.utils.ConstantUtils;

import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.TypeFaceUtil;
import com.pulp.campaigntracker.utils.TypeFaceUtil.EnumCustomTypeFace;
import com.pulp.campaigntracker.utils.UtilityMethods;

/**
 * Main UI for the demo app.
 */
public class SplashScreen extends Activity {



	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * This is the project number you got from the API Console, as described in
	 * "Getting Started."
	 */
	String SENDER_ID = "408447823501";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Registeration";

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;

	String regid;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private TextView splashIcon;
    private SharedPreferences mApppref;
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			int level = intent.getIntExtra("level", 0);
			InitData mInitData = InitData.getInstance();
			mInitData.setLocationBatteryStatus(level);

		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);
		
		
		splashIcon = (TextView) findViewById(R.id.splashIcon);
		TypeFaceUtil.getInstance(getBaseContext()).setCustomTypeFaceText(
				EnumCustomTypeFace.ICOMOON, splashIcon);
		context = getApplicationContext();

		context.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		
		mApppref = UtilityMethods.getAppPreferences(context);


		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			registerInBackground();
			regid = UtilityMethods.getRegistrationId(context);

			TLog.v(TAG, "regid : " + regid);
			if (regid == null || regid.equals("")) {
				// This condition should never be true
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}

		if (UtilityMethods.getAppPreferences(getApplicationContext())
				.getString(ConstantUtils.USER_EMAIL, "").isEmpty()
				&& UtilityMethods.getAppPreferences(getApplicationContext())
						.getString(ConstantUtils.USER_NUMBER, "").isEmpty()) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					Intent intent = new Intent(getApplicationContext(),
							LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					if (!isFinishing())
						startActivity(intent);
					finish();
				}
			}, 2000);
		} else {
			LoginData mLoginData = LoginData.getInstance();
			mLoginData.setUsername(mApppref.getString(
					ConstantUtils.LOGIN_NAME, ""));
			mLoginData.setEmail(mApppref.getString(
					ConstantUtils.USER_EMAIL, ""));
			mLoginData.setPhoneNo(mApppref.getString(
					ConstantUtils.USER_NUMBER, ""));
			mLoginData.setId(mApppref.getString(ConstantUtils.LOGIN_ID,
					""));
			mLoginData.setAuthToken(mApppref.getString(
					ConstantUtils.AUTH_TOKEN, ""));
			mLoginData.setRole(mApppref.getString(ConstantUtils.USER_ROLE,
					""));

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					Intent intent = null;
//					if (LoginData.getInstance().getRole()
//							.equals(LoginType.promotor.toString())) {
//						intent = new Intent(context,
//								PromotorMotherActivity.class);
//					} else {

						intent = new Intent(getApplicationContext(),
								UserMotherActivity.class);

//					}

					if (!isFinishing()) {
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}

					finish();
				}
			}, 2000);

		}
		File cacheDir = StorageUtils.getCacheDirectory(context);

		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Create configuration for ImageLoader (all options are optional)
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getBaseContext())
				// You can pass your own memory cache implementation
				.discCache(new UnlimitedDiscCache(cacheDir))
				// You can pass your own disc cache implementation
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.enableLogging().build();
		// Initialize ImageLoader with created configuration. Do it once.
		imageLoader.init(config);
		
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	//	finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Check device for Play Services APK.
		checkPlayServices();

	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					TLog.v(TAG, "registerInBackground");
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					TLog.v(TAG, "msg" + msg);

					if(!regid.equals(UtilityMethods.getRegistrationId(context)))
					{
						
						UtilityMethods.storeRegistrationId(context, regid);
					}
					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.

					// setGcmPreferences(PROPERTY_GCM_REG_ID, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
					
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				// mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

}
