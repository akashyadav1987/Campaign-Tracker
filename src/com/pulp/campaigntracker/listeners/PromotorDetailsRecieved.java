package com.pulp.campaigntracker.listeners;

import java.util.ArrayList;
import java.util.List;

import com.pulp.campaigntracker.beans.SinglePromotorData;
import com.pulp.campaigntracker.beans.UserProfile;

public interface PromotorDetailsRecieved {

	public void onPromotorDetailsRecieved(SinglePromotorData mSinglePromotorData);
	
}
