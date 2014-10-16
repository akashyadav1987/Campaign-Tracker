package com.pulp.campaigntracker.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.controllers.StoreDetailsAdapter;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;

public class PromotorListFragment extends android.support.v4.app.Fragment implements android.widget.AdapterView.OnItemClickListener{

	private static final String TAG = PromotorListFragment.class.getSimpleName();
	private Activity mActivity;
	private Context mContext;
	private ListView storeList;
	StoreDetailsAdapter storeDetailsListAdapter;
	private CampaignDetails mCampaignDetails;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity(); 
		mContext = getActivity().getBaseContext();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.store_list_screen, container, false);
		if(getArguments()!=null)

		{
			Bundle mBundle = getArguments();
			mCampaignDetails = mBundle.getParcelable(ConstantUtils.CAMPAIGN_DETAILS);
			TLog.v(TAG, "mBundle "+mBundle);
		}
		storeList = (ListView)view.findViewById(R.id.storeList);
		storeList.setOnItemClickListener(this);

		if(storeDetailsListAdapter == null)
			storeDetailsListAdapter = new StoreDetailsAdapter(mContext, mCampaignDetails.getStoreList());
		storeDetailsListAdapter.notifyDataSetChanged();
		storeList.setAdapter(storeDetailsListAdapter);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		switch (arg0.getId()) {
		case R.id.storeList:

			StoreFragment sf = new StoreFragment();
			Bundle mBundle  = new Bundle();
			mBundle.putParcelable(ConstantUtils.STORE_DETAILS, mCampaignDetails.getStoreList().get(arg2));
			sf.setArguments(mBundle);
			((UserMotherActivity)mActivity).onItemSelected(sf, true);


			break;

		default:
			break;
		}
	}

}
