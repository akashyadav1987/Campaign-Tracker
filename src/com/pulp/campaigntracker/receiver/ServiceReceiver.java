package com.pulp.campaigntracker.receiver;


import com.pulp.campaigntracker.listeners.PhoneCallListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ServiceReceiver extends BroadcastReceiver {
    TelephonyManager telephony=null;
    PhoneCallListener phoneListener=null;

    public void onReceive(Context context, Intent intent) {
        phoneListener = new PhoneCallListener();
        telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void onDestroy() {
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
    }

}