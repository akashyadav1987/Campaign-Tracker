package com.pulp.campaigntracker.controllers;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserNotification;
import com.pulp.campaigntracker.ui.AllPromotorListFragment;
import com.pulp.campaigntracker.ui.UserMotherActivity;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ObjectSerializer;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class NotificationListFragment extends Fragment {

	private static final String TAG = AllPromotorListFragment.class
			.getSimpleName();
	private static FragmentActivity mActivity;
	private Context mContext;
	private ArrayList<UserNotification> mNotificationList;

	private NotificationListAdapter notificationListAdapter;
	private ListView notificationList;
	private View emptyText;
	public static GcmReceiver gcmReceiver;
	
	private SharedPreferences mAppPref; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity().getApplicationContext();
		mAppPref = UtilityMethods.getAppPreferences(mContext);

	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.notification_list, container,
				false);
		gcmReceiver = new GcmReceiver();
		getActivity().registerReceiver(gcmReceiver,
				new IntentFilter("googleCloudMessage"));

		emptyText = view.findViewById(R.id.emptyText);
		setActionBarTitle();
		setHasOptionsMenu(true);

		mActivity.getApplicationContext()
				.getSharedPreferences("NOTIFY_NUMBER", 0).edit()
				.putInt("Notif_Number_Constant", 0).commit();

		notificationList = (ListView) view.findViewById(R.id.notificationList);
		try {

			mNotificationList = (ArrayList<UserNotification>) ObjectSerializer
					.deserialize(mAppPref
							.getString(ConstantUtils.NOTIFICATION, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mNotificationList != null) {

			if (notificationListAdapter == null)
				notificationListAdapter = new NotificationListAdapter(mContext,
						mNotificationList);

			notificationListAdapter.notifyDataSetChanged();
			// promotorList.setOnScrollListener(new EndlessScrollListener());
			notificationList.setAdapter(notificationListAdapter);
			notificationList.setEmptyView(emptyText);
		}
		return view;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle("Notifications");
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.notification_action_bar, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear_all:
			if (mNotificationList != null) {
				mNotificationList.clear();
				notificationListAdapter.notifyDataSetChanged();
				try {
						mAppPref
							.edit()
							.putString(
									ConstantUtils.NOTIFICATION,
									ObjectSerializer
											.serialize(mNotificationList))
							.commit();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(mContext, "Notification list empty ",
						Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.notifications:
			NotificationListFragment notificationListFragment = new NotificationListFragment();
			((UserMotherActivity) mActivity).onItemSelected(
					notificationListFragment, true);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class GcmReceiver extends BroadcastReceiver {

		@SuppressWarnings("unchecked")
		public void onReceive(Context context, Intent intent) {

			try {

				mNotificationList = (ArrayList<UserNotification>) ObjectSerializer
						.deserialize(mAppPref
								.getString(ConstantUtils.NOTIFICATION, ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
			notificationListAdapter = new NotificationListAdapter(mContext,
					mNotificationList);
			notificationListAdapter.notifyDataSetChanged();
			notificationList.setAdapter(notificationListAdapter);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// notificationManager.cancel(ConstantUtils.NOTIFI_ID);
			notificationManager.cancelAll();

		}
	}
}
