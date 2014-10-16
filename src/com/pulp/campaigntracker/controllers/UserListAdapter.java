package com.pulp.campaigntracker.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.UserFormAdapter.ViewHolder;
import com.pulp.campaigntracker.utils.ConstantUtils;

public class UserListAdapter extends BaseAdapter {

	private Context mContext;
	private List<UserProfile> mUserList;
	List<UserProfile> mAllUserList;
	private LayoutInflater layoutInflater;
	ViewHolder viewHolder = null;

	int[] placeholders = { R.drawable.place_holder_blue,
			R.drawable.place_holder_red, R.drawable.place_holder_orange,
			R.drawable.place_holder_yellow };


	private Typeface icomoon;



	public UserListAdapter(Context mContext,
			ArrayList<UserProfile> mUserList) {
		this.mContext = mContext;
		this.mUserList = mUserList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");
		this.mAllUserList = new ArrayList<UserProfile>();
		mAllUserList.addAll(mUserList);

	}
	
	


	@Override
	public int getCount() {
		return mUserList.size();
	}

	@Override
	public UserProfile getItem(int position) {
		return mUserList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());

		mUserList.clear();
		if (charText.length() == 0) {
			mUserList.addAll(mAllUserList);
		} else {
			for (UserProfile p : mAllUserList) {
				if (p.getName().toLowerCase()
						.contains(charText.toString().toLowerCase()))
					mUserList.add(p);
			}
		}

		notifyDataSetChanged();
	}


	/**
	 * 
	 * 
	 * 
	 */

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {



		convertView = layoutInflater.inflate(R.layout.promotor_list_item, null);
		viewHolder = new ViewHolder();


		viewHolder.userName = (TextView) convertView
				.findViewById(R.id.promotorName);
		viewHolder.type =(TextView) convertView.findViewById(R.id.role);
		viewHolder.userNameIcon = (TextView) convertView
				.findViewById(R.id.promotorNameIcon);
		viewHolder.phoneIcon = (TextView) convertView
				.findViewById(R.id.phoneIcon);
		viewHolder.emailIcon = (TextView) convertView
				.findViewById(R.id.emailIcon);
		viewHolder.campaignImage = (ImageView) convertView
				.findViewById(R.id.campaignImage);
		viewHolder.userLocationOrTime = (TextView) convertView
				.findViewById(R.id.promotorCheckinTime);
		viewHolder.status = (View) convertView.findViewById(R.id.status);

		convertView.setTag(viewHolder);
	

		if (mUserList.get(position).getName() != null)
			viewHolder.userName.setText(getItem(position).getName());

		if (mUserList.get(position).getName() != null)
			viewHolder.userNameIcon.setText(getItem(position).getName()
					.substring(0, 1).toUpperCase());
		viewHolder.userNameIcon
				.setBackgroundResource(placeholders[(position % 4)]);
		if(getItem(position).getRole()==null){
			getItem(position).setRole("");
		}
		
		viewHolder.type.setText("Role: "+getItem(position).getRole());
		viewHolder.emailIcon.setTypeface(icomoon);
		viewHolder.phoneIcon.setTypeface(icomoon);

		if (getItem(position).getStatus().equalsIgnoreCase("active")) {

			setStatusBackground(R.drawable.circle_active);

			viewHolder.userLocationOrTime.setText(getItem(position)
						.getCurrentCampagin());

		}

		else
			setStatusBackground(R.drawable.circle_inactive);

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

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setStatusBackground(int resID) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			viewHolder.status.setBackground(mContext.getResources()
					.getDrawable(resID));
		} else {
			viewHolder.status.setBackgroundDrawable(mContext.getResources()
					.getDrawable(resID));
		}
	}

	/**
	 * ViewHolder class for the list item.
	 * 
	 * 
	 */
	public class ViewHolder {
		private TextView userName;
		private TextView userNameIcon;
		private TextView phoneIcon;
		private TextView emailIcon;
		private ImageView campaignImage;
		private TextView userLocationOrTime;
		private TextView type;
		private View status;

	}

}
