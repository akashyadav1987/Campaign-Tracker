package com.pulp.campaigntracker.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.background.PeriodicLocation;
import com.pulp.campaigntracker.background.PeriodicService;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.UserFormDetails;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.controllers.CheckInStatusController;
import com.pulp.campaigntracker.controllers.NotificationListFragment;
import com.pulp.campaigntracker.controllers.SupervisorListAdapter;
import com.pulp.campaigntracker.controllers.UserFormAdapter;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.CampaignDetailsRecieved;
import com.pulp.campaigntracker.listeners.GetStoreDistanceRecieved;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.listeners.UpdateLocation;
import com.pulp.campaigntracker.listeners.UserFormFieldRecieved;
import com.pulp.campaigntracker.listeners.UserLocationManager;
import com.pulp.campaigntracker.parser.JsonGetCampaignDetails;
import com.pulp.campaigntracker.parser.JsonGetStoreDistance;
import com.pulp.campaigntracker.utils.ConstantUtils;
//import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class PromotorMainScreenFragment extends Fragment implements
		CampaignDetailsRecieved, android.view.View.OnClickListener,
		UserFormFieldRecieved, UpdateLocation, GetStoreDistanceRecieved {

	private Activity mActivity;
	private Context mContext;

	private static final String TAG = PromotorMainScreenFragment.class
			.getSimpleName();
	JsonGetCampaignDetails jsonGetCampaignDetails;

	private Typeface iconFonts;

	private TextView storeName;
	private TextView pincode;
	private TextView state;
	private TextView addLine1;
	private TextView CheckInIcon;
	private TextView RouteMapIcon;
	private TextView fillReportIcon;
	private ListView userForm;
	private List<UserFormDetails> mUserForm;
	private UserFormAdapter userFormListAdapter;

	private ListView supervisorList;
	private List<UserProfile> mSupervisorList;
	private SupervisorListAdapter supervisorListAdapter;
	private String mCurrentPhotoPath;

	private RelativeLayout promoterCheckInFrame;
	private RelativeLayout storeDetails;
	private RelativeLayout checkInLayout;

	private RelativeLayout errorLayout;
	private FrameLayout mapFrame;
	private GoogleMap map;
	private TextView userIcon;
	private double storeLatitude;
	private double storeLangitude;
	private String campaignName;
	private ImageView myImage;
	private double myLatitude;
	private double myLongitude;
	private TextView tvDistanceDuration;
	private LatLng storeLatLng;
	private TextView compaginLogo;
	private TextView uploadFormIcon;
	private TextView uploadForm;
	private Button submitButton;
	private View buttonLayoutView;
	private TextView postAnImage;

	private boolean status = false;
	SharedPreferences preferences;
	private String uploadFormSavedFilePath;
	File uploadFormSavedFile = null;
	LoginData mLoginData = LoginData.getInstance();
	private boolean bCheckInLayout = false;
	// private Uri selectedImageUri;
	private TextView userListIcon;
	private TextView checkInText;
	private TextView errorImage;
	private Button retryButton;
	private Dialog mProgressDialog;

	private CheckInStatusController mCheckInStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
		mContext = getActivity().getBaseContext();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		iconFonts = Typeface.createFromAsset(mContext.getAssets(),
				"icomoon.ttf");
		View view = inflater.inflate(R.layout.promoter_main_screen, container,
				false);

		mCheckInStatus = new CheckInStatusController(mContext);

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

		supervisorList = (ListView) view.findViewById(R.id.supervisorList);

		storeDetails = (RelativeLayout) view.findViewById(R.id.storeDetails);

		postAnImage = (TextView) checkInLayout.findViewById(R.id.postImageText);
		storeName = (TextView) view.findViewById(R.id.storeName);
		addLine1 = (TextView) view.findViewById(R.id.addLine1);
		state = (TextView) view.findViewById(R.id.storeState);
		pincode = (TextView) view.findViewById(R.id.storePincode);
		myImage = (ImageView) checkInLayout.findViewById(R.id.imageCaptured);
		myImage.setVisibility(View.INVISIBLE);

		userIcon = (TextView) view.findViewById(R.id.userIcon);
		userIcon.setTypeface(iconFonts);

		userListIcon = (TextView) storeDetails.findViewById(R.id.userListIcon);
		userListIcon.setTypeface(iconFonts);

		CheckInIcon = (TextView) storeDetails
				.findViewById(R.id.storeCheckInIcon);
		CheckInIcon.setTypeface(iconFonts);
		checkInText = (TextView) storeDetails.findViewById(R.id.checkInText);

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

		supervisorList.setVisibility(View.VISIBLE);
		mapFrame.setVisibility(View.GONE);
		checkInLayout.setVisibility(View.GONE);
		userForm.setVisibility(View.GONE);

		CheckInIcon.setOnClickListener(this);
		RouteMapIcon.setOnClickListener(this);
		checkInLayout.setOnClickListener(this);
		fillReportIcon.setOnClickListener(this);
		uploadFormIcon.setOnClickListener(this);
		submitButton.setOnClickListener(this);
		userListIcon.setOnClickListener(this);

		userListIcon
				.setTextColor(mContext.getResources().getColor(R.color.red));

		if (mCheckInStatus.getCheckInStatus()) {

			checkInText.setText(getString(R.string.check_out));
			CheckInIcon.setText(getString(R.string.check_out_icon));
			fillReportIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			mCheckInStatus.updateCheckInStatus(true);

			ConstantUtils.SYNC_INTERVAL = 60 * 60 * 1000;
			Intent i = new Intent(mContext, PeriodicService.class);
			mContext.startService(i);
		} else {
			checkInText.setText(getString(R.string.check_in));
			CheckInIcon.setText(getString(R.string.checkinIcon));
			fillReportIcon.setTextColor(mContext.getResources().getColor(
					R.color.GreyLineColor));
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			mCheckInStatus.updateCheckInStatus(false);

			Intent intent = new Intent(mContext, PeriodicLocation.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getActivity().getApplicationContext(), 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) getActivity()
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			Intent serviceI = new Intent(mContext, PeriodicService.class);
			mContext.stopService(serviceI);

		}

		errorLayout = (RelativeLayout) view.findViewById(R.id.errorLayout);
		errorImage = (TextView) errorLayout.findViewById(R.id.errorImage);
		retryButton = (Button) errorLayout.findViewById(R.id.retryButton);
		retryButton.setOnClickListener(this);
		errorImage.setTypeface(iconFonts);

		if (!HTTPConnectionWrapper.isNetworkAvailable(mContext))
			errorLayout.setVisibility(View.VISIBLE);
		else
			errorLayout.setVisibility(View.INVISIBLE);

		executeQuery();

		return view;
	}

	@Override
	public void onCampaignDetailsRecieved(
			ArrayList<CampaignDetails> campaignDetailsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
	}

	@Override
	public void onCampaignDetailsRecieved(CampaignDetails cd) {
		mProgressDialog.dismiss();
		if (cd != null) {
			addLine1.setText(cd.getStoreList().get(0).getAddress());
			pincode.setText(cd.getStoreList().get(0).getPincode());
			state.setText(cd.getStoreList().get(0).getState());
			storeName.setText(cd.getStoreList().get(0).getName());
			storeLatitude = cd.getStoreList().get(0).getLatitude();
			storeLangitude = cd.getStoreList().get(0).getLongitude();

			Bitmap bitmap = cd.getStoreList().get(0).getStoreImage();

			if (bitmap != null) {
				Drawable background = new BitmapDrawable(getResources(), bitmap);
				setImageBackground(background);

			}

			campaignName = cd.getName();
			if (campaignName != null)
				((PromotorMotherActivity) LoginData.getMotherActivity())
						.setActionBarTitle(campaignName);

			TLog.v(TAG, "storeLatitude : " + storeLatitude);
			TLog.v(TAG, "storeLangitude : " + storeLangitude);

			if (cd.getUserFormDetailsList() != null
					&& cd.getUserFormDetailsList().size() > 0) {
				this.mUserForm = cd.getUserFormDetailsList();

				if (userFormListAdapter == null)
					userFormListAdapter = new UserFormAdapter(mContext,
							mUserForm);

				userFormListAdapter.notifyDataSetChanged();

				if (userForm.getFooterViewsCount() == 0) {
					userForm.addFooterView(buttonLayoutView);
				}
				userForm.setAdapter(userFormListAdapter);
			}

		}

		if (cd.getUserList() != null && cd.getUserList().size() > 0) {
			this.mSupervisorList = cd.getUserList();

			if (supervisorListAdapter == null)
				supervisorListAdapter = new SupervisorListAdapter(mContext,
						mSupervisorList);

			supervisorListAdapter.notifyDataSetChanged();
			supervisorList.setAdapter(supervisorListAdapter);
		}

	}

	public void executeQuery() {

		mProgressDialog = new Dialog(mActivity,
				R.style.transparent_dialog_no_titlebar);

		// TLog.i(TAG, "show mProgressDialog " + mProgressDialog);
		mProgressDialog.setContentView(R.layout.please_wait_dialog);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mProgressDialog.cancel();
				mActivity.onBackPressed();

			}
		});

		jsonGetCampaignDetails = JsonGetCampaignDetails.getInstance();
		jsonGetCampaignDetails.getCampaignDetailsFromURL(
				ConstantUtils.CAMPAIGN_DETAILS_URL, this, LoginData
						.getInstance().getId(), LoginData.getInstance()
						.getAuthToken(),  LoginData.getInstance()
						.getRole(), mContext);

	}

	@Override
	public void onClick(View v) {

		EasyTracker easyTracker = EasyTracker.getInstance(mActivity);
		switch (v.getId()) {
		case R.id.checkInLayout_ref:
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			File f = null;

			try {

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

		case R.id.userListIcon:
			supervisorList.setVisibility(View.VISIBLE);
			checkInLayout.setVisibility(View.INVISIBLE);
			mapFrame.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			bCheckInLayout = false;

			/**
			 * Change color of tabs
			 */
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			userListIcon.setTextColor(mContext.getResources().getColor(
					R.color.red));

			if (mCheckInStatus.getCheckInStatus()) {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));

			} else {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));

			}

			RouteMapIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			break;

		case R.id.storeRouteMapIcon:

			easyTracker.send(MapBuilder.createEvent("promotor_Check", // Event
					// category
					// (required)
					"button_press", // Event action (required)
					"Route_Map", // Event label
					null) // Event value
					.build());

			supervisorList.setVisibility(View.INVISIBLE);
			checkInLayout.setVisibility(View.INVISIBLE);
			mapFrame.setVisibility(View.VISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			showOnMap();

			bCheckInLayout = true;

			/**
			 * Change color of tabs
			 */
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			userListIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			// fillReportIcon.setTextColor(mContext.getResources().getColor(
			// R.color.lightest_orange));
			RouteMapIcon.setTextColor(mContext.getResources().getColor(
					R.color.red));

			if (mCheckInStatus.getCheckInStatus()) {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));

			} else {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));

			}
			break;

		case R.id.storeCheckInIcon:

			easyTracker.send(MapBuilder.createEvent("promotor_Check", // Event
					// category
					// (required)
					"button_press", // Event action (required)
					"Store_CheckIn", // Event label
					null) // Event value
					.build());

			supervisorList.setVisibility(View.INVISIBLE);
			checkInLayout.setVisibility(View.VISIBLE);
			mapFrame.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);

			if (mCheckInStatus.getCheckInStatus()) {
				myImage.setVisibility(View.INVISIBLE);
				postAnImage.setVisibility(View.VISIBLE);
			} else {
				if (!myImage.isShown())
					postAnImage.setVisibility(View.VISIBLE);
				else
					postAnImage.setVisibility(View.INVISIBLE);
			}

			if (mCheckInStatus.getCheckInStatus()) {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));

			} else {
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.GreyLineColor));

			}
			bCheckInLayout = true;

			/**
			 * Change color of tabs
			 */
			CheckInIcon.setTextColor(mContext.getResources().getColor(
					R.color.red));
			userListIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));
			// fillReportIcon.setTextColor(mContext.getResources().getColor(
			// R.color.lightest_orange));
			RouteMapIcon.setTextColor(mContext.getResources().getColor(
					R.color.lightest_orange));

			break;

		case R.id.fillReportIcon:

			easyTracker.send(MapBuilder.createEvent("promotor_Check", // Event
					// category
					// (required)
					"button_press", // Event action (required)
					"Fill_Report", // Event label
					null) // Event value
					.build());

			if (!mCheckInStatus.getCheckInStatus()) {

				Toast.makeText(mContext, "You should Checkin First",
						Toast.LENGTH_LONG).show();
			} else {
				checkInLayout.setVisibility(View.INVISIBLE);
				mapFrame.setVisibility(View.INVISIBLE);
				supervisorList.setVisibility(View.INVISIBLE);
				userForm.setVisibility(View.VISIBLE);

				/**
				 * Change color of tabs
				 */
				CheckInIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				userListIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));
				fillReportIcon.setTextColor(mContext.getResources().getColor(
						R.color.red));
				RouteMapIcon.setTextColor(mContext.getResources().getColor(
						R.color.lightest_orange));

			}
			bCheckInLayout = true;

			break;

		case R.id.uploadFormIcon:

			easyTracker.send(MapBuilder.createEvent("promotor_Check", // Event
					// category
					// (required)
					"button_press", // Event action (required)
					"Upload_Form", // Event label
					null) // Event value
					.build());

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

			easyTracker.send(MapBuilder.createEvent("promotor_Check", // Event
					// category
					// (required)
					"button_press", // Event action (required)
					"Submit_Form", // Event label
					null) // Event value
					.build());
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
			supervisorList.setVisibility(View.VISIBLE);
			break;
		case R.id.retryButton:
			executeQuery();
			// view.requestLayout();
			break;
		default:
			break;

		}

	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.notifications:
			NotificationListFragment notificationListFragment = new NotificationListFragment();
			((PromotorMotherActivity) mActivity).onItemSelected(
					notificationListFragment, true);

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	public boolean onBack() {
		// TODO Auto-generated method stub

		if (bCheckInLayout) {
			storeDetails.setVisibility(View.VISIBLE);
			supervisorList.setVisibility(View.VISIBLE);
			checkInLayout.setVisibility(View.INVISIBLE);
			mapFrame.setVisibility(View.INVISIBLE);
			userForm.setVisibility(View.INVISIBLE);
			storeDetails.invalidate();

		}

		boolean temp = bCheckInLayout;
		bCheckInLayout = false;
		return temp;

	}

	private void showOnMap() {

		if (storeLatitude != 0 && storeLangitude != 0) {
			map.clear();
			storeLatLng = new LatLng(storeLatitude, storeLangitude);
			UserLocationManager ulm = new UserLocationManager(this, mContext);
			ulm.getAddress();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					storeLatitude, storeLangitude), 10));
			map.addMarker(new MarkerOptions().position(
					new LatLng(storeLatitude, storeLangitude)).title(
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

					myImage.setImageBitmap(myBitmap);
					myImage.setVisibility(View.VISIBLE);
					postAnImage.setVisibility(View.INVISIBLE);
					checkInLayout.setVisibility(View.VISIBLE);

					if (!mCheckInStatus.getCheckInStatus()) {
						checkInText.setText(getString(R.string.check_out));
						fillReportIcon.setTextColor(mContext.getResources()
								.getColor(R.color.lightest_orange));

						CheckInIcon.setText(getString(R.string.check_out_icon));
						CheckInIcon.setTextColor(mContext.getResources()
								.getColor(R.color.red));

						ConstantUtils.SYNC_INTERVAL = 20 * 1000 * 60;
						Intent i = new Intent(mContext, PeriodicService.class);
						mContext.startService(i);

						mCheckInStatus.updateCheckInStatus(true);

					} else {
						checkInText.setText(getString(R.string.check_in));
						fillReportIcon.setTextColor(mContext.getResources()
								.getColor(R.color.GreyLineColor));

						CheckInIcon.setText(getString(R.string.checkinIcon));
						CheckInIcon.setTextColor(mContext.getResources()
								.getColor(R.color.red));

						mCheckInStatus.updateCheckInStatus(false);
						userForm.setVisibility(View.INVISIBLE);
						supervisorList.setVisibility(View.VISIBLE);

						Intent intent = new Intent(mContext,
								PeriodicLocation.class);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(getActivity()
										.getApplicationContext(), 0, intent,
										PendingIntent.FLAG_UPDATE_CURRENT);
						AlarmManager alarmManager = (AlarmManager) getActivity()
								.getSystemService(Context.ALARM_SERVICE);
						alarmManager.cancel(pendingIntent);
						Intent serviceI = new Intent(mContext,
								PeriodicService.class);
						mContext.stopService(serviceI);
					}

				}
			} catch (Exception e) {
				TLog.v(TAG, "Error : " + e.toString());
				myImage.setVisibility(View.INVISIBLE);
			}

			break;

		case ConstantUtils.SELECT_PICTURE_REQUEST_CODE:

			final boolean isCamera;
			if (data == null) {
				isCamera = true;
			} else {
				Uri action = data.getData();
				if (action == null) {
					isCamera = false;
				} else {
					isCamera = action
							.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				}
			}

			// if (isCamera) {
			// selectedImageUri = Uri.fromFile(uploadFormSavedFile);
			// } else {
			// selectedImageUri = data.getData();
			// }

			try {
				TLog.v(TAG, "ACTION_PICTURE : " + requestCode);
				String url = UtilityMethods.compressAndUploadImage(
						uploadFormSavedFilePath,
						ConstantUtils.IMAGE_UPLOAD_URL, mContext);
				File imgFile = new File(url);
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					// TLog.v(TAG, "myBitmap : " + myBitmap.getByteCount());

					myImage.setImageBitmap(myBitmap);

				}
			} catch (Exception e) {
				TLog.v(TAG, "Error : " + e.toString());
				myImage.setVisibility(View.INVISIBLE);

			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onFormDataRecieved(List<UserFormDetails> userFormDetails) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pulp.campaigntracker.listeners.UpdateLocation#showLocation(com.pulp
	 * .campaigntracker.listeners.MyLocation) Retrieve the current Location
	 * Object
	 */
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

				new JsonGetStoreDistance(url, this, mContext);
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
		new MarkerOptions();
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

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setImageBackground(Drawable background) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			compaginLogo.setBackground(background);
		} else {
			compaginLogo.setBackgroundDrawable(background);
		}
	}

}