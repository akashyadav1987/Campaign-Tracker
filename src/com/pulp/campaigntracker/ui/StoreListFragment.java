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
import android.os.Parcelable;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.AllPromoterData;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.FetchData;
import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.CampaignListAdapter;
import com.pulp.campaigntracker.controllers.PromotorListAdapter;
import com.pulp.campaigntracker.controllers.StoreDetailsAdapter;
import com.pulp.campaigntracker.listeners.CampaignDetailsRecieved;
import com.pulp.campaigntracker.listeners.PromotorDetailsRecieved;
import com.pulp.campaigntracker.parser.JsonGetCampaignDetails;
import com.pulp.campaigntracker.parser.JsonGetPromotorDetails;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;
import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.TLog;

public class StoreListFragment extends android.support.v4.app.Fragment
		implements android.widget.AdapterView.OnItemClickListener,
		PromotorDetailsRecieved, OnClickListener {

	private static final String TAG = StoreListFragment.class.getSimpleName();
	private Activity mActivity;
	private Context mContext;
	private ListView storeList;
	StoreDetailsAdapter storeDetailsListAdapter;
	private CampaignDetails mCampaignDetails;

	private List<CampaignDetails> campaignDetailsList;
	ArrayList<UserProfile> promotorList = new ArrayList<UserProfile>();
	private ProgressBar promotorListProgressBar;
	private Object promotorListAdapter;
	private String storeId;
	private SinglePromotorData mSinglePromotorData;
	private RelativeLayout errorLayout;
	private TextView errorImage;
	private Button retryButton;
	private Typeface iconFonts;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
		mContext = getActivity().getBaseContext();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.store_list_screen, container, false);
		iconFonts = Typeface.createFromAsset(mContext.getAssets(),
				"icomoon.ttf");
		promotorListProgressBar = (ProgressBar) view
				.findViewById(R.id.promotorListProgressBar);
		promotorListProgressBar.setVisibility(View.GONE);

		setHasOptionsMenu(true);

		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			mCampaignDetails = mBundle
					.getParcelable(ConstantUtils.CAMPAIGN_DETAILS);

			setActionBarTitle();

		}
		storeList = (ListView) view.findViewById(R.id.storeList);
		storeList.setOnItemClickListener(this);

		if (storeDetailsListAdapter == null)
			storeDetailsListAdapter = new StoreDetailsAdapter(mContext,
					mCampaignDetails.getStoreList());
		storeDetailsListAdapter.notifyDataSetChanged();
		storeList.setAdapter(storeDetailsListAdapter);

		errorLayout = (RelativeLayout) view
				.findViewById(R.id.storeListerrorLayout);
		errorImage = (TextView) errorLayout.findViewById(R.id.errorImage);
		retryButton = (Button) errorLayout.findViewById(R.id.retryButton);
		retryButton.setOnClickListener(this);
		errorImage.setTypeface(iconFonts);

		if (!UtilityMethods.isNetworkAvailable(mContext))
			errorLayout.setVisibility(View.VISIBLE);
		else
			errorLayout.setVisibility(View.INVISIBLE);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		switch (arg0.getId()) {
		case R.id.storeList:

			StoreFragment sf = new StoreFragment();
			Bundle mBundle = new Bundle();
			ArrayList<UserProfile> promotorList = new ArrayList<UserProfile>();
			ArrayList<UserFormDetails> userFormList = new ArrayList<UserFormDetails>();
			storeId = mCampaignDetails.getStoreList().get(arg2).getId();

			// Get Store Specific Form And Promoter List and put in Bundle

			for (int i = 0; i < mCampaignDetails.getUserList().size(); i++) {
				if (mCampaignDetails.getUserList().get(i).getStoreId() != null
						&& mCampaignDetails.getUserList().get(i).getStoreId()
								.equals(storeId)) {
					promotorList.add(mCampaignDetails.getUserList().get(i));
				}
			}

			for (int i = 0; i < mCampaignDetails.getUserFormDetailsList()
					.size(); i++) {
				userFormList.add(mCampaignDetails.getUserFormDetailsList().get(
						i));
			}

			mBundle.putParcelable(ConstantUtils.STORE_DETAILS, mCampaignDetails
					.getStoreList().get(arg2));
			mBundle.putParcelableArrayList(ConstantUtils.PROMOTOR_LIST,
					promotorList);
			mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
					userFormList);

			sf.setArguments(mBundle);
			((SupervisorMotherActivity) mActivity).onItemSelected(sf, true);

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_all_promotors:
			executeQuery();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void executeQuery() {
		promotorListProgressBar.setVisibility(View.VISIBLE);

		JsonGetPromotorDetails jsonGetPromotorDetails = new JsonGetPromotorDetails();
		StringBuilder url = new StringBuilder();
		url.append(ConstantUtils.USER_DETAILS_URL);
		url.append(mCampaignDetails.getId());
		jsonGetPromotorDetails.getPromotorDetailsFromURL(url.toString(), this,
				mContext, mCampaignDetails.getId(), "",
				ConstantUtils.START_COUNT, ConstantUtils.NUMBER);

	}

	@Override
	public void onPromotorDetailsRecieved(SinglePromotorData mSinglePromotorData) {
		this.mSinglePromotorData = mSinglePromotorData;
		if (mSinglePromotorData.getPersonalDetails() != null
				&& mSinglePromotorData.getPersonalDetails().size() > 0) {
			promotorListProgressBar.setVisibility(View.GONE);
			// manageFetchUsers(mSinglePromotorData.getPersonalDetails());

			AllPromotorListFragment allPromotorListFragment = new AllPromotorListFragment();
			Bundle mBundle = new Bundle();
			mBundle.putParcelableArrayList(ConstantUtils.PROMOTOR_LIST,
					mSinglePromotorData.getPersonalDetails());
			allPromotorListFragment.setArguments(mBundle);
			ConstantUtils.ReferList = false;

			((SupervisorMotherActivity) mActivity).onItemSelected(
					allPromotorListFragment, true);

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.supervisor_action_bar, menu);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle(mCampaignDetails.getName());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retryButton:
			executeQuery();
		//	view.requestLayout();
			break;

		default:
			break;
		}

	}
}
