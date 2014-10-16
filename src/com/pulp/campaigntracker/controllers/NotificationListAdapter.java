package com.pulp.campaigntracker.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserNotification;

public class NotificationListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private ArrayList<UserNotification> mNotificationList;
	private Typeface icomoon;
	private ViewHolder viewHolder;

	public NotificationListAdapter(Context mContext,
			ArrayList<UserNotification> mNotificationList) {
		this.mContext = mContext;
		this.mNotificationList = mNotificationList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNotificationList.size();
	}

	@Override
	public UserNotification getItem(int position) {
		// TODO Auto-generated method stub
		return mNotificationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.notification_list_item, null);
			viewHolder.notifyTitle = (TextView) convertView
					.findViewById(R.id.notifyTitle);
			viewHolder.notifyMessage = (TextView) convertView
					.findViewById(R.id.notifyMessage);
			viewHolder.clockTime = (TextView) convertView
					.findViewById(R.id.clockTime);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (mNotificationList.get(position).getTitle() != null)
			viewHolder.notifyTitle.setText(getItem(position).getTitle());

		if (mNotificationList.get(position).getMessage() != null)
			viewHolder.notifyMessage.setText(getItem(position).getMessage());

		if (mNotificationList.get(position).getNotifyTime() != 0) {

			Calendar calendar = Calendar.getInstance();

			String date = new SimpleDateFormat("dd/MM/yyyy",
					Locale.getDefault()).format(new java.util.Date(getItem(
					position).getNotifyTime()));

			String currentDate = new SimpleDateFormat("dd/MM/yyyy",
					Locale.getDefault()).format(calendar.getTime());
			int compare = date.compareTo(currentDate);
			if (compare == 0) {
				String time = new SimpleDateFormat("hh:mm a",
						Locale.getDefault()).format(new java.util.Date(getItem(
						position).getNotifyTime()));
				viewHolder.clockTime.setText(time);

			} else {
				viewHolder.clockTime.setText(date);
			}

		}
		return convertView;
	}

	/**
	 * ViewHolder class for the list item.
	 * 
	 * 
	 */
	public class ViewHolder {
		private TextView notifyTitle;
		private TextView notifyMessage;
		private TextView clockTime;

	}

}
