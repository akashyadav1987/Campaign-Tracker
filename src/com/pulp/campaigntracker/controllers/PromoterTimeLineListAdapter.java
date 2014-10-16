package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserNotification;

public class PromoterTimeLineListAdapter extends BaseAdapter{


	private Context mContext;
	private List<UserNotification> mPromotorList;
	private LayoutInflater layoutInflater;

	public PromoterTimeLineListAdapter(Context mContext,List<UserNotification> mPromotorList)
	{
		this.mContext = mContext;
		this.mPromotorList = mPromotorList;
		this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	@Override
	public int getCount() {
		return mPromotorList.size();
	}

	@Override
	public UserNotification getItem(int position) {
		return mPromotorList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.promotor_notification_item,
					null);
			viewHolder.notificationTitle = (TextView) convertView
					.findViewById(R.id.notificationTitle);
			viewHolder.notificationMessage = (TextView) convertView
					.findViewById(R.id.notificationMessage);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(mPromotorList.get(position).getTitle()!=null)
			viewHolder.notificationTitle.setText(getItem(position).getTitle());

		if(mPromotorList.get(position).getMessage()!=null)
			viewHolder.notificationMessage.setText(getItem(position).getMessage());



		return convertView;
	}

	/**
	 * ViewHolder class for the list item.
	 * 
	 * @author udit.gupta
	 */
	public class ViewHolder {
		private TextView notificationTitle;
		private TextView notificationMessage;
	}

}
