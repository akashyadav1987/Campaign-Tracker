package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserProfile;

public class SupervisorListAdapter extends BaseAdapter {

	private Context mContext;
	private List<UserProfile> mSupervisorList;
	private LayoutInflater layoutInflater;
	int[] placeholders = { R.drawable.place_holder_blue,
			R.drawable.place_holder_red, R.drawable.place_holder_orange,
			R.drawable.place_holder_yellow };
	private ImageLoader imageLoader;
	private Typeface icomoon;

	public SupervisorListAdapter(Context mContext,
			List<UserProfile> mSupervisorList) {
		this.mContext = mContext;
		this.mSupervisorList = mSupervisorList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");

	}

	@Override
	public int getCount() {
		return mSupervisorList.size();
	}

	@Override
	public UserProfile getItem(int position) {
		return mSupervisorList.get(position);
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
			convertView = layoutInflater.inflate(
					R.layout.contact_supervisor_list_item, null);
			viewHolder.supervisor = (TextView) convertView
					.findViewById(R.id.supervisor);
			viewHolder.firstLetterOfSecondLevel = (TextView) convertView
					.findViewById(R.id.firstLetterOfSecondLevel);
			viewHolder.phoneIcon = (TextView) convertView
					.findViewById(R.id.phoneIcon);
			viewHolder.emailIcon = (TextView) convertView
					.findViewById(R.id.emailIcon);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (mSupervisorList.get(position).getName() != null)
			viewHolder.supervisor.setText(getItem(position).getName());

		if (mSupervisorList.get(position).getName() != null) {
			viewHolder.firstLetterOfSecondLevel.setText(getItem(position)
					.getName().substring(0, 1).toUpperCase());
			viewHolder.firstLetterOfSecondLevel
					.setBackgroundResource(placeholders[(position % 4)]);
		}

		viewHolder.emailIcon.setTypeface(icomoon);
		viewHolder.phoneIcon.setTypeface(icomoon);

		viewHolder.phoneIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL, Uri
						.parse("tel:" + getItem(position).getContactNumber()));
				callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(callIntent);
			}
		});
		viewHolder.emailIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent messageIntent;
				messageIntent = new Intent(Intent.ACTION_VIEW);
				messageIntent.setData(Uri.parse("smsto:"
						+ getItem(position).getContactNumber().toString()));
				messageIntent.putExtra("sms_body", "Please Contact Me ASAP");
				messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(messageIntent);
			}
		});

		return convertView;
	}

	/**
	 * ViewHolder class for the list item.
	 * 
	 * @author udit.gupta
	 */
	public class ViewHolder {
		private TextView supervisor;
		private TextView firstLetterOfSecondLevel;
		private TextView phoneIcon;
		private TextView emailIcon;
	}
}
