package com.pulp.campaigntracker.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.CampaignDetails;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.controllers.StoreDetailsAdapter;
import com.pulp.campaigntracker.listeners.CampaignDetailsRecieved;
import com.pulp.campaigntracker.parser.JsonGetCampaignDetails;
import com.pulp.campaigntracker.utils.ConstantUtils;


public class CampaignDetailsActivity extends Activity implements CampaignDetailsRecieved,OnClickListener{

	private JsonGetCampaignDetails jsonGetCampaignDetails;
	private TextView supervisorDetailsHeading;
	private TextView supervisorName;
	private TextView supervisorContactNumber;
	private TextView supervisorCallIcon;

	private StoreDetailsAdapter sda;
	private ListView campaignStoreList;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		setContentView(R.layout.campaign_details_screen);


		supervisorDetailsHeading = (TextView)findViewById(R.id.supervisorDetailsHeading);
		supervisorName = (TextView)findViewById(R.id.supervisorName);
		supervisorContactNumber = (TextView)findViewById(R.id.supervisorContactNumber);
		supervisorCallIcon = (TextView)findViewById(R.id.supervisorCallIcon);
		campaignStoreList = (ListView)findViewById(R.id.campaignStoreList);

		executeQuery();
	}

	/**
	 * Gets the data for the campaign from server in the background thread.
	 */
	public void executeQuery()
	{
		jsonGetCampaignDetails = JsonGetCampaignDetails.getInstance();
		jsonGetCampaignDetails.getCampaignDetailsFromURL(ConstantUtils.CAMPAIGN_DETAILS_URL, this,LoginData.getInstance().getId(), LoginData.getInstance().getAuthToken(), LoginData.getInstance().getRole(),getBaseContext());
		//jsonGetCampaignDetails.getCampaignDetailsFromURL(ConstantUtils.CAMPAIGN_DETAILS_URL, this, LoginType.supervisor,getBaseContext());
	}


	@Override
	public void onCampaignDetailsRecieved(CampaignDetails cd) {

		if(cd!=null)
		{
			supervisorName.setText(cd.getImmediateManager().getName());
			supervisorContactNumber.setText(cd.getImmediateManager().getContactNumber());

			if(cd.getStoreList()!=null && cd.getStoreList().size()>0)
			{
				if(sda == null)
					sda = new StoreDetailsAdapter(getBaseContext(), cd.getStoreList());
				else
					sda.notifyDataSetChanged();
				
				campaignStoreList.setAdapter(sda);
			}
		}
	}

	@Override
	public void onCampaignDetailsRecieved(
			ArrayList<CampaignDetails> campaignDetailsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
