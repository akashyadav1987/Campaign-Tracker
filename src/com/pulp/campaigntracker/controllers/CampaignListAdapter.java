package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;

public class CampaignListAdapter extends BaseAdapter{


	private List<CampaignDetails> mCampaignList;
	private LayoutInflater layoutInflater;
	public final String STORES = "Stores : ";
	public final String PROMOTORS = "Promotors : ";
	int[] placeholders = {R.drawable.place_holder_blue, R.drawable.place_holder_red,R.drawable.place_holder_orange, R.drawable.place_holder_yellow};
	public CampaignListAdapter(Context mContext,List<CampaignDetails> mCampaignList)
	{
		this.mCampaignList = mCampaignList;
		this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}
	@Override
	public int getCount() {
		return mCampaignList.size();
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.campaign_list_item,
					null);
			viewHolder.campaignName = (TextView) convertView
					.findViewById(R.id.campaignName);
			viewHolder.campaignStoreCount = (TextView) convertView
					.findViewById(R.id.campaignStoreCount);
			viewHolder.campaignPromotorCount = (TextView) convertView
					.findViewById(R.id.campaignPromotorCount);
			viewHolder.campaignText = (TextView) convertView
					.findViewById(R.id.campaignText);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(mCampaignList.get(position).getName()!=null)
			viewHolder.campaignName.setText(getItem(position).getName());
		else
			viewHolder.campaignName.setVisibility(View.GONE);

		if(mCampaignList.get(position).getStoreList()!=null && getItem(position).getStoreList().size()>0)
			viewHolder.campaignStoreCount.setText(STORES + getItem(position).getStoreList().size());
		else
			viewHolder.campaignStoreCount.setVisibility(View.GONE);

		if(mCampaignList.get(position).getUserList()!=null && getItem(position).getUserList().size()>0)
			viewHolder.campaignPromotorCount.setText(PROMOTORS + getItem(position).getUserList().size());
		else
			viewHolder.campaignPromotorCount.setVisibility(View.GONE);

		if(mCampaignList.get(position).getName()!=null && mCampaignList.get(position).getName().length()>0)
		{	
			viewHolder.campaignText.setText(getItem(position).getName().substring(0, 1).toUpperCase());
			viewHolder.campaignText.setBackgroundResource(placeholders[(position % 4)]);
			
		}



		return convertView;
	}

	/**
	 * ViewHolder class for the list item.
	 * 
	 * @author udit.gupta
	 */
	public class ViewHolder {
		private TextView campaignName;
		private TextView campaignStoreCount;
		private TextView campaignPromotorCount;
		private TextView campaignText;
	}
}
