package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;

public class AssignCampaignAdapter extends BaseAdapter {

	private Context mContext;
	private List<CampaignDetails> mCampaignList;

	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	public final String STORES = "Stores : ";
	public final String PROMOTORS = "Promotors : ";

	public AssignCampaignAdapter(Context mContext,
			List<CampaignDetails> mCampaignList) {
		this.mContext = mContext;
		this.mCampaignList = mCampaignList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();

	}

	@Override
	public int getCount() {
		return (mCampaignList.size());
	}

	@Override
	public CampaignDetails getItem(int position) {
		return mCampaignList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {

			view = layoutInflater.inflate(R.layout.assign_campaign_item,
					parent, false);
		}
		
		
		if (mCampaignList.get(position).getName() != null)
			((TextView) view.findViewById(R.id.assignCampaignName))
					.setText(getItem(position).getName());
		else
			((TextView) view.findViewById(R.id.assignCampaignName))
					.setVisibility(View.GONE);
	
//		if (mCampaignList.get(position).getStoreList() != null
//				&& getItem(position).getStoreList().size() > 0)
//			((TextView) view.findViewById(R.id.storesCount)).setText(STORES
//					+ getItem(position).getStoreList().size());
//		else
//			((TextView) view.findViewById(R.id.storesCount))
//					.setVisibility(View.GONE);
//
//		if (mCampaignList.get(position).getUserList() != null
//				&& getItem(position).getUserList().size() > 0)
//			((TextView) view.findViewById(R.id.promotorsCount))
//					.setText(PROMOTORS + getItem(position).getUserList().size());
//		else
//			((TextView) view.findViewById(R.id.promotorsCount))
//					.setVisibility(View.GONE);

		return view;

	}

}
