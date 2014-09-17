package com.pulp.campaigntracker.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pulp.campaigntracker.ui.PromotorIconFragment;
import com.pulp.campaigntracker.ui.SupervisorIconFragment;

public class ScreenPagerAdapter extends FragmentStatePagerAdapter {

	public ScreenPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		
		switch(index){
		case 0:
			return new PromotorIconFragment();
		case 1:
			return new SupervisorIconFragment();
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
