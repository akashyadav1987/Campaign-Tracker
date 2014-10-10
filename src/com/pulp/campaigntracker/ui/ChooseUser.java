package com.pulp.campaigntracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.controllers.ScreenPagerAdapter;
import com.pulp.campaigntracker.utils.ConstantUtils;
import com.pulp.campaigntracker.utils.TLog;
import com.pulp.campaigntracker.utils.UtilityMethods;
import com.pulp.campaigntracker.utils.ConstantUtils.LoginType;
import com.pulp.campaigntracker.utils.TypeFaceUtil;
import com.pulp.campaigntracker.utils.TypeFaceUtil.EnumCustomTypeFace;

public class ChooseUser extends ActionBarActivity implements
OnPageChangeListener, OnClickListener{

	private static final String TAG = ChooseUser.class.getSimpleName();
	private ViewPager roleSelector;
	private TextView backwardIcon;
	private TextView forwardIcon;
	private Button loginButton;
	private String role = LoginType.promotor.toString();
	private ScreenPagerAdapter mPagerAdapter;
	private TextView chooseAccount;
	private int mCurrentPosition;
	private int mScrollState;
	
	private ActionBarHelper mActionBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_fragment_holder);
		roleSelector = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenPagerAdapter(getSupportFragmentManager());
		roleSelector.setAdapter(mPagerAdapter);
		roleSelector.setOnPageChangeListener(this);

		backwardIcon = (TextView) findViewById(R.id.backwardIcon);
		TypeFaceUtil.getInstance(getBaseContext()).setCustomTypeFaceText(EnumCustomTypeFace.ICOMOON, backwardIcon);
		backwardIcon.setOnClickListener(this);

		forwardIcon = (TextView) findViewById(R.id.forwardIcon);
		TypeFaceUtil.getInstance(getBaseContext()).setCustomTypeFaceText(EnumCustomTypeFace.ICOMOON, forwardIcon);
		forwardIcon.setOnClickListener(this);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);

	
		TypeFaceUtil.getInstance(getBaseContext()).setCustomTypeFaceButton(EnumCustomTypeFace.TITILLIUM_DARK, loginButton);
	
		chooseAccount = (TextView) findViewById(R.id.chooseAccount);
		TypeFaceUtil.getInstance(getBaseContext()).setCustomTypeFaceText(EnumCustomTypeFace.TITILLIUM_DARK, chooseAccount);

		
		mActionBar = createActionBarHelper();
		mActionBar.init();
		mActionBar.setTitle("Campaign Tracker");

	}
	
	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!(UtilityMethods.getLoginPreferences(getApplicationContext()).getString(ConstantUtils.USER_EMAIL, "").isEmpty()))
				finish();
				
	}
	@Override
    public void onPageSelected(final int position) {
        mCurrentPosition = position;
        
        switch (position) {
		case 0:
			role = LoginType.promotor.toString();
			break;
		case 1:
			role =  LoginType.supervisor.toString();
			break;
		default:
			break;
		}
        
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        handleScrollState(state);
        mScrollState = state;
    }

    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setNextItemIfNeeded();
        }
    }

    private void setNextItemIfNeeded() {
        if (!isScrollStateSettling()) {
            handleSetNextItem();
        }
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = roleSelector.getAdapter().getCount() - 1;
        if(mCurrentPosition == 0) {
        	roleSelector.setCurrentItem(lastPosition, false);
        } else if(mCurrentPosition == lastPosition) {
        	roleSelector.setCurrentItem(0, false);
        }
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }



	@Override
	public void onClick(View v) {


		switch (v.getId()) {
		case R.id.loginButton:
			
			Intent loginIntent = new Intent(ChooseUser.this,
					LoginActivity.class);
			loginIntent.putExtra("UserType", role);
			startActivity(loginIntent);
			break;



		case R.id.backwardIcon:
			if (role.equals(LoginType.promotor.toString())) {
				roleSelector.setCurrentItem(1, true);
			} else if (role.equals(LoginType.supervisor.toString())) {
				roleSelector.setCurrentItem(0, true);
			}
			break;
			
			
		case R.id.forwardIcon:
			if (role.equals(LoginType.promotor.toString())) {
				roleSelector.setCurrentItem(1, true);
			} else if (role.equals(LoginType.supervisor.toString())) {
				roleSelector.setCurrentItem(0, true);
			}
			break;



		default:
			break;
		}
	}


	


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
