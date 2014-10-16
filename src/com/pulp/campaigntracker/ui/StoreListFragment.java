package com.pulp.campaigntracker.ui;

import java.util.ArrayList;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.controllers.StoreDetailsAdapter;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.UserDetailsRecieved;
import com.pulp.campaigntracker.parser.JsonGetPromotorDetails;
import com.pulp.campaigntracker.utils.ConstantUtils;

public class StoreListFragment extends android.support.v4.app.Fragment
		implements android.widget.AdapterView.OnItemClickListener,
		UserDetailsRecieved, OnClickListener {

	private static final String TAG = StoreListFragment.class.getSimpleName();
	private Activity mActivity;
	private Context mContext;
	private ListView storeList;
	StoreDetailsAdapter storeDetailsListAdapter;
	private CampaignDetails mCampaignDetails;

	ArrayList<UserProfile> promotorList = new ArrayList<UserProfile>();
	private ProgressBar promotorListProgressBar;
	private String storeId;
	private RelativeLayout errorLayout;
	private TextView errorImage;
	private Button retryButton;
	private Typeface iconFonts;
	private View view;
	private ArrayList<CampaignDetails> campaignDetailsList;
	private ArrayList<StoreDetails> storeDetailsList;
	private SinglePromotorData mSinglePromotorData;

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
			campaignDetailsList = mBundle
					.getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);
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

		if (!HTTPConnectionWrapper.isNetworkAvailable(mContext))
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

			// for (int i = 0; i < mCampaignDetails.getUserList().size(); i++) {
			// if (mCampaignDetails.getUserList().get(i).getStoreId() != null
			// && mCampaignDetails.getUserList().get(i).getStoreId()
			// .equals(storeId)) {
			// promotorList.add(mCampaignDetails.getUserList().get(i));
			// }
			// }

			for (int i = 0; i < mCampaignDetails.getUserFormDetailsList()
					.size(); i++) {
				userFormList.add(mCampaignDetails.getUserFormDetailsList().get(
						i));
			}

			mBundle.putParcelable(ConstantUtils.STORE_DETAILS, mCampaignDetails
					.getStoreList().get(arg2));
			// mBundle.putParcelableArrayList(ConstantUtils.PROMOTOR_LIST,
			// promotorList);
			mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
					userFormList);
			mBundle.putParcelable(ConstantUtils.CAMPAIGN_DETAILS,
					mCampaignDetails);
			mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
					campaignDetailsList);
			sf.setArguments(mBundle);
			((UserMotherActivity) mActivity).onItemSelected(sf, true);

			break;

		default:
			break;
		}
	}

	public void executeQuery(int start) {
		// promotorListProgressBar.setVisibility(View.VISIBLE);

		JsonGetPromotorDetails jsonGetPromotorDetails = new JsonGetPromotorDetails();
		StringBuilder url = new StringBuilder();
		url.append(ConstantUtils.USER_DETAILS_URL);
		url.append(mCampaignDetails.getId());
		jsonGetPromotorDetails.getPromotorDetailsFromURL(url.toString(), this,
				mContext, mCampaignDetails.getId(), "0", start,
				ConstantUtils.MAX_USER_RESPONSE_COUNT);

	}

	@Override
	public void onUserDetailsRecieved(SinglePromotorData mSinglePromotorData) {
		this.mSinglePromotorData = mSinglePromotorData;

		// ArrayList<UserFormDetails> userFormList = new
		// ArrayList<UserFormDetails>();
		
		if(mSinglePromotorData!=null){
		if (mSinglePromotorData.getPersonalDetails() != null
				&& mSinglePromotorData.getPersonalDetails().size() > 0) {
			// promotorListProgressBar.setVisibility(View.GONE);
			// manageFetchUsers(mSinglePromotorData.getPersonalDetails());
			AllPromotorListFragment allPromotorListFragment = new AllPromotorListFragment();
			Bundle mBundle = new Bundle();
			mBundle.putParcelableArrayList(ConstantUtils.USER_LIST,
					mSinglePromotorData.getPersonalDetails());
			mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
					campaignDetailsList);
			// mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
			// userFormList);
			allPromotorListFragment.setArguments(mBundle);
			//ConstantUtils.ReferList = false;
			((UserMotherActivity) mActivity).onItemSelected(
					allPromotorListFragment, true);
		}
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
			executeQuery(0);//ConstantUtils.START_COUNT);
			// view.requestLayout();
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
			executeQuery(0);//ConstantUtils.START_COUNT);
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

}
