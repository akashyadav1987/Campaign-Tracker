package com.pulp.campaigntracker.ui;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.InitData;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.listeners.FragmentListener;
import com.pulp.campaigntracker.listeners.InitializeApp;
import com.pulp.campaigntracker.parser.JsonInitDataParser;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class PromotorMotherActivity extends ActionBarActivity implements
		FragmentListener, InitializeApp {

	private static final String TAG = PromotorMotherActivity.class
			.getSimpleName();
	private ActionBarHelper mActionBar;
	private Fragment mCurrentFragment;
	private int mFragmentHolder;
	private Fragment mPromoterMainScreenFragment;
	private JsonInitDataParser jsonInitDataParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supervisor);

		// OverRide for hardware menu item

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

		// Print Logcat to file.
		try {

			String fullPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File myFile = new File(fullPath + File.separator + "/"
					+ "Logcat(Promoter).txt");
			Process process = Runtime.getRuntime().exec(
					"logcat -f " + myFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mActionBar = createActionBarHelper();
		mActionBar.init();
		mActionBar.setTitle("Promoter Login");
		mFragmentHolder = R.id.supervisor_fragment_holder;

		mPromoterMainScreenFragment = new PromotorMainScreenFragment();
		onItemSelected(mPromoterMainScreenFragment, false);

		LoginData.getInstance();
		LoginData.setMotherActivity(this);
		

		executeQuery();
	}



	public void setActionBarTitle(String title) {

		mActionBar.setTitle(title);

	}

	public void executeQuery() {
		jsonInitDataParser = new JsonInitDataParser();
		jsonInitDataParser.getInitDataFromURL(ConstantUtils.INIT_URL, this,
				getBaseContext());

	}

	@Override
	public void onItemSelected(Fragment frg, boolean animate) {

		mCurrentFragment = getCurrentFragment();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (animate)
			ft.setCustomAnimations(R.anim.slide_left_to_right,
					R.anim.slide_right_to_left, R.anim.slide_out_right_to_left,
					R.anim.slide_out_left_to_right);

		ft.addToBackStack(null);
		ft.replace(mFragmentHolder, frg);
		ft.commit();
		mCurrentFragment = frg;
		TLog.i(TAG,
				"showFragment mCurrentFragment " + mCurrentFragment.getClass());

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		// super.onBackPressed();
		if(mCurrentFragment!=null){
		if (mCurrentFragment.getClass().toString()
				.contains("PromotorMainScreenFragment")) {
			if (((PromotorMainScreenFragment) mCurrentFragment).onBack()) {
				// No Checkinlayout or route laout is open
				Toast.makeText(this, "Back", Toast.LENGTH_LONG).show();
			} else {
				this.finish();
			}
		} else {
			this.finish();
		}
		}else
			super.onBackPressed();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// No call for super(). Bug on API Level > 11.
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		List<android.support.v4.app.Fragment> mList = this
				.getSupportFragmentManager().getFragments();
		if (mList != null)

			for (android.support.v4.app.Fragment fragment : mList) {

				// getFragmentManager().beginTransaction().remove(fragment).commit();
			}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.promoter_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case R.id.notifications:
			NotificationListFragment notificationListFragment = new NotificationListFragment();
			this.onItemSelected(notificationListFragment, true);
			break;
	
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper(this);
	}

	public Fragment getCurrentFragment() {
		return getSupportFragmentManager().findFragmentById(mFragmentHolder);
	}

	@Override
	public void onInitDataRecieved(InitData initData) {
		if (initData != null) {
			SharedPreferences prefs = UtilityMethods
					.getAppPreferences(getApplicationContext());
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(ConstantUtils.BATTERY_STATUS,
					initData.getLocationBatteryStatus());
			editor.putInt(ConstantUtils.LOCATION_INTERVAL,
					initData.getLocationPeriodicInterval());
			editor.putInt(ConstantUtils.SYNC_INTERVAL_TIME,
					initData.getSyncUnsentDataInterval());
			editor.putString(ConstantUtils.LOCATION_START_TIME,
					initData.getLocationStartInterval());
			editor.putString(ConstantUtils.LOCATION_END_TIME,
					initData.getLocationEndInterval());
			editor.putBoolean(ConstantUtils.INIT, true);
			editor.commit();
		}
	}

	@Override
	public void requestStartActivity(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestStartActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		
	}



	
}
