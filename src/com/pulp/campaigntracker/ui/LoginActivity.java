package com.pulp.campaigntracker.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.LoginErrorData;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.LoginDataRecieved;
import com.pulp.campaigntracker.parser.JsonLoginDataParser;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.UtilityMethods;


public class LoginActivity extends ActionBarActivity implements
		LoginDataRecieved, OnClickListener, OnGlobalLayoutListener {

	JsonLoginDataParser jsonLoginDataParser;

	private EditText mobileNo;
	private EditText emailId;
	private EditText userPassword;
	private Button loginButton;


	private String TAG = LoginActivity.class.getSimpleName();
	private Typeface iconFonts;
	private TextView phoneIcon;
	private TextView emailIcon;
	private TextView passwordIcon;

	private TextView errorText;
	private String role;
	private TextView supervisorIcon;
	private TextView promoterIcon;

	private TextView userLabel;

	private ProgressDialog mProgressDialog;

	private Button erroDialogButton;

	private View activityRootView;

	private View view1;
	private ActionBarHelper mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_feilds_layout);

		iconFonts = Typeface.createFromAsset(getBaseContext().getAssets(),
				"icomoon.ttf");

		mobileNo = (EditText) findViewById(R.id.mobileNumber);
		emailId = (EditText) findViewById(R.id.emailId);
		userPassword = (EditText) findViewById(R.id.password);

		activityRootView = findViewById(R.id.activityRoot);

		view1 = findViewById(R.id.view1);
		view1.setVisibility(View.VISIBLE);

		phoneIcon = (TextView) findViewById(R.id.phoneIcon);
		phoneIcon.setTypeface(iconFonts);

		emailIcon = (TextView) findViewById(R.id.emailIcon);
		emailIcon.setTypeface(iconFonts);

		passwordIcon = (TextView) findViewById(R.id.passwordIcon);
		passwordIcon.setTypeface(iconFonts);

		supervisorIcon = (TextView) findViewById(R.id.supervisorIcon);
		supervisorIcon.setTypeface(iconFonts);

		promoterIcon = (TextView) findViewById(R.id.promotorIcon);
		promoterIcon.setTypeface(iconFonts);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		userLabel = (TextView) findViewById(R.id.userLabel);

		Intent loginIntent = getIntent();
		role = loginIntent.getStringExtra("UserType");
		
		
		promoterIcon.setVisibility(View.VISIBLE);
		userPassword.setVisibility(View.VISIBLE);
		userLabel.setText("User");
		
		
		mActionBar = createActionBarHelper();
		mActionBar.init();
		mActionBar.setTitle("Field Force Automation");

		LoginData.setLoginActivity(this);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

	}

	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper(this);
	}

	public void executeQuery(String email, String password, String number,
			String role, String gcm_Token) {


		mProgressDialog = ProgressDialog.show(this,
				"Logging in ...", "Please Wait ...", true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
		

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String device_id = telephonyManager.getDeviceId();

		jsonLoginDataParser = new JsonLoginDataParser();
		jsonLoginDataParser.getLoginDataFromURL(ConstantUtils.LOGIN_URL, this,
				email, password, number, gcm_Token, device_id);
	}

	private void storeLoginInfo() {
		UtilityMethods.setLoginDataInPref(getApplicationContext(),
				ConstantUtils.AUTH_TOKEN, LoginData.getInstance()
						.getAuthToken());
		UtilityMethods
				.setLoginDataInPref(getApplicationContext(),
						ConstantUtils.LOGIN_NAME, LoginData.getInstance()
								.getUsername());
		UtilityMethods.setLoginDataInPref(getApplicationContext(),
				ConstantUtils.LOGIN_ID, LoginData.getInstance().getId());
		UtilityMethods.setLoginDataInPref(getApplicationContext(),
				ConstantUtils.USER_EMAIL, LoginData.getInstance().getEmail());
		UtilityMethods.setLoginDataInPref(getApplicationContext(),
				ConstantUtils.USER_ROLE, LoginData.getInstance().getRole());
		UtilityMethods
				.setLoginDataInPref(getApplicationContext(),
						ConstantUtils.USER_NUMBER, LoginData.getInstance()
								.getPhoneNo());
			}

	@Override
	public void onLoginDataRecieved(LoginData ld) {
		// TODO Auto-generated method stub

		storeLoginInfo();

		if(mProgressDialog!=null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
			mProgressDialog=null;
		}

		Intent intent = new Intent(LoginActivity.this,
				UserMotherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
		finish();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void alertBuilder(String message) {
		final AlertDialog alert;
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.transparent_dialog_no_titlebar);

		LayoutInflater inflater = getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.error_dialog, null);
		builder.setView(dialoglayout);

		alert = builder.create();

		errorText = (TextView) dialoglayout.findViewById(R.id.errorText);
		errorText.setText(message);
		alert.show();

		erroDialogButton = (Button) dialoglayout.findViewById(R.id.cancel);
		erroDialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				alert.dismiss();

			}
		});

	}

	@Override
	public void onLoginErrorDataRecieved(LoginErrorData ld) {

		// to be used when Credentials not match..
		final AlertDialog alert = null;

		mProgressDialog.dismiss();
		if (ld.getMessage() != null) {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				alertBuilder(ld.getMessage());
			} else {
				LayoutInflater inflater = getLayoutInflater();
				View dialoglayout = inflater.inflate(R.layout.error_dialog,
						null);
				errorText = (TextView) dialoglayout
						.findViewById(R.id.errorText);
				errorText.setText(ld.getMessage());
				alert.show();

				erroDialogButton = (Button) dialoglayout
						.findViewById(R.id.cancel);
				erroDialogButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();

					}
				});

			}

		}

	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.loginButton:

			if (!HTTPConnectionWrapper.isNetworkAvailable(LoginActivity.this))
				UtilityMethods.ShowAlertDialog(LoginActivity.this);
			else {
				if (!PhoneNumberUtils.isGlobalPhoneNumber(mobileNo.getText()
						.toString())) {

					Toast.makeText(getApplicationContext(),
							"Enter Phone Number", Toast.LENGTH_SHORT).show();
					break;

				}
				if (!UtilityMethods.isValidEmail(emailId.getText().toString())) {
					Toast.makeText(getApplicationContext(), "Enter Email",
							Toast.LENGTH_SHORT).show();
					break;

				}

				String regid = UtilityMethods
						.getRegistrationId(getApplicationContext());
					
				executeQuery(emailId.getText().toString(),
						userPassword.getText().toString(), mobileNo
									.getText().toString(),
							LoginData.getInstance().getRole(), regid);
			}

			break;
			

		default:
			break;
		}
	}

	@Override
	public void onGlobalLayout() {
		int heightDiff = activityRootView.getRootView().getHeight()
				- activityRootView.getHeight();
		if (heightDiff > 120) { // if more than 100 pixels, its probably a
								// keyboard...
			userLabel.setVisibility(View.INVISIBLE);
			promoterIcon.setVisibility(View.INVISIBLE);
			supervisorIcon.setVisibility(View.INVISIBLE);
			view1.setVisibility(View.INVISIBLE);
		} else {
			userLabel.setVisibility(View.VISIBLE);
			promoterIcon.setVisibility(View.VISIBLE);
			supervisorIcon.setVisibility(View.VISIBLE);
			view1.setVisibility(View.VISIBLE);
		}
	}
}
