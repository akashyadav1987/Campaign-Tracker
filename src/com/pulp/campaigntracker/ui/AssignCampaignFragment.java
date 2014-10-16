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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.AssignCampaignAdapter;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.ObjectSerializer;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class AssignCampaignFragment extends android.support.v4.app.Fragment
		implements OnItemClickListener {

	private static final String TAG = AssignCampaignFragment.class
			.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	private StoreDetails mStoreDetails;
	private ArrayList<UserFormDetails> mUserForm;
	private UserProfile userDetails;
	private ListView assignCampaignList;
	private ArrayList<CampaignDetails> campaignDetailsList;

	private CampaignDetails mEmptyCamapign = null;
	private ArrayList<CampaignDetails> newCampaignDetailsList;
	AssignCampaignAdapter assignCampaignAdapter;

	private String campaignDisplayName;
	private String assignStoreName;
	private String selectedCampaign;

	// ArrayList<CampaignDetails> mCampaignDetails = new
	// ArrayList<CampaignDetails>();

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

		View view = inflater.inflate(R.layout.assign_screen, container, false);

		assignCampaignList = (ListView) view.findViewById(R.id.assignList);
		newCampaignDetailsList = new ArrayList<CampaignDetails>();
		setHasOptionsMenu(true);

		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			mStoreDetails = mBundle.getParcelable(ConstantUtils.STORE_DETAILS);
			userDetails = mBundle.getParcelable(ConstantUtils.USER_DETAILS);
			mUserForm = mBundle
					.getParcelableArrayList(ConstantUtils.USER_FORM_LIST);
			assignStoreName = mBundle
					.getString(ConstantUtils.ASSIGN_STORE_NAME);
			campaignDetailsList = mBundle
					.getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);

			campaignDisplayName = mBundle
					.getString(ConstantUtils.CAMPAIGN_NAME);

			setActionBarTitle();

		}
		newCampaignDetailsList.addAll(campaignDetailsList);
		mEmptyCamapign = new CampaignDetails();
		mEmptyCamapign.setName("None");
		newCampaignDetailsList.add(0, mEmptyCamapign);
		assignCampaignList = (ListView) view.findViewById(R.id.assignList);
		assignCampaignList.setOnItemClickListener(this);

		if (assignCampaignAdapter == null)
			assignCampaignAdapter = new AssignCampaignAdapter(mContext,
					newCampaignDetailsList);

		assignCampaignAdapter.notifyDataSetChanged();
		assignCampaignList.setAdapter(assignCampaignAdapter);

		return view;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle("Assign Campaign");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.assignList:

			selectedCampaign = newCampaignDetailsList.get(position).getName();

			UtilityMethods.getAppPreferences(mContext)
					.edit()
					.putString(ConstantUtils.ASSIGN_CAMAIGN, selectedCampaign)
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
									mBundle.putParcelableArrayList(
											ConstantUtils.CAMPAIGN_LIST,
											campaignDetailsList);
									mBundle.putString(
											ConstantUtils.CAMPAIGN_NAME,
											campaignDisplayName);
									mBundle.putString(
											ConstantUtils.ASSIGN_CAMPAIGN_NAME,
											selectedCampaign);
									mBundle.putParcelableArrayList(
											ConstantUtils.USER_FORM_LIST,
											mUserForm);
									mBundle.putString(
											ConstantUtils.ASSIGN_STORE_NAME,
											assignStoreName);
									mBundle.putParcelable(
											ConstantUtils.USER_DETAILS,
											userDetails);
									promotorDetailsFragment
											.setArguments(mBundle);
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
									mBundle.putParcelableArrayList(
											ConstantUtils.CAMPAIGN_LIST,
											campaignDetailsList);
									mBundle.putString(
											ConstantUtils.CAMPAIGN_NAME,
											campaignDisplayName);
									mBundle.putParcelableArrayList(
											ConstantUtils.USER_FORM_LIST,
											mUserForm);
									mBundle.putString(
											ConstantUtils.ASSIGN_STORE_NAME,
											assignStoreName);
									mBundle.putParcelable(
											ConstantUtils.USER_DETAILS,
											userDetails);
									promotorDetailsFragment
											.setArguments(mBundle);
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
			break;
		}

	}
}
