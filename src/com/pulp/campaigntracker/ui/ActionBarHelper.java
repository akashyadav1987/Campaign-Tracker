package com.pulp.campaigntracker.ui;
import android.support.v7.app.ActionBarActivity;


public class ActionBarHelper {

	private final android.support.v7.app.ActionBar mActionBar;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	ActionBarHelper(ActionBarActivity activity) {
		mActionBar = activity.getSupportActionBar();
	}

	public void init() {

		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);

	}

	public void setTitle(CharSequence title) {
		mTitle = title;
		mActionBar.setTitle(mTitle);
	}

}
