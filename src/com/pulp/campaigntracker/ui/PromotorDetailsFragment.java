package com.pulp.campaigntracker.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserNotification;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.PromotorNotificationListAdapter;
import com.pulp.campaigntracker.controllers.UserFormAdapter;
import com.pulp.campaigntracker.listeners.UserNotificationsRecieved;
import com.pulp.campaigntracker.parser.JsonGetUserNotification;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;

public class PromotorDetailsFragment extends Fragment implements
		OnClickListener, UserNotificationsRecieved {

	private static final String TAG = PromotorDetailsFragment.class
			.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	TextView nameLogo;
	TextView addressHeadingTitle;
	TextView addressLines;
	TextView contactNo;
	TextView fillReport;
	TextView sendText;
	ListView promotorNotificationList;
	LinearLayout promotorDetails;
	PromotorNotificationListAdapter promotorNotificationListAdapter;
	private UserProfile userDetails;
	private Typeface icomoon;
	private TextView callIcon;
	private TextView fillReportIcon;
	private TextView messageIcon;
	private StoreDetails mStoreDetails;
	private Intent callIntent;
	private Intent messageIntent;
	private ArrayList<UserFormDetails> mUserForm;
	private UserFormAdapter userFormListAdapter;
	private ListView userForm;
	private View buttonLayoutView;
	private TextView uploadFormIcon;
	private TextView uploadForm;
	private Button submitButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity().getBaseContext();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			mStoreDetails = mBundle.getParcelable(ConstantUtils.STORE_DETAILS);
			userDetails = mBundle.getParcelable(ConstantUtils.USER_DETAILS);
			mUserForm = mBundle
					.getParcelableArrayList(ConstantUtils.USER_FORM_LIST);
			setActionBarTitle();

		}

		View view = inflater.inflate(R.layout.promotor_details_screen,
				container, false);
		promotorDetails = (LinearLayout) view
				.findViewById(R.id.promotorDetails);
		addressHeadingTitle = (TextView) view
				.findViewById(R.id.addressHeadingTitle);
		addressLines = (TextView) view.findViewById(R.id.addressLines);
		contactNo = (TextView) view.findViewById(R.id.contactNo);
		fillReport = (TextView) view.findViewById(R.id.fillReport);
		sendText = (TextView) view.findViewById(R.id.sendText);
		callIcon = (TextView) view.findViewById(R.id.callIcon);
		fillReportIcon = (TextView) view.findViewById(R.id.fillReportIcon);
		messageIcon = (TextView) view.findViewById(R.id.messageIcon);
		nameLogo = (TextView) promotorDetails.findViewById(R.id.nameLogo);
		promotorNotificationList = (ListView) view
				.findViewById(R.id.promotorNotificationList);
		buttonLayoutView = inflater.inflate(R.layout.user_form_button_layout,
				null);
		userForm = (ListView) view.findViewById(R.id.userForm);

		uploadFormIcon = (TextView) buttonLayoutView
				.findViewById(R.id.uploadFormIcon);
		contactNo.setText(userDetails.getContactNumber());
		uploadForm = (TextView) buttonLayoutView.findViewById(R.id.uploadForm);
		submitButton = (Button) buttonLayoutView
				.findViewById(R.id.submitFormButton);
		addressLines.setText(mStoreDetails.getName());

		executeQuery();

		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");
		callIcon.setTypeface(icomoon);
		fillReportIcon.setTypeface(icomoon);
		messageIcon.setTypeface(icomoon);
		nameLogo.setTypeface(icomoon);
		uploadFormIcon.setTypeface(icomoon);

		fillReportIcon.setOnClickListener(this);
		messageIcon.setOnClickListener(this);
		callIcon.setOnClickListener(this);
		sendText.setOnClickListener(this);

		if (userFormListAdapter == null)
			userFormListAdapter = new UserFormAdapter(mContext, mUserForm);
		userFormListAdapter.notifyDataSetChanged();
		if (userForm.getFooterViewsCount() == 0) {
			userForm.addFooterView(buttonLayoutView);
		}
		userForm.setAdapter(userFormListAdapter);
		userForm.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.messageIcon:
			messageIntent = new Intent(Intent.ACTION_VIEW);
			messageIntent.setData(Uri.parse("smsto:" + contactNo.getText()));
			messageIntent.putExtra("sms_body", "Please Contact Me ASAP");
			messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(messageIntent);
			break;

		case R.id.callIcon:
			callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ contactNo.getText()));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(callIntent);
			break;
		case R.id.fillReportIcon:
			ConstantUtils.formButtonClicked = true;
			promotorNotificationList.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.VISIBLE);
			break;

		case R.id.sendText:

			messageIntent = new Intent(Intent.ACTION_VIEW);
			messageIntent.setData(Uri.parse("smsto:" + contactNo.getText()));
			messageIntent.putExtra("sms_body", "Please Contact Me ASAP");
			messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(messageIntent);
			break;

		case R.id.contactNo:
			callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ contactNo.getText()));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(callIntent);
			break;

		default:
			break;
		}
	}

	public boolean onBack() {
		if (ConstantUtils.formButtonClicked) {
			userForm.setVisibility(View.INVISIBLE);
			promotorNotificationList.setVisibility(View.VISIBLE);
		}
		boolean temp = ConstantUtils.formButtonClicked;

		ConstantUtils.formButtonClicked = false;
		return temp;

	}

	@Override
	public void onRecievedNotifcationList(
			List<UserNotification> notificationList) {

		if (notificationList != null && notificationList.size() > 0) {
			if (promotorNotificationListAdapter == null)
				promotorNotificationListAdapter = new PromotorNotificationListAdapter(
						mContext, notificationList);

			promotorNotificationListAdapter.notifyDataSetChanged();
			promotorNotificationList
					.setAdapter(promotorNotificationListAdapter);
		}
	}

	public void executeQuery() {
		JsonGetUserNotification jsonGetUserNotification = new JsonGetUserNotification();
		jsonGetUserNotification.getCampaignDetailsFromURL(
				ConstantUtils.USER_NOTIFICATION_URL, this, mContext);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SupportMapFragment f = (SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.map);
		if (f != null)
			getFragmentManager().beginTransaction().remove(f).commit();
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle(userDetails.getName());
		}
	}

}
