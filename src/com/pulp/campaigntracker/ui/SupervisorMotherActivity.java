package com.pulp.campaigntracker.ui;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.InitData;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.listeners.CampaignDetailsRecieved;
import com.pulp.campaigntracker.listeners.FragmentListener;
import com.pulp.campaigntracker.listeners.InitializeApp;
import com.pulp.campaigntracker.parser.JsonGetCampaignDetails;
import com.pulp.campaigntracker.parser.JsonInitDataParser;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class SupervisorMotherActivity extends ActionBarActivity implements
		FragmentListener, CampaignDetailsRecieved, InitializeApp {

	private static final String TAG = SupervisorMotherActivity.class
			.getSimpleName();
	private ActionBarHelper mActionBar;
	private android.support.v4.app.Fragment mCurrentFragment;
	private int mFragmentHolder;
	private CampaignListFragment mCampaignFragment;
	JsonGetCampaignDetails jsonGetCampaignDetails;
	private List<CampaignDetails> campaignDetailsList;
	private JsonInitDataParser jsonInitDataParser;
	ArrayList<UserProfile> promotorList = new ArrayList<UserProfile>();
	private Dialog mProgressDialog;

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
			// String fileName = "logcat_"+System.currentTimeMillis()+".txt";

			String fullPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File myFile = new File(fullPath + File.separator + "/"
					+ "Logcat(Supervisor).txt");
			Process process = Runtime.getRuntime().exec(
					"logcat -f " + myFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mActionBar = createActionBarHelper();
		mActionBar.init();
		mFragmentHolder = R.id.supervisor_fragment_holder;
		executeQuery();
		LoginData.setMotherActivity(this);
	}

	@Override
	public void requestStartActivity(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestStartActivityForResult(Intent intent, int requestCode) {

		switch (requestCode) {
		case ConstantUtils.ACTION_PICTURE:

			break;

		default:
			break;
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu items for use in the action bar
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.promoter_action_bar, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// switch (item.getItemId()) {
	// case R.id.action_all_promotors:
	//
	// if (campaignDetailsList != null && campaignDetailsList.size() > 0) {
	// AllPromotorListFragment allPromotorListFragment = new
	// AllPromotorListFragment();
	// Bundle mBundle = new Bundle();
	// mBundle.putParcelableArrayList(ConstantUtils.PROMOTOR_LIST,
	// promotorList);
	// allPromotorListFragment.setArguments(mBundle);
	// onItemSelected(allPromotorListFragment, true);
	// }
	//
	// break;
	//
	// case R.id.notifications:
	// NotificationListFragment notificationListFragment = new
	// NotificationListFragment();
	// this.onItemSelected(notificationListFragment, true);
	// break;
	//
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	public void setActionBarTitle(String title) {

		mActionBar.setTitle(title);

	}

	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper(this);
	}

	public android.support.v4.app.Fragment getCurrentFragment() {
		return getSupportFragmentManager().findFragmentById(mFragmentHolder);
	}

	public void executeQuery() {
		mProgressDialog = new Dialog(SupervisorMotherActivity.this,
				R.style.transparent_dialog_no_titlebar);

		// TLog.i(TAG, "show mProgressDialog " + mProgressDialog);
		mProgressDialog.setContentView(R.layout.please_wait_dialog);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mProgressDialog.cancel();
				onBackPressed();

			}

		});

		jsonGetCampaignDetails = JsonGetCampaignDetails.getInstance();
		// jsonGetCampaignDetails.getCampaignDetailsFromURL(ConstantUtils.SERVER,
		// this,LoginType.supervisor,getBaseContext());
		jsonGetCampaignDetails
				.getCampaignDetailsFromURL(ConstantUtils.CAMPAIGN_DETAILS_URL,
						this, LoginData.getInstance().getId(), LoginData
								.getInstance().getAuthToken(),
						LoginType.supervisor, getBaseContext());

	}

	@Override
	public void onCampaignDetailsRecieved(CampaignDetails cd) {

	}

	@Override
	public void onCampaignDetailsRecieved(
			ArrayList<CampaignDetails> campaignDetailsList) {

		if (campaignDetailsList != null && campaignDetailsList.size() > 0) {
			mProgressDialog.dismiss();
			Bundle mBundle = new Bundle();
			mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
					campaignDetailsList);

			jsonInitDataParser = new JsonInitDataParser();
			jsonInitDataParser.getInitDataFromURL(ConstantUtils.INIT_URL, this,
					getBaseContext());

			mCampaignFragment = new CampaignListFragment();
			mCampaignFragment.setArguments(mBundle);
			onItemSelected(mCampaignFragment, false);
		}

	}

	@Override
	public void onItemSelected(android.support.v4.app.Fragment frg,
			boolean animate) {
		mCurrentFragment = getCurrentFragment();
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();

		if (animate) {
			ft.setCustomAnimations(R.anim.slide_left_to_right,
					R.anim.slide_right_to_left, R.anim.slide_out_right_to_left,
					R.anim.slide_out_left_to_right);

		}

		ft.addToBackStack(null);
		ft.replace(mFragmentHolder, frg);
		ft.commitAllowingStateLoss();
		mCurrentFragment = frg;

	}

	@Override
	public void onInitDataRecieved(InitData initData) {

		TLog.v(TAG, "onInitDataRecieved ");
		if (initData != null) {
			TLog.v(TAG, "onInitDataRecieved : " + initData);

			SharedPreferences prefs = UtilityMethods
					.getInitPreferences(getBaseContext());
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(ConstantUtils.BATTERY_STATUS,
					initData.getLocationBatteryStatus());
			editor.putInt(ConstantUtils.LOCATION_INTERVAL,
					initData.getLocationPeriodicInterval());
			// editor.putInt(ConstantUtils.SYNC_INTERVAL,
			// initData.getSyncUnsentDataInterval());
			editor.putString(ConstantUtils.LOCATION_START_TIME,
					initData.getLocationStartInterval());
			editor.putString(ConstantUtils.LOCATION_END_TIME,
					initData.getLocationEndInterval());
			editor.commit();
		}
	}

	@Override
	public void onBackPressed() {
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();

		}
		// super.onBackPressed();
		int cnt = getSupportFragmentManager().getBackStackEntryCount();
		// android.support.v4.app.FragmentManager.BackStackEntry bk =
		// getSupportFragmentManager()
		// .getBackStackEntryAt(cnt - 1);
		// bk.toString();

		if (mCurrentFragment != null) {
			if (mCurrentFragment.getClass().toString()
					.contains("PromotorDetailsFragment")) {

				// if (((PromotorDetailsFragment) mCurrentFragment).onBack()) {
				// } else {
				super.onBackPressed();
				// }
			} else {
				super.onBackPressed();
			}

			if (cnt == 1) {
				this.finish();
			}
		} else {

			SupervisorMotherActivity.this.finish();

			// super.onBackPressed();
			jsonGetCampaignDetails.killAsyncTask();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// No call for super(). Bug on API Level > 11.
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		List<android.support.v4.app.Fragment> mList = this
				.getSupportFragmentManager().getFragments();
		if (mList != null)

			for (android.support.v4.app.Fragment fragment : mList) {

				// getFragmentManager().beginTransaction().remove(fragment)
				// .commitAllowingStateLoss();
			}
	}

}
