package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.StoreDetails;

public class StoreDetailsAdapter extends BaseAdapter {

	private Context mContext;
	private List<StoreDetails> mStoreList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private Typeface icomoon;
	private double storeLan;
	private double storeLat;
	private String storeNameString;
	int[] placeholders = { R.drawable.place_holder_blue,
			R.drawable.place_holder_red, R.drawable.place_holder_orange,
			R.drawable.place_holder_yellow };

	public StoreDetailsAdapter(Context mContext, List<StoreDetails> mStoreList) {
		this.mContext = mContext;
		this.mStoreList = mStoreList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");

	}

	@Override
	public int getCount() {
		return mStoreList.size();
	}

	@Override
	public StoreDetails getItem(int position) {
		return mStoreList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.store_details_layout,
					null);
			viewHolder.storeName = (TextView) convertView
					.findViewById(R.id.storeName);
			viewHolder.storeState = (TextView) convertView
					.findViewById(R.id.storeState);
			viewHolder.storeMapImage = (TextView) convertView
					.findViewById(R.id.storeMapImage);
			viewHolder.storeRegion = (TextView) convertView
					.findViewById(R.id.storeRegion);
			viewHolder.textbelowImage = (TextView) convertView
					.findViewById(R.id.forwardIcon);
			viewHolder.storeText = (TextView) convertView
					.findViewById(R.id.storeText);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (mStoreList.get(position).getName() != null)

			viewHolder.storeName.setText(getItem(position).getName());
		
//		if(viewHolder.storeName.length()>15){
//			String smallString = getItem(position).getName().substring(0, Math.min(viewHolder.storeName.length(), 12));
//			smallString = smallString.concat("...");
//			viewHolder.storeName.setText(smallString);
//		} //Ritu
//		
//		
//		else
//			viewHolder.storeName.setVisibility(View.GONE);

		if (mStoreList.get(position).getState() != null)
			viewHolder.storeState.setText(getItem(position).getState());
		else
			viewHolder.storeState.setVisibility(View.GONE);

		if (mStoreList.get(position).getRegion() != null)
			viewHolder.storeRegion.setText(getItem(position).getRegion());
		else
			viewHolder.storeRegion.setVisibility(View.GONE);

		storeLat = mStoreList.get(position).getLatitude();
		storeLan = mStoreList.get(position).getLongitude();
		storeNameString = mStoreList.get(position).getName();
		viewHolder.storeMapImage.setTypeface(icomoon);

		viewHolder.storeMapImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showOnMap();
				if (storeLan != 0 && storeLat != 0)
					showOnMap();
				else
					Toast.makeText(mContext, "Store Co-ordinates Not Availabe",
							Toast.LENGTH_LONG).show();
			}
		});

		viewHolder.textbelowImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showOnMap();
				if (storeLan != 0 && storeLat != 0)
					showOnMap();
				else
					Toast.makeText(mContext, "Store Co-ordinates Not Availabe",
							Toast.LENGTH_LONG).show();
			}
		});
		if (mStoreList.get(position).getName() != null
				&& mStoreList.get(position).getName().length() > 0) {
			viewHolder.storeText.setText(getItem(position).getName()
					.substring(0, 1).toUpperCase());
			viewHolder.storeText
					.setBackgroundResource(placeholders[(position % 4)]);

		}

		return convertView;
	}

	/**
	 * ViewHolder class for the list item.
	 * 
	 * @author udit.gupta
	 */
	public class ViewHolder {
		private TextView storeDetailsHeading;
		private TextView storeName;
		private TextView storeState;
		private TextView storeMapImage;
		private TextView storeRegion;
		private TextView storeText;
		private TextView textbelowImage;

	}

	private void showOnMap() {
		String label = "Store Location";
		String uriBegin = "geo:" + storeLat + "," + storeLan;
		String query = storeLat + "," + storeLan + "(" + label + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

}
