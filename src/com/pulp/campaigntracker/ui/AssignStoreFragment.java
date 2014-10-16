package com.pulp.campaigntracker.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.AssignStoreAdapter;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class AssignStoreFragment extends android.support.v4.app.Fragment
		implements android.widget.AdapterView.OnItemClickListener {

	private static final String TAG = AssignStoreFragment.class.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	private String assignCampaignName;
	private ListView assignStoreList;
	private UserProfile userDetails;
	String campaignDisplayName;
	private ArrayList<CampaignDetails> campaignDetailsList;
	String storeName;

	AssignStoreAdapter assignStoreAdapter;

	private CampaignDetails mCampaignDetails;

	private StoreDetails mEmptystoreDetails = null;
	private ArrayList<StoreDetails> newStoreDetailsList;
	private String selectedStore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity().getApplicationContext();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.assign_screen, container, false);

		assignStoreList = (ListView) view.findViewById(R.id.assignList);
		newStoreDetailsList = new ArrayList<StoreDetails>();
		setHasOptionsMenu(true);

		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			campaignDetailsList = mBundle
					.getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);
			campaignDisplayName = mBundle
					.getString(ConstantUtils.CAMPAIGN_NAME);
			assignCampaignName = mBundle
					.getString(ConstantUtils.ASSIGN_CAMPAIGN_NAME);
			mCampaignDetails = mBundle
					.getParcelable(ConstantUtils.CAMPAIGN_DETAILS);
			userDetails = mBundle.getParcelable(ConstantUtils.USER_DETAILS);

			TLog.v(TAG, "mBundle " + mBundle);
			setActionBarTitle();

		}
		assignStoreList = (ListView) view.findViewById(R.id.assignList);
		assignStoreList.setOnItemClickListener(this);

		newStoreDetailsList.addAll(mCampaignDetails.getStoreList());
		mEmptystoreDetails = new StoreDetails();
		mEmptystoreDetails.setName("None");
		newStoreDetailsList.add(0, mEmptystoreDetails);
		assignStoreList = (ListView) view.findViewById(R.id.assignList);

		if (assignStoreAdapter == null)
			assignStoreAdapter = new AssignStoreAdapter(mContext,
					newStoreDetailsList);

		assignStoreAdapter.notifyDataSetChanged();
		assignStoreList.setAdapter(assignStoreAdapter);

	
		return view;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle("Assign Store");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.assignList:
			
			
			selectedStore=newStoreDetailsList.get(position).getName();
				UtilityMethods.getAppPreferences(mContext)
					.edit()
					.putString(ConstantUtils.ASSIGN_STORE, selectedStore)
					.commit();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					mActivity);
			// set title
			alertDialogBuilder.setTitle("Are You Sure");
			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Are you sure about changing Campaign information? It will reset the store data for selected promoter.")
					.setCancelable(false)
					.setNegativeButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									PromotorDetailsFragment promotorDetailsFragment = new PromotorDetailsFragment();
									Bundle mBundle = new Bundle();
									mBundle.putString(ConstantUtils.ASSIGN_STORE_NAME,
											selectedStore);
									mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
											campaignDetailsList);
									mBundle.putString(ConstantUtils.CAMPAIGN_NAME, campaignDisplayName);
									mBundle.putParcelable(ConstantUtils.USER_DETAILS, userDetails);
									mBundle.putString(ConstantUtils.ASSIGN_CAMPAIGN_NAME,
											assignCampaignName);
									((UserMotherActivity) mActivity)
											.onItemSelected(
													promotorDetailsFragment,
													true);

								}
							})
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									PromotorDetailsFragment promotorDetailsFragment = new PromotorDetailsFragment();
									Bundle mBundle = new Bundle();
									mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
											campaignDetailsList);
									mBundle.putString(ConstantUtils.CAMPAIGN_NAME, campaignDisplayName);
									mBundle.putParcelable(ConstantUtils.USER_DETAILS, userDetails);
									mBundle.putString(ConstantUtils.ASSIGN_CAMPAIGN_NAME,
											assignCampaignName);
									((UserMotherActivity) mActivity)
											.onItemSelected(
													promotorDetailsFragment,
													true);

								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			break;
			
			
		default:

		}
	}
}
