package com.pulp.campaigntracker.listeners;


	
import com.pulp.campaigntracker.beans.LoginData;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


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
	                		LoginData.getInstance().getMotherActivity().restartapp();
	
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
