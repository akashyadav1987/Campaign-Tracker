package com.pulp.campaigntracker.ui;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.NotificationListAdapter;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.controllers.UserListAdapter;
import com.pulp.campaigntracker.listeners.FetchMoreListner;
import com.pulp.campaigntracker.listeners.UserDetailsRecieved;
import com.pulp.campaigntracker.parser.JsonGetPromotorDetails;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class AllPromotorListFragment extends Fragment implements
		OnItemClickListener, UserDetailsRecieved, OnScrollListener {

	private static final String TAG = AllPromotorListFragment.class
			.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	// private List<CampaignDetails> campaignDetailsList;
	private ArrayList<CampaignDetails> campaignDetailsList;
	private CampaignDetails mCampaignDetails;
	private ArrayList<UserProfile> mPromotorList;

	private ArrayList<UserProfile> mActivePromotorList;
	private ArrayList<UserProfile> mInactivePromotorList;

	private UserListAdapter userListAdapter;
	private ListView promotorList;
	private EditText inputSearch;
	private TextView searchIcon;
	private Typeface icomoon;
	private String campaignDisplayName;
	private ArrayList<Parcelable> mUserForm;
	private boolean activeorinactive = true;
	private FetchMoreListner mCallBack;
	private ArrayList<UserProfile> mList;

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
		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");
		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			campaignDetailsList = mBundle
					.getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);
			// campaignDetailsList = mBundle
			// .getParcelableArrayList(ConstantUtils.CAMPAIGN_LIST);
			//
			mUserForm = mBundle
					.getParcelableArrayList(ConstantUtils.USER_FORM_LIST);
			mPromotorList = mBundle
					.getParcelableArrayList(ConstantUtils.USER_LIST);
			TLog.v(TAG, "mBundle " + mBundle);
			setActionBarTitle();

		}
		promotorList.setOnItemClickListener(this);
		this.mActivePromotorList = new ArrayList<UserProfile>();
		this.mInactivePromotorList = new ArrayList<UserProfile>();
		setHasOptionsMenu(true);
		inputSearch = (EditText) view.findViewById(R.id.inputSearch);
		inputSearch.setHint(Html.fromHtml("<small>" + getString(R.string.hint)
				+ "</small>"));

		for (int i = 0; i < mPromotorList.size(); i++) {
			if (mPromotorList.get(i).getStatus().equals("active")) {
				mActivePromotorList.add(mPromotorList.get(i));
			} else if (mPromotorList.get(i).getStatus().equals("inactive")) {
				mInactivePromotorList.add(mPromotorList.get(i));
			}
		}

		if (userListAdapter == null)
			userListAdapter = new UserListAdapter(mContext,
					mPromotorList);

		userListAdapter.notifyDataSetChanged();
		promotorList.setAdapter(userListAdapter);
		searchIcon = (TextView) view.findViewById(R.id.searchIcon);
		searchIcon.setTypeface(icomoon);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				// promotorListAdapter.get
				String text = inputSearch.getText().toString()
						.toLowerCase(Locale.getDefault());

				userListAdapter.filter(text);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// promotorListAdapter.getFilter().filter(s);

			}
		});
		promotorList.setOnScrollListener(this);
		return view;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle("All Promoters");
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.promoter_list_action_bar, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.activePromoters:
			activeorinactive = true;
			userListAdapter = new UserListAdapter(mContext,
					mActivePromotorList);
			userListAdapter.notifyDataSetChanged();
			promotorList.setAdapter(userListAdapter);
			break;

		case R.id.inactivePromoters:
			activeorinactive = false;
			userListAdapter = new UserListAdapter(mContext,
					mInactivePromotorList);
			userListAdapter.notifyDataSetChanged();
			promotorList.setAdapter(userListAdapter);

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.allPromotorList:
			try {
				PromotorDetailsFragment promotorDetailsFragment = new PromotorDetailsFragment();
				Bundle mBundle = new Bundle();
				// Bundle storeBundle = new Bundle();
				if (activeorinactive) {
					mBundle.putParcelable(ConstantUtils.USER_DETAILS,
							mActivePromotorList.get(position));
				} else {
					mBundle.putParcelable(ConstantUtils.USER_DETAILS,
							mInactivePromotorList.get(position));
				}

				mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
						mUserForm);
				// mBundle.putParcelable(ConstantUtils.CAMPAIGN_DETAILS,
				// mCampaignDetails); // sending particular campaign from
				// store list to all promoterslist
				mBundle.putParcelableArrayList(ConstantUtils.CAMPAIGN_LIST,
						campaignDetailsList);

				campaignDisplayName = mPromotorList.get(position)
						.getCurrentCampagin();
				mBundle.putString(ConstantUtils.CAMPAIGN_NAME,
						campaignDisplayName);

				promotorDetailsFragment.setArguments(mBundle);

				((UserMotherActivity) mActivity).onItemSelected(
						promotorDetailsFragment, true);

			} catch (Exception e) {
				Toast.makeText((UserMotherActivity) mActivity,
						"ArrayOutOfBounds", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}

	private void execute() {
		JsonGetPromotorDetails jsonGetPromotorDetails = new JsonGetPromotorDetails();
		StringBuilder url = new StringBuilder();
		url.append(ConstantUtils.USER_DETAILS_URL);
		// url.append(mCampaignDetails.getId());
		jsonGetPromotorDetails.getPromotorDetailsFromURL(url.toString(), this,
				mContext, "0", "0", mPromotorList.size(), ConstantUtils.MAX_USER_RESPONSE_COUNT);

	}

	@Override
	public void onUserDetailsRecieved(SinglePromotorData mSinglePromotorData) {
		if (mSinglePromotorData.getPersonalDetails() != null
				&& mSinglePromotorData.getPersonalDetails().size() > 0) {
			mList = mSinglePromotorData.getPersonalDetails();
			// for (int i = 0; i < mList.size(); i++) {
			// if (mList.get(i).getStatus().equals("active")) {
			// mActivePromotorList.add(mList.get(i));
			// } else if (mList.get(i).getStatus().equals("inactive")) {
			// mInactivePromotorList.add(mList.get(i));
			// }
			// }

			mPromotorList.addAll(mList);

			UtilityMethods.refreshList(userListAdapter, promotorList,
					mPromotorList, mContext);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
			int lastInScreen = firstVisibleItem + visibleItemCount;
//		if ((lastInScreen == totalItemCount) && !(ConstantUtils.loadingMore)) {
		if (lastInScreen == totalItemCount){
			Toast.makeText(mContext, "Size" + mPromotorList.size(),
					Toast.LENGTH_LONG).show();
			execute();
		}
	}
}
