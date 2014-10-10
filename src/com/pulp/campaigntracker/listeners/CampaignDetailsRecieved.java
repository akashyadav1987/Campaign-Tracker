package com.pulp.campaigntracker.listeners;

import java.util.ArrayList;

import com.pulp.campaigntracker.beans.CampaignDetails;

public interface CampaignDetailsRecieved {

	/**
	 *  Campaign List for supervisor
	 * @param campaignDetailsList
	 */
	public void onCampaignDetailsRecieved(ArrayList<CampaignDetails> campaignDetailsList);
	
	/**
	 * Campaign Object for promotor
	 * @param cd
	 */
	public void onCampaignDetailsRecieved(CampaignDetails cd);

}
