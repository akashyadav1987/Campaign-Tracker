package com.pulp.campaigntracker.listeners;


	
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.utils.UtilityMethods;


	public class PhoneCallListener extends PhoneStateListener {

	    public static Boolean phoneRinging = false;

	    public void onCallStateChanged(int state, String incomingNumber) {

	        switch (state) {
	        case TelephonyManager.CALL_STATE_IDLE:
	            Log.d("DEBUG", "IDLE");
	            
	            if (TelephonyManager.CALL_STATE_IDLE == state) {
	                // run when class initial and phone call ended, need detect flag
	                // from CALL_STATE_OFFHOOK
	                if (phoneRinging) {

	                   // Log.i(LOG_TAG, "restart app");
	                	if(LoginData.getInstance()!=null && LoginData.getInstance().getMotherActivity()!=null)
	                		UtilityMethods.restartapp(LoginData.getMotherActivity().getBaseContext(), LoginData.getMotherActivity());
	
	                    phoneRinging = false;
	                }

	            }
	            
	            break;
	        case TelephonyManager.CALL_STATE_OFFHOOK:
	            Log.d("DEBUG", "OFFHOOK");
	            phoneRinging = true;
	            break;
	        case TelephonyManager.CALL_STATE_RINGING:
	            Log.d("DEBUG", "RINGING");
	          

	            break;
	        }
	    }

	}

