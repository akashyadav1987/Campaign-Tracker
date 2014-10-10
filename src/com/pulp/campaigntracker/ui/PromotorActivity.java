package com.pulp.campaigntracker.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserProfile;
import com.pulp.campaigntracker.listeners.UserDataRecieved;

public class PromotorActivity extends ActionBarActivity implements UserDataRecieved{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promoter_main_screen);
		
			}
	@Override
	public void onUserDataRecieved(UserProfile up) {
		// TODO Auto-generated method stub
		
	}

}
