package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.StoreDetails;

public class AssignStoreAdapter extends BaseAdapter {

	private Context mContext;
	private List<StoreDetails> mStoreList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;

	private String storeNameString;

	// ViewHolder viewHolder;

	public AssignStoreAdapter(Context mContext, List<StoreDetails> mStoreList) {
		this.mContext = mContext;
		this.mStoreList = mStoreList;
		this.layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();

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

	int selected = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder viewHolder = null;
		// final ViewHolder viewHolder;
		View view = convertView;
		if (view == null) {
			// viewHolder = new ViewHolder();
			view = layoutInflater.inflate(R.layout.assign_store_item, parent,
					false);
		}

		if (mStoreList.get(position).getName() != null)

			((TextView) view.findViewById(R.id.assignStoreName))
					.setText(getItem(position).getName());
		else
			((TextView) view.findViewById(R.id.assignStoreName))
					.setVisibility(View.GONE);

		if (mStoreList.get(position).getState() != null)
			((TextView) view.findViewById(R.id.assignStoreState))
					.setText(getItem(position).getState());
		else
			((TextView) view.findViewById(R.id.assignStoreState))
					.setVisibility(View.GONE);

		if (mStoreList.get(position).getRegion() != null)
			((TextView) view.findViewById(R.id.assignStoreRegion))
					.setText(getItem(position).getRegion());
		else
			((TextView) view.findViewById(R.id.assignStoreRegion))
					.setVisibility(View.GONE);

		storeNameString = mStoreList.get(position).getName();

		return view;

	}

}
