package com.pulp.campaigntracker.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserNotification;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.controllers.PromoterTimeLineListAdapter;
import com.pulp.campaigntracker.controllers.UserFormAdapter;
import com.pulp.campaigntracker.listeners.UserNotificationsRecieved;
import com.pulp.campaigntracker.parser.JsonGetUserNotification;
import com.pulp.campaigntracker.parser.JsonSendFormFillDetails;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;

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
	ListView promotorTimeLineList;
	RelativeLayout promotorDetails;
	PromoterTimeLineListAdapter promoterTimeLineListAdapter;
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
	private TextView contactIcon;
	private TextView contactText;
	private TextView campaignDescIcon;
	private TextView campaignDescText;
	private TextView timeLineIcon;
	private TextView timeLineText;
	private ScrollView promoterCampaignDescription;
	private TextView campaignName;
	private TextView campaignDateEnd;
	private TextView campaignDateStart;
	private TextView storeName;
	private TextView storeDateEnd;
	private TextView storeDateStart;
	private TextView rightArrow;
	private ScrollView promoterContactLayout;
	private TextView callSupervisorIcon;
	private TextView callSupervisorNumber;
	private TextView msgSupervisorIcon;
	private TextView msgSupervisorNumber;
	private TextView storerightArrow;
	private TextView callrightArrow;
	private TextView msgrightArrow;
	private RelativeLayout callLayout;
	private RelativeLayout messageLayout;
	private View activeCircle;
	private View storeactiveCircle;
	private RelativeLayout campaignLayout;
	private RelativeLayout storeView;
	private ArrayList<CampaignDetails> campaignDetailsList;
	private TextView storeText;
	private TextView campaignText;
	// private Object campaignDisplayName;
	private String campaignDisplayName;
	private String assignCampaignName;
	private String assignStoreName;
	private ArrayList<UserProfile> mPromotorList;
	private ArrayList<StoreDetails> storeDetailsList;
	private ArrayList<UserProfile> mInactivePromotorList;
	private boolean formButtonClicked;
	private static final String KEY_CAMPAIGN_ID="campaign_id";
	private static final String KEY_STORE_ID="store_id";
	private static final String KEY_SUPERVISOR_ID="supervisor_id";
	private static final String KEY_PRAMOTOR_ID="pramotor_id";
	private static final String KEY_FORM_DATA="form_data";
	private static final String KEY_FIELD_ID="field_id";
	private static final String KEY_FIELD_VALUE="field_value";
	private static final String KEY_TIME="time";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity().getApplicationContext();

	}

	@SuppressWarnings("deprecation")
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
			campaignDetailsList = mBundle
					.getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);
			campaignDisplayName = mBundle
					.getString(ConstantUtils.CAMPAIGN_NAME);

			assignCampaignName = mBundle
					.getString(ConstantUtils.ASSIGN_CAMPAIGN_NAME);

			assignStoreName = mBundle
					.getString(ConstantUtils.ASSIGN_STORE_NAME);

			setActionBarTitle();

		}

		View view = inflater.inflate(R.layout.promotor_details_screen,
				container, false);
		promotorDetails = (RelativeLayout) view
				.findViewById(R.id.promotorDetails);
		setHasOptionsMenu(true);

		promoterCampaignDescription = (ScrollView) view
				.findViewById(R.id.promoterCampaign);

		promoterContactLayout = (ScrollView) view
				.findViewById(R.id.promoterContactLayout);
		addressHeadingTitle = (TextView) view
				.findViewById(R.id.addressHeadingTitle);
		addressLines = (TextView) view.findViewById(R.id.addressLines);

		fillReportIcon = (TextView) view.findViewById(R.id.fillReportIcon);
		fillReport = (TextView) view.findViewById(R.id.fillReportText);

		timeLineIcon = (TextView) view.findViewById(R.id.timeLineIcon);
		timeLineText = (TextView) view.findViewById(R.id.timeLineText);

		campaignDescIcon = (TextView) view.findViewById(R.id.campaignDescIcon);
		campaignDescText = (TextView) view.findViewById(R.id.campaignDescText);

		contactIcon = (TextView) view.findViewById(R.id.contactIcon);
		contactText = (TextView) view.findViewById(R.id.contactText);

		nameLogo = (TextView) promotorDetails.findViewById(R.id.nameLogo);
		promotorTimeLineList = (ListView) view
				.findViewById(R.id.promoterTimeLineList);

		campaignName = (TextView) promoterCampaignDescription
				.findViewById(R.id.campaignName);

		campaignLayout = (RelativeLayout) promoterCampaignDescription
				.findViewById(R.id.campaignLayout);
		storeView = (RelativeLayout) promoterCampaignDescription
				.findViewById(R.id.storeView);

		campaignDateEnd = (TextView) promoterCampaignDescription
				.findViewById(R.id.campaignDateEnd);

		campaignDateStart = (TextView) promoterCampaignDescription
				.findViewById(R.id.campaignDateStart);
		storeText = (TextView) promoterCampaignDescription
				.findViewById(R.id.storeText);
		campaignText = (TextView) promoterCampaignDescription
				.findViewById(R.id.campainText);
		storeName = (TextView) promoterCampaignDescription
				.findViewById(R.id.storeName);

		storeDateEnd = (TextView) promoterCampaignDescription
				.findViewById(R.id.storeDateEnd);

		storeDateStart = (TextView) promoterCampaignDescription
				.findViewById(R.id.storeDateStart);

		rightArrow = (TextView) promoterCampaignDescription
				.findViewById(R.id.rightArrow);

		activeCircle = (View) promoterCampaignDescription
				.findViewById(R.id.activeCircle);
		storeactiveCircle = (View) promoterCampaignDescription
				.findViewById(R.id.storeactiveCircle);

		storerightArrow = (TextView) promoterCampaignDescription
				.findViewById(R.id.storerightArrow);

		callLayout = (RelativeLayout) promoterContactLayout
				.findViewById(R.id.callSupervisorLayout);
		messageLayout = (RelativeLayout) promoterContactLayout
				.findViewById(R.id.msgSupervisorLayout);

		callSupervisorIcon = (TextView) promoterContactLayout
				.findViewById(R.id.supervisorCallIcon);
		callSupervisorNumber = (TextView) promoterContactLayout
				.findViewById(R.id.callSupervisorNumber);
		msgSupervisorIcon = (TextView) promoterContactLayout
				.findViewById(R.id.msgSupervisorIcon);

		msgSupervisorNumber = (TextView) promoterContactLayout
				.findViewById(R.id.msgSupervisorNumber);
		callrightArrow = (TextView) promoterContactLayout
				.findViewById(R.id.callrightArrow);
		msgrightArrow = (TextView) promoterContactLayout
				.findViewById(R.id.msgrightArrow);

		buttonLayoutView = inflater.inflate(R.layout.user_form_button_layout,
				null);
		userForm = (ListView) view.findViewById(R.id.userForm);

		uploadFormIcon = (TextView) buttonLayoutView
				.findViewById(R.id.uploadFormIcon);
		uploadForm = (TextView) buttonLayoutView.findViewById(R.id.uploadForm);
		submitButton = (Button) buttonLayoutView
				.findViewById(R.id.submitFormButton);
		addressLines.setText(userDetails.getAddress());

		executeQuery();

		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");
		contactIcon.setTypeface(icomoon);
		fillReportIcon.setTypeface(icomoon);
		timeLineIcon.setTypeface(icomoon);
		nameLogo.setTypeface(icomoon);
		campaignDescIcon.setTypeface(icomoon);
		uploadFormIcon.setTypeface(icomoon);
		rightArrow.setTypeface(icomoon);
		storerightArrow.setTypeface(icomoon);
		callSupervisorIcon.setTypeface(icomoon);
		msgSupervisorIcon.setTypeface(icomoon);
		callrightArrow.setTypeface(icomoon);
		msgrightArrow.setTypeface(icomoon);
		
		storeDateStart.setText(userDetails.getStore_end_date());
		storeDateEnd.setText(userDetails.getStore_end_date());
		campaignDateStart.setText(userDetails.getCampaign_start_date());
		campaignDateEnd.setText(userDetails.getCampaign_end_date());


		if (userDetails.getStatus().equals("active")) {
			setStatusBackground(R.drawable.circle_active);

		} else {
			setStatusBackground(R.drawable.circle_inactive);
			timeLineIcon.setTextColor(mContext.getResources().getColor(
					R.color.GreyLineColor));
			fillReportIcon.setTextColor(mContext.getResources().getColor(
					R.color.GreyLineColor));
			campaignDescIcon.setTextColor(mContext.getResources().getColor(
					R.color.red));

		}

		
		promoterCampaignDescription.setVisibility(View.INVISIBLE);

		

		
		promoterContactLayout.setVisibility(View.INVISIBLE);
		if (mUserForm != null) {
			if (userFormListAdapter == null)
				userFormListAdapter = new UserFormAdapter(mContext, mUserForm);
			userFormListAdapter.notifyDataSetChanged();
			if (userForm.getFooterViewsCount() == 0) {
				userForm.addFooterView(buttonLayoutView);
			}
			userForm.setAdapter(userFormListAdapter);
		}
		
		if (userDetails.getStatus().equals("inactive")) {
			promoterCampaignDescription.setVisibility(View.VISIBLE);
			promotorTimeLineList.setVisibility(View.INVISIBLE);
		} else {
			promoterCampaignDescription.setVisibility(View.INVISIBLE);
			promotorTimeLineList.setVisibility(View.VISIBLE);
		}

		assignCampaignName = UtilityMethods.getAppPreferences(mContext)
				.getString(ConstantUtils.ASSIGN_CAMAIGN,
						userDetails.getCurrentCampagin());

		if (assignCampaignName != null)
			campaignName.setText(assignCampaignName);
		else {
			if (userDetails.getCurrentCampagin().equals(""))
				campaignText.setText("Assign to Campaign");
			else
				campaignName.setText(userDetails.getCurrentCampagin());
		}

		if (!(campaignName.equals("None")) && campaignName != null) {
			if (assignStoreName != null)
				storeName.setText(assignStoreName);
			else {

				if (userDetails.getCurrentStore().equals(""))
					storeText.setText("Assign to Store");
				else
					storeName.setText(userDetails.getCurrentStore());
			}
		}
		promoterContactLayout.setVisibility(View.INVISIBLE);
		userForm.setVisibility(View.INVISIBLE);

		fillReportIcon.setOnClickListener(this);
		timeLineIcon.setOnClickListener(this);
		campaignDescIcon.setOnClickListener(this);
		contactIcon.setOnClickListener(this);
		callLayout.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
		campaignLayout.setOnClickListener(this);
		storeView.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.contactIcon:
			promoterContactLayout.setVisibility(View.VISIBLE);
			promoterCampaignDescription.setVisibility(View.INVISIBLE);
			promotorTimeLineList.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			if (userDetails.getStatus().equals("inactive")) {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));
				timeLineIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));
			} else {
				campaignDescIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				timeLineIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				contactIcon.setTextColor(mContext.getResources().getColor(
						R.color.red));
			}
			break;

		case R.id.timeLineIcon:

			if (userDetails.getStatus().equals("inactive")) {
				Toast.makeText(mContext, "Inactive User", Toast.LENGTH_LONG)
						.show();
			} else {
				promoterContactLayout.setVisibility(View.INVISIBLE);
				promoterCampaignDescription.setVisibility(View.INVISIBLE);
				userForm.setVisibility(View.INVISIBLE);
				promotorTimeLineList.setVisibility(View.VISIBLE);
				campaignDescIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				timeLineIcon.setTextColor(mContext.getResources().getColor(
						R.color.red));
				contactIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
			}
			break;
		case R.id.fillReportIcon:
			if (userDetails.getStatus().equals("inactive")) {
				Toast.makeText(mContext, "Inactive User", Toast.LENGTH_LONG)
						.show();
			} else {
				promoterContactLayout.setVisibility(View.INVISIBLE);
				formButtonClicked = true;
				promoterCampaignDescription.setVisibility(View.INVISIBLE);
				promotorTimeLineList.setVisibility(View.INVISIBLE);
				userForm.setVisibility(View.VISIBLE);
				campaignDescIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.red));
				timeLineIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				contactIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				JSONObject formDataToSend =	getJsonForFormDetails();
		      	
		      	JsonSendFormFillDetails formFillDetails =new JsonSendFormFillDetails();
		      	formFillDetails.sendFormFillupDetails(getActivity(), formDataToSend, false);
			}
			break;

		case R.id.campaignDescIcon:
			promoterContactLayout.setVisibility(View.INVISIBLE);
			promoterCampaignDescription.setVisibility(View.VISIBLE);
			promotorTimeLineList.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);

			if (userDetails.getStatus().equals("inactive")) {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));
				timeLineIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));
				campaignText.setText("Assign To Campaign");
				storeText.setText("Assign To Store");
			} else {
				campaignDescIcon.setTextColor(mContext.getResources().getColor(
						R.color.red));
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				timeLineIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				contactIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				campaignText.setText("Campaign");
				storeText.setText("Store");
			}
			break;

		case R.id.callSupervisorLayout:
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ "9999999999"));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(callIntent);
			break;
		case R.id.msgSupervisorLayout:

			Intent messageIntent;
			messageIntent = new Intent(Intent.ACTION_VIEW);
			messageIntent.setData(Uri.parse("smsto:" + "999999999"));
			messageIntent.putExtra("sms_body", "Please Contact Me ASAP");
			messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(messageIntent);
			break;

		case R.id.campaignLayout:
			try {
				AssignCampaignFragment assignCampaignFragment = new AssignCampaignFragment();
				Bundle mBundle = new Bundle();

				mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
						campaignDetailsList);
				mBundle.putParcelable(ConstantUtils.STORE_DETAILS,
						mStoreDetails);
				mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
						mUserForm);
				mBundle.putParcelable(ConstantUtils.USER_DETAILS, userDetails);

				mBundle.putString(ConstantUtils.CAMPAIGN_NAME,
						campaignDisplayName);
				if (assignStoreName != null)
					mBundle.putString(ConstantUtils.ASSIGN_STORE_NAME,
							assignStoreName);

				assignCampaignFragment.setArguments(mBundle);
				((UserMotherActivity) mActivity).onItemSelected(
						assignCampaignFragment, true);

			} catch (Exception e) {
				Toast.makeText((UserMotherActivity) mActivity,
						"ArrayOutOfBounds", Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.storeView:
			AssignStoreFragment assignStoreFragment = new AssignStoreFragment();

			Bundle mBundle = new Bundle();

			// mBundle.putParcelableArrayList(ConstantUtils.STORE_LIST,
			// storeDetailsList);
			for (int i = 0; i < campaignDetailsList.size(); i++) {
				if (campaignDisplayName.equals(campaignDetailsList.get(i)
						.getName())) {
					mBundle.putParcelable(ConstantUtils.CAMPAIGN_DETAILS,
							campaignDetailsList.get(i));
				}

			}
			mBundle.putParcelable(ConstantUtils.USER_DETAILS, userDetails);
			mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
					campaignDetailsList);
			mBundle.putString(ConstantUtils.CAMPAIGN_NAME, campaignDisplayName);
			if (assignCampaignName != null)
				mBundle.putString(ConstantUtils.ASSIGN_CAMPAIGN_NAME,
						assignCampaignName);
			assignStoreFragment.setArguments(mBundle);
			((UserMotherActivity) mActivity).onItemSelected(
					assignStoreFragment, true);

			break;
		default:
			break;
		}
	}

	public boolean onBack() {
		
		boolean temp = formButtonClicked;
		formButtonClicked = false;
		return temp;

	}

	@Override
	public void onRecievedNotifcationList(
			List<UserNotification> notificationList) {

		if (notificationList != null && notificationList.size() > 0) {
			if (promoterTimeLineListAdapter == null)
				promoterTimeLineListAdapter = new PromoterTimeLineListAdapter(
						mContext, notificationList);

			promoterTimeLineListAdapter.notifyDataSetChanged();
			promotorTimeLineList.setAdapter(promoterTimeLineListAdapter);
		}
	}

	
	private JSONObject getJsonForFormDetails() {
		
		JSONObject parentJson =new JSONObject();
		try {
			Calendar calendar = Calendar.getInstance();
			String currentDate = new SimpleDateFormat("yyyy-MM-dd ",Locale.getDefault()).format(calendar.getTime());
			String currentTime = new SimpleDateFormat("hh:mm:ss",Locale.getDefault()).format(calendar.getTime());

			
			for (int i = 0; i < campaignDetailsList.size(); i++) {
				if (campaignDisplayName.equals(campaignDetailsList.get(i)
						.getName())) {
					
					parentJson.put(KEY_CAMPAIGN_ID, campaignDetailsList.get(i).getId());
					break;
				}

			}
			
			parentJson.put(KEY_STORE_ID, mStoreDetails.getId());
			parentJson.put(KEY_TIME,currentDate + currentTime);
			if(LoginData.getInstance().getRole().equals("supervisor")){
				parentJson.put(KEY_SUPERVISOR_ID,LoginData.getInstance().getId());
				parentJson.put(KEY_PRAMOTOR_ID, userDetails.getUid());
			}
			
			
			
			JSONArray formDataJsonArray =new JSONArray();
			
			int numberOfFeilds = userFormListAdapter.getCount();
			for (int i = 0; i < numberOfFeilds; i++) {
				String Fvalue = userFormListAdapter.getItem(i).getFieldValue();
                String FId =userFormListAdapter.getItem(i).getFieldId();
                
                if(Fvalue==null){
                	if(Fvalue.trim().length()==0){
                		Toast.makeText(getActivity(), "please fill values", 5).show();
                		break;
                	}
                }
                JSONObject formValue =new JSONObject();
				formValue.put(KEY_FIELD_ID, FId);
				formValue.put(KEY_FIELD_VALUE, Fvalue);
				formDataJsonArray.put(formValue);

			}
			parentJson.put(KEY_FORM_DATA, formDataJsonArray);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return parentJson;
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

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setStatusBackground(int resID) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			activeCircle.setBackground(mContext.getResources().getDrawable(
					resID));
			storeactiveCircle.setBackground(mContext.getResources()
					.getDrawable(resID));
		} else {
			activeCircle.setBackgroundDrawable(mContext.getResources()
					.getDrawable(resID));
			storeactiveCircle.setBackgroundDrawable(mContext.getResources()
					.getDrawable(resID));
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.promoter_action_bar, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.notifications:
			NotificationListFragment notificationListFragment = new NotificationListFragment();
			((UserMotherActivity) mActivity).onItemSelected(
					notificationListFragment, true);

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
