package com.pulp.campaigntracker.ui;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.PromotorListAdapter;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;

public class AllPromotorListFragment extends Fragment {

	private static final String TAG = AllPromotorListFragment.class
			.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	private ArrayList<UserProfile> mPromotorList;
	private PromotorListAdapter promotorListAdapter;
	private ListView promotorList;
	private EditText inputSearch;

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

		// if(JsonGetCampaignDetails.getInstance().getCampaignDetails(LoginType.supervisor)!=null
		// &&
		// JsonGetCampaignDetails.getInstance().getCampaignDetails(LoginType.supervisor)
		// instanceof List<?>)
		// {
		// List<CampaignDetails> mCampaignList = (List<CampaignDetails>)
		// JsonGetCampaignDetails.getInstance().getCampaignDetails(LoginType.supervisor);
		// }
		View view = inflater.inflate(R.layout.all_promotor_list, container,
				false);
		promotorList = (ListView) view.findViewById(R.id.allPromotorList);

		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			mPromotorList = mBundle
					.getParcelableArrayList(ConstantUtils.PROMOTOR_LIST);
			TLog.v(TAG, "mBundle " + mBundle);
			setActionBarTitle();
			
		}

		inputSearch = (EditText) view.findViewById(R.id.inputSearch);
		inputSearch.setHint(Html.fromHtml("<small>" + 
	             getString(R.string.hint) + "</small>"));

		if (promotorListAdapter == null)
			promotorListAdapter = new PromotorListAdapter(mContext,
					mPromotorList);

		promotorListAdapter.notifyDataSetChanged();
		// promotorList.setOnScrollListener(new EndlessScrollListener());
		promotorList.setAdapter(promotorListAdapter);

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				// promotorListAdapter.get
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());

				promotorListAdapter.filter(text);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
			//	promotorListAdapter.getFilter().filter(s);

			}
		});

		return view;
	}


	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
			mActivity.getActionBar().setTitle("All Promoters");
			}
		
	}

}
