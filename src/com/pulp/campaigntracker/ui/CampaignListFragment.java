package com.pulp.campaigntracker.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.AllPromoterData;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.FetchData;
import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.CampaignListAdapter;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.PromotorDetailsRecieved;
import com.pulp.campaigntracker.parser.JsonGetPromotorDetails;
import com.pulp.campaigntracker.utils.ConstantUtils;

public class CampaignListFragment extends android.support.v4.app.Fragment
		implements android.widget.AdapterView.OnItemClickListener,
		PromotorDetailsRecieved, OnClickListener {

	private Activity mActivity;
	private Context mContext;
	private ListView campaignList;
	CampaignListAdapter campaignListAdapter;
	private ArrayList<CampaignDetails> campaignDetailsList;
	ArrayList<UserProfile> promotorList = new ArrayList<UserProfile>();
	private SinglePromotorData mSinglePromotorData;
	private RelativeLayout errorLayout;
	private TextView errorImage;
	private Button retryButton;
	private View view;
	private Typeface iconFonts;
	private CampaignDetails mCampaignDetails;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
		mContext = getActivity().getBaseContext();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.campaign_screen, container, false);

		iconFonts = Typeface.createFromAsset(mContext.getAssets(),
				"icomoon.ttf");

		campaignList = (ListView) view.findViewById(R.id.campaignList);

		campaignList.setOnItemClickListener(this);

		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			campaignDetailsList = mBundle
					.getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);

			// mActivity.getActionBar().setTitle(ConstantUtils.CAMPAIGN_DISPLAY_NAME);
			setActionBarTitle();

		}

		if (campaignListAdapter == null)
			campaignListAdapter = new CampaignListAdapter(mContext,
					campaignDetailsList);
		else
			campaignListAdapter.notifyDataSetChanged();

		campaignList.setAdapter(campaignListAdapter);
		setHasOptionsMenu(true);

		errorLayout = (RelativeLayout) view
				.findViewById(R.id.campaignListerrorLayout);
		errorImage = (TextView) errorLayout.findViewById(R.id.errorImage);
		retryButton = (Button) errorLayout.findViewById(R.id.retryButton);
		retryButton.setOnClickListener(this);
		errorImage.setTypeface(iconFonts);

		if (!HTTPConnectionWrapper.isNetworkAvailable(mContext))
			errorLayout.setVisibility(View.VISIBLE);
		else
			errorLayout.setVisibility(View.INVISIBLE);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		switch (arg0.getId()) {
		case R.id.campaignList:
			mCampaignDetails = campaignDetailsList.get(arg2);
			StoreListFragment sf = new StoreListFragment();
			Bundle mBundle = new Bundle();
			mBundle.putParcelable(ConstantUtils.CAMPAIGN_DETAILS,
					campaignDetailsList.get(arg2));
			mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
					campaignDetailsList);
			sf.setArguments(mBundle);
			((SupervisorMotherActivity) mActivity).onItemSelected(sf, true);

			break;

		default:
			break;
		}
	}

	public void executeQuery() {
		// promotorListProgressBar.setVisibility(View.VISIBLE);

		JsonGetPromotorDetails jsonGetPromotorDetails = new JsonGetPromotorDetails();
		StringBuilder url = new StringBuilder();
		url.append(ConstantUtils.USER_DETAILS_URL);
		// url.append(mCampaignDetails.getId());
		jsonGetPromotorDetails.getPromotorDetailsFromURL(url.toString(), this,
				mContext, "", "", ConstantUtils.START_COUNT,
				ConstantUtils.NUMBER);

	}

	@Override
	public void onPromotorDetailsRecieved(SinglePromotorData mSinglePromotorData) {
		this.mSinglePromotorData = mSinglePromotorData;

		ArrayList<UserFormDetails> userFormList = new ArrayList<UserFormDetails>();
		if (mSinglePromotorData.getPersonalDetails() != null
				&& mSinglePromotorData.getPersonalDetails().size() > 0) {
			// promotorListProgressBar.setVisibility(View.GONE);
			// manageFetchUsers(mSinglePromotorData.getPersonalDetails());
//
//			for (int i = 0; i < mCampaignDetails.getUserFormDetailsList()
//					.size(); i++) {
//				userFormList.add(mCampaignDetails.getUserFormDetailsList().get(
//						i));
//			}

			AllPromotorListFragment allPromotorListFragment = new AllPromotorListFragment();
			Bundle mBundle = new Bundle();
			mBundle.putParcelableArrayList(ConstantUtils.PROMOTOR_LIST,
					mSinglePromotorData.getPersonalDetails());
			mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
					campaignDetailsList);
//			mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
//					userFormList);
			allPromotorListFragment.setArguments(mBundle);
			ConstantUtils.ReferList = true;
			((SupervisorMotherActivity) mActivity).onItemSelected(
					allPromotorListFragment, true);

		}
	}

	private void manageFetchUsers(ArrayList<UserProfile> personalDetails) {
		FetchData fetchData = new FetchData();
		AllPromoterData allPromoterData = new AllPromoterData();
		if (fetchData.getLast_count() == Integer.valueOf(ConstantUtils.NUMBER)) {
			allPromoterData.setCurrentSet(mSinglePromotorData
					.getPersonalDetails());
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.supervisor_action_bar, menu);

	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle(
					ConstantUtils.CAMPAIGN_DISPLAY_NAME);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retryButton:
			executeQuery();
			// view.requestLayout();
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_all_promotors:
			executeQuery();
			break;

		case R.id.notifications:
			NotificationListFragment notificationListFragment = new NotificationListFragment();
			((SupervisorMotherActivity) mActivity).onItemSelected(
					notificationListFragment, true);

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
