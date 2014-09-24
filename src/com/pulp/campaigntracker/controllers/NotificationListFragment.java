package com.pulp.campaigntracker.controllers;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserNotification;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.NotificationListAdapter;
import com.pulp.campaigntracker.controllers.PromotorListAdapter;
import com.pulp.campaigntracker.ui.AllPromotorListFragment;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ObjectSerializer;
import com.pulp.campaigntracker.utils.TLog;

public class NotificationListFragment extends Fragment {

	private static final String TAG = AllPromotorListFragment.class
			.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	private ArrayList<UserNotification> mNotificationList;
	private PromotorListAdapter promotorListAdapter;

	private NotificationListAdapter notificationListAdapter;
	private ListView notificationList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity().getBaseContext();

	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.notification_list, container,
				false);
		setActionBarTitle();
		notificationList = (ListView) view.findViewById(R.id.notificationList);
		try {

			mNotificationList = (ArrayList<UserNotification>) ObjectSerializer
					.deserialize(mActivity
							.getApplicationContext()
							.getSharedPreferences(
									ConstantUtils.NOTIFICATION_CACHE, 0)
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

}
