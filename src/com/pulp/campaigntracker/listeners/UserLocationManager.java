package com.pulp.campaigntracker.listeners;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.utils.TLog;

public class UserLocationManager implements LocationListener {

	private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	private Context mContext;
	private UpdateLocation instance;
	private MyLocation loc;
	public static UserLocationManager userLocationManager;
	Location location;
	private final String TAG = UserLocationManager.class.getSimpleName();

	/**
	 * Constructor taking Update Location listener instance.
	 * 
	 * @param instance
	 */
	public UserLocationManager(UpdateLocation instance, Context context) {

		this.instance = instance;
		this.mContext = context;
	}

	@Override
	public void onLocationChanged(Location location) {
		System.out
				.println("onLocationChanged...................................");
		GetAddress getaddress = new GetAddress();
		getaddress.execute(location);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	class GetAddress extends AsyncTask<Location, Void, MyLocation> {

		protected MyLocation doInBackground(Location... params) {
			List<Address> addresses = null;
			location = params[0];
			loc = new MyLocation();

			if (location != null) {
				try {
					Geocoder geocoder = new Geocoder(mContext,
							Locale.getDefault());
					addresses = geocoder.getFromLocation(
							location.getLatitude(), location.getLongitude(), 1);

				} catch (IOException e) {
					e.printStackTrace();
				}
				if (addresses != null && addresses.size() > 0) {
					Address add = addresses.get(0);

					TLog.v(TAG, "address : " + add);

					if (add.getLatitude() != 0)
						loc.setLatitude(location.getLatitude());

					if (add.getLongitude() != 0)
						loc.setLongitude(location.getLongitude());

					if (add.getLongitude() != 0)
						loc.setLongitude(location.getLongitude());

					loc.setTimeStamp(getCurrentTimeStamp());

				} else {
					Log.v(TAG, "address  == null");
				}
			} else {
				// retrieve a reference to an instance of TelephonyManager
				TelephonyManager telephonyManager = (TelephonyManager) LoginData
						.getMotherActivity().getSystemService(
								Context.TELEPHONY_SERVICE);
				GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager
						.getCellLocation();

				int cid = cellLocation.getCid();
				int lac = cellLocation.getLac();
				loc.setCellId(String.valueOf(cid));
				loc.setLacId(String.valueOf(lac));
				loc.setTimeStamp(getCurrentTimeStamp());

			}
			return loc;
		}

		protected void onPostExecute(MyLocation loc) {
			super.onPostExecute(loc);
			System.out
					.println("insideeeeeee location gettttttttttttttttttttttttt");
			instance.showLocation(loc);
		}
	}

	// get location object from GPS or network

	public Location getLocationObjecct() {

		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		// getting GPS status
		Boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		Boolean isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			// no network provider is enabled
			Toast.makeText(mContext, "Error In Getting Location",
					Toast.LENGTH_SHORT).show();

			return null;

		} else {
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			} else if (isGPSEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
			}
		}
		return location;
	}

	// get address of location object
	public void getAddress() {

		Location location = getLocationObjecct();
		GetAddress getaddress = new GetAddress();
		getaddress.execute(location);

	}

	// show location on map
	public void showOnMap() {

		Location location = getLocationObjecct();

		if (location != null) {
			instance.showMap(location.getLatitude(), location.getLongitude());
		} else
			Log.v(TAG, "Location == null");
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String format = s.format(new Date());
		return format;
	}

}