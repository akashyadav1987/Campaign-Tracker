package com.pulp.campaigntracker.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.menu;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.internal.da;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.ResponseData;
import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.StoreDetails;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.PromotorListAdapter;
import com.pulp.campaigntracker.controllers.UserFormAdapter;
import com.pulp.campaigntracker.listeners.GetStoreDistanceRecieved;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.listeners.PromotorDetailsRecieved;
import com.pulp.campaigntracker.listeners.ResponseRecieved;
import com.pulp.campaigntracker.listeners.UpdateLocation;
import com.pulp.campaigntracker.listeners.UserLocationManager;
import com.pulp.campaigntracker.parser.JsonGetPromotorDetails;
import com.pulp.campaigntracker.parser.JsonGetStoreDistance;
import com.pulp.campaigntracker.parser.JsonGetUserNotification;
import com.pulp.campaigntracker.parser.JsonSubmitSucessParser;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class StoreFragment extends Fragment implements OnClickListener,
		OnItemClickListener, PromotorDetailsRecieved, UpdateLocation,
		GetStoreDistanceRecieved, ResponseRecieved {

	private static final String TAG = StoreFragment.class.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	private TextView compaginLogo;
	private TextView storeAddress;
	private TextView storeName;
	private TextView addLine1;
	private TextView storeState;
	private TextView storePincode;
	private TextView storeRouteMapIcon;
	private TextView storeCheckInIcon;
	private ListView promotorList;
	private StoreDetails mStoreDetails;
	private ArrayList<UserProfile> mPromotorList;
	private PromotorListAdapter promotorListAdapter;
	private Typeface icomoon;
	private FrameLayout mapFrame;
	private RelativeLayout checkInLayout;
	private TextView userIcon;
	private GoogleMap map;
	private SupportMapFragment mapFragment;
	private View view;
	private String mCurrentPhotoPath;
	private ProgressBar promotorListProgressBar;
	private View buttonLayoutView;
	private TextView uploadFormIcon;
	private TextView uploadForm;
	private Button submitButton;
	private RelativeLayout promoterCheckInFrame;
	private TextView tvDistanceDuration;
	private TextView fillReportIcon;
	private Typeface iconFonts;
	private RelativeLayout storeDetails;
	private ImageView myImage;
	private TextView CheckInIcon;
	private TextView RouteMapIcon;
	private TextView checkOutIcon;
	SharedPreferences preferences;
	private boolean bCheckInLayout;
	private boolean status;
	private String uploadFormSavedFilePath;
	File uploadFormSavedFile = null;
	private LatLng storeLatLng;
	private double storeLatitude;
	private double storeLongitude;
	private double myLatitude;
	private double myLongitude;
	private ListView userForm;
	private ArrayList<UserFormDetails> mUserForm;
	private UserFormAdapter userFormListAdapter;
	private ArrayList<Parcelable> mUserFormList;
	private TextView userListIcon;
	private Typeface user_list_icon;
	private TextView checkInText;
	private ResponseData mResponseData;
	private ArrayList<NameValuePair> formSubmitValues;
	private RelativeLayout errorLayout;
	private TextView errorImage;
	private Button retryButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mContext = getActivity().getBaseContext();
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		iconFonts = Typeface.createFromAsset(mContext.getAssets(),
				"icomoon.ttf");

		view = inflater.inflate(R.layout.store_info_screen, container, false);
		preferences = mContext.getSharedPreferences("PrefString",
				Context.MODE_PRIVATE);
		if (getArguments() != null)

		{
			Bundle mBundle = getArguments();
			mStoreDetails = mBundle.getParcelable(ConstantUtils.STORE_DETAILS);
			// mPromotorList = mBundle
			// .getParcelableArrayList(ConstantUtils.PROMOTOR_LIST);
			mUserForm = mBundle
					.getParcelableArrayList(ConstantUtils.USER_FORM_LIST);
			setActionBarTitle();

			TLog.v(TAG, "mBundle " + mBundle);
		}

		// /New Code Below this

		buttonLayoutView = inflater.inflate(R.layout.user_form_button_layout,
				null);
		userForm = (ListView) view.findViewById(R.id.userForm);

		uploadFormIcon = (TextView) buttonLayoutView
				.findViewById(R.id.uploadFormIcon);
		uploadFormIcon.setTypeface(iconFonts);
		uploadForm = (TextView) buttonLayoutView.findViewById(R.id.uploadForm);
		submitButton = (Button) buttonLayoutView
				.findViewById(R.id.submitFormButton);

		promoterCheckInFrame = (RelativeLayout) view
				.findViewById(R.id.promoterCheckInFrame);

		checkInLayout = (RelativeLayout) view
				.findViewById(R.id.checkInLayout_ref);

		tvDistanceDuration = (TextView) view
				.findViewById(R.id.tvDistanceDuration);

		storeDetails = (RelativeLayout) view.findViewById(R.id.storeDetails);
		storeName = (TextView) view.findViewById(R.id.storeName);
		addLine1 = (TextView) view.findViewById(R.id.addLine1);
		storeState = (TextView) view.findViewById(R.id.storeState);
		storePincode = (TextView) view.findViewById(R.id.storePincode);
		myImage = (ImageView) checkInLayout.findViewById(R.id.imageCaptured);

		userIcon = (TextView) view.findViewById(R.id.userIcon);
		userIcon.setTypeface(iconFonts);

		CheckInIcon = (TextView) storeDetails
				.findViewById(R.id.storeCheckInIcon);
		checkInText = (TextView) storeDetails.findViewById(R.id.checkInText);
		CheckInIcon.setTypeface(iconFonts);

		userListIcon = (TextView) storeDetails.findViewById(R.id.userListIcon);
		userListIcon.setTypeface(iconFonts);

		RouteMapIcon = (TextView) storeDetails
				.findViewById(R.id.storeRouteMapIcon);
		RouteMapIcon.setTypeface(iconFonts);

		fillReportIcon = (TextView) storeDetails
				.findViewById(R.id.fillReportIcon);
		fillReportIcon.setTypeface(iconFonts);

		compaginLogo = (TextView) storeDetails.findViewById(R.id.compaginLogo);
		compaginLogo.setTypeface(iconFonts);

		mapFrame = (FrameLayout) view.findViewById(R.id.mapFrame_ref);
		map = ((SupportMapFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		// promotorList.setVisibility(View.VISIBLE);
		mapFrame.setVisibility(View.GONE);
		checkInLayout.setVisibility(View.GONE);
		userForm.setVisibility(View.GONE);

		promotorListProgressBar = (ProgressBar) view
				.findViewById(R.id.promotorListProgressBar);

		promotorList = (ListView) view.findViewById(R.id.promotorList);

		userIcon = (TextView) view.findViewById(R.id.userIcon);

		executeQuery();

		promotorList.setOnItemClickListener(this);
		CheckInIcon.setOnClickListener(this);
		RouteMapIcon.setOnClickListener(this);
		checkInLayout.setOnClickListener(this);
		fillReportIcon.setOnClickListener(this);
		uploadFormIcon.setOnClickListener(this);
		compaginLogo.setOnClickListener(this);
		userListIcon.setOnClickListener(this);
		submitButton.setOnClickListener(this);

		storeLatitude = mStoreDetails.getLatitude();
		storeLongitude = mStoreDetails.getLongitude();

		// Set all the values from the data
		storeName.setText(mStoreDetails.getName());
		storeState.setText(mStoreDetails.getState());
		// storeAddress.setText(mStoreDetails.getCity());
		// storeAddress.setText(mStoreDetails.getAddress());

		if (userFormListAdapter == null)
			userFormListAdapter = new UserFormAdapter(mContext, mUserForm);
		userFormListAdapter.notifyDataSetChanged();
		if (userForm.getFooterViewsCount() == 0) {
			userForm.addFooterView(buttonLayoutView);
		}
		userForm.setAdapter(userFormListAdapter);

		if (preferences.getBoolean(ConstantUtils.STATUS, false)) {
			checkInText.setText(getString(R.string.log_out));
			fillReportIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.red));
		} else {
			checkInText.setText(getString(R.string.check_in));
			fillReportIcon.setTextColor(mContext.getResources().getColor(
					R.color.GreyLineColor));
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
		}

		errorLayout = (RelativeLayout) view.findViewById(R.id.storeErrorLayout);
		errorImage = (TextView) errorLayout.findViewById(R.id.errorImage);
		retryButton = (Button) errorLayout.findViewById(R.id.retryButton);
		retryButton.setOnClickListener(this);
		errorImage.setTypeface(iconFonts);

		if (!UtilityMethods.isNetworkAvailable(mContext))
			errorLayout.setVisibility(View.VISIBLE);
		else
			errorLayout.setVisibility(View.INVISIBLE);

		return view;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void setActionBarTitle() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mActivity.getActionBar().setTitle(mStoreDetails.getName());
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.storeCheckInIcon:
			promotorList.setVisibility(View.GONE);
			checkInLayout.setVisibility(View.VISIBLE);
			mapFrame.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			break;

		case R.id.userListIcon:
			promotorList.setVisibility(View.VISIBLE);
			checkInLayout.setVisibility(View.INVISIBLE);
			mapFrame.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			break;

		case R.id.storeRouteMapIcon:

			promotorList.setVisibility(View.GONE);
			checkInLayout.setVisibility(View.INVISIBLE);
			mapFrame.setVisibility(View.VISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			showOnMap();
			break;
		case R.id.compaginLogo:

			promotorList.setVisibility(View.VISIBLE);
			checkInLayout.setVisibility(View.GONE);
			mapFrame.setVisibility(View.GONE);
			break;
		case R.id.checkInLayout_ref:

			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			File f = null;
			TLog.v(TAG, "takePictureIntent : ");

			try {
				TLog.v(TAG, "UtilityMethods : ");

				f = UtilityMethods.setUpPhotoFile(mContext);
				TLog.v(TAG, "mCurrentPhotoPath : ");

				mCurrentPhotoPath = f.getAbsolutePath();
				TLog.v(TAG, "putExtra : " + mCurrentPhotoPath);

				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(f));
				TLog.v(TAG, "putExtra : ");

			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}

			startActivityForResult(takePictureIntent,
					ConstantUtils.ACTION_PICTURE);
			break;

		case R.id.fillReportIcon:

			status = preferences.getBoolean(ConstantUtils.STATUS, false);
			if (!status) {
				Toast.makeText(mContext, "You should Checkin First",
						Toast.LENGTH_LONG).show();
			} else {
				checkInLayout.setVisibility(View.INVISIBLE);
				mapFrame.setVisibility(View.INVISIBLE);
				promotorList.setVisibility(View.INVISIBLE);
				userForm.setVisibility(View.VISIBLE);

			}
			break;

		case R.id.uploadFormIcon:

			// Camera.
			final List<Intent> cameraIntents = new ArrayList<Intent>();
			final Intent captureIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			final PackageManager packageManager = mActivity.getPackageManager();
			final List<ResolveInfo> listCam = packageManager
					.queryIntentActivities(captureIntent, 0);
			for (ResolveInfo res : listCam) {
				final String packageName = res.activityInfo.packageName;
				final Intent intent = new Intent(captureIntent);
				intent.setComponent(new ComponentName(
						res.activityInfo.packageName, res.activityInfo.name));
				intent.setPackage(packageName);

				try {

					uploadFormSavedFile = UtilityMethods
							.setUpPhotoFile(mContext);

					uploadFormSavedFilePath = uploadFormSavedFile
							.getAbsolutePath();
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							uploadFormSavedFilePath);

				} catch (IOException e) {
					e.printStackTrace();
					uploadFormSavedFile = null;
					uploadFormSavedFilePath = null;
				}
				cameraIntents.add(intent);
			}

			// Filesystem.
			final Intent galleryIntent = new Intent();
			galleryIntent.setType("image/*");
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

			// Chooser of filesystem options.
			final Intent chooserIntent = Intent.createChooser(galleryIntent,
					"Select Source");

			// Add the camera options.
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
					cameraIntents.toArray(new Parcelable[] {}));

			startActivityForResult(chooserIntent,
					ConstantUtils.SELECT_PICTURE_REQUEST_CODE);
			break;
		case R.id.submitFormButton:
			// int numberOfFeilds = userFormListAdapter.getCount();
			// for (int i = 1; i < numberOfFeilds; i++) {
			// String Fvalue = userFormListAdapter.getItem(i).getFieldValue();
			// formSubmitValues = new ArrayList<NameValuePair>();
			// formSubmitValues
			// .add(new BasicNameValuePair("Feild" + i, Fvalue));
			//
			// }
			// JsonSubmitSucessParser jsonSubmitSucessParser = new
			// JsonSubmitSucessParser();
			// jsonSubmitSucessParser.submitFormToDb(formSubmitValues,
			// mContext);
			// if (mResponseData != null) {
			// if (mResponseData.getSuccess()) {
			// Toast.makeText(mContext, "Success", Toast.LENGTH_LONG)
			// .show();
			// } else {
			// Toast.makeText(mContext, "Upload Failed", Toast.LENGTH_LONG)
			// .show();
			// }
			// }
			userForm.setVisibility(View.INVISIBLE);
			promotorList.setVisibility(View.VISIBLE);
			break;

		case R.id.retryButton:
			executeQuery();

			// view.requestLayout();
			break;

		default:
			break;
		}
	}

	private void showOnMap() {

		if (storeLatitude != 0 && storeLongitude != 0) {
			map.clear();
			storeLatLng = new LatLng(storeLatitude, storeLongitude);
			UserLocationManager ulm = new UserLocationManager(this, mContext);
			ulm.getAddress();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					storeLatitude, storeLongitude), 10));
			map.addMarker(new MarkerOptions().position(
					new LatLng(storeLatitude, storeLongitude)).title(
					"Store location"));

		} else {
			// Error message.
			UserLocationManager ulm = new UserLocationManager(this, mContext);
			ulm.getAddress();
			// map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
			// storeLatitude, storeLangitude), 10));
			// map.addMarker(new MarkerOptions().position(
			// new LatLng(storeLatitude, storeLangitude)).title(
			// "Store location"));
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.promotorList:
			try {
				PromotorDetailsFragment promotorDetailsFragment = new PromotorDetailsFragment();
				Bundle mBundle = new Bundle();
				mBundle.putParcelable(ConstantUtils.STORE_DETAILS,
						mStoreDetails);
				mBundle.putParcelable(ConstantUtils.USER_DETAILS,
						mPromotorList.get(position));
				mBundle.putParcelableArrayList(ConstantUtils.USER_FORM_LIST,
						mUserForm);
				promotorDetailsFragment.setArguments(mBundle);
				((SupervisorMotherActivity) mActivity).onItemSelected(
						promotorDetailsFragment, true);

			} catch (Exception e) {
				Toast.makeText((SupervisorMotherActivity) mActivity,
						"ArrayOutOfBounds", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SupportMapFragment f = (SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.map);
		if (f != null)
			getFragmentManager().beginTransaction().remove(f).commit();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		TLog.v(TAG, "onActivityResult : " + requestCode);

		switch (requestCode) {
		case ConstantUtils.ACTION_PICTURE:

			try {
				TLog.v(TAG, "ACTION_PICTURE : " + requestCode);
				String url = UtilityMethods.compressAndUploadImage(
						mCurrentPhotoPath, ConstantUtils.IMAGE_UPLOAD_URL,
						mContext);
				File imgFile = new File(url);
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());

					// Drawable d = new BitmapDrawable(getResources(),
					// myBitmap);
					myImage.setImageBitmap(myBitmap);

					promotorList.setVisibility(View.VISIBLE);
					checkInLayout.setVisibility(View.INVISIBLE);

					status = true;

					if (CheckInIcon.getText().toString()
							.equals(getString(R.string.checkinIcon))) {
						CheckInIcon.setText(R.string.log_out_icon);
						CheckInIcon.setTextColor(getResources().getColor(
								R.color.red));
						checkInText.setText(getString(R.string.log_out));
						fillReportIcon.setTextColor(mContext.getResources()
								.getColor(R.color.lightest_orange));
						status = true;
						preferences.edit()
								.putBoolean(ConstantUtils.STATUS, status)
								.commit();

					} else if (CheckInIcon.getText().toString()
							.equals(getString(R.string.log_out_icon))) {
						CheckInIcon.setText(R.string.checkinIcon);
						checkInText.setText(getString(R.string.check_in));
						CheckInIcon.setTextColor(getResources().getColor(
								R.color.lightest_orange));
						fillReportIcon.setTextColor(getResources().getColor(
								R.color.GreyLineColor));
						status = false;
						preferences.edit()
								.putBoolean(ConstantUtils.STATUS, status)
								.commit();
						userForm.setVisibility(View.INVISIBLE);
						promotorList.setVisibility(View.VISIBLE);
					}
					myImage.setVisibility(View.INVISIBLE);

				}
			} catch (Exception e) {
				TLog.v(TAG, "Error : " + e.toString());
			}

			break;

		case ConstantUtils.SELECT_PICTURE_REQUEST_CODE:

			final boolean isCamera;
			if (data == null) {
				isCamera = true;
			} else {
				isCamera = MediaStore.ACTION_IMAGE_CAPTURE.equals(data
						.getAction());
			}

			Uri selectedImageUri;
			if (isCamera) {
				selectedImageUri = Uri.fromFile(uploadFormSavedFile);
			} else {
				selectedImageUri = data == null ? null : data.getData();
			}

			try {
				TLog.v(TAG, "ACTION_PICTURE : " + requestCode);
				String url = UtilityMethods.compressAndUploadImage(
						uploadFormSavedFilePath,
						ConstantUtils.IMAGE_UPLOAD_URL, mContext);
				File imgFile = new File(url);
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());

					
					// Drawable d = new BitmapDrawable(getResources(),
					// myBitmap);
					myImage.setImageBitmap(myBitmap);

				}
			} catch (Exception e) {
				TLog.v(TAG, "Error : " + e.toString());
			}

			break;
		default:
			break;
		}
	}

	public void executeQuery() {
		promotorListProgressBar.setVisibility(View.VISIBLE);
		promotorList.setVisibility(View.GONE);

		JsonGetPromotorDetails jsonGetPromotorDetails = new JsonGetPromotorDetails();
		StringBuilder url = new StringBuilder();
		url.append(ConstantUtils.USER_DETAILS_URL);
		url.append(mStoreDetails.getId());
		jsonGetPromotorDetails.getPromotorDetailsFromURL(url.toString(), this,
				mContext, "", mStoreDetails.getId(), ConstantUtils.START_COUNT,
				ConstantUtils.NUMBER);
	}

	@Override
	public void showLocation(MyLocation loc) {
		if (loc != null) {
			myLatitude = loc.getLatitude();
			myLongitude = loc.getLongitude();
			map.addMarker(new MarkerOptions().position(
					new LatLng(myLatitude, myLongitude)).title("My location"));

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					myLatitude, myLongitude), 10));

			// map.addMarker(new MarkerOptions().position(
			// new LatLng(storeLatitude, storeLangitude)).title(
			// "Store location"));

			LatLng currentLatLng = new LatLng(myLatitude, myLongitude);

			if (storeLatLng != null) {
				String url = UtilityMethods.getDirectionsUrl(currentLatLng,
						storeLatLng);

				JsonGetStoreDistance jsonGetStoreDistance = new JsonGetStoreDistance(
						url, this, mContext);
			}

		}
	}

	@Override
	public void showMap(double latitude, double longitude) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showDistance(List<List<HashMap<String, String>>> result) {
		ArrayList<LatLng> points = null;
		PolylineOptions lineOptions = null;
		MarkerOptions markerOptions = new MarkerOptions();
		String distance = "";
		String duration = "";

		if (result.size() < 1) {
			Toast.makeText(mContext, "No Points", Toast.LENGTH_SHORT).show();
			return;
		}

		// Traversing through all the routes
		for (int i = 0; i < result.size(); i++) {
			points = new ArrayList<LatLng>();
			lineOptions = new PolylineOptions();

			// Fetching i-th route
			List<HashMap<String, String>> path = result.get(i);

			// Fetching all the points in i-th route
			for (int j = 0; j < path.size(); j++) {
				HashMap<String, String> point = path.get(j);

				if (j == 0) { // Get distance from the list
					distance = (String) point.get("distance");
					continue;
				} else if (j == 1) { // Get duration from the list
					duration = (String) point.get("duration");
					continue;
				}

				double lat = Double.parseDouble(point.get("lat"));
				double lng = Double.parseDouble(point.get("lng"));
				LatLng position = new LatLng(lat, lng);

				points.add(position);
			}

			// Adding all the points in the route to LineOptions
			lineOptions.addAll(points);
			lineOptions.width(2);
			lineOptions.color(Color.RED);
		}

		tvDistanceDuration.setText("Distance:" + distance + ", Duration:"
				+ duration);

		// Drawing polyline in the Google Map for the i-th route
		map.addPolyline(lineOptions);

	}

	@Override
	public void onPromotorDetailsRecieved(SinglePromotorData mSinglePromotorData) {
		if (mSinglePromotorData.getPersonalDetails() != null
				&& mSinglePromotorData.getPersonalDetails().size() > 0) {
			promotorListProgressBar.setVisibility(View.GONE);
			promotorList.setVisibility(View.VISIBLE);
			mPromotorList = mSinglePromotorData.getPersonalDetails();
			if (promotorListAdapter == null)
				promotorListAdapter = new PromotorListAdapter(mContext,
						mSinglePromotorData.getPersonalDetails());
			promotorListAdapter.notifyDataSetChanged();
			promotorList.setAdapter(promotorListAdapter);
		}

	}

	@Override
	public void responseRecieved(ResponseData mResponseData) {
		this.mResponseData = mResponseData;
	}

}
