package com.pulp.campaigntracker.background;

import java.util.List;

import com.pulp.campaigntracker.beans.CheckIn;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.dao.DataBaseHandler;
import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.listeners.MyLocation;
import com.pulp.campaigntracker.parser.JsonCheckinDataParser;
import com.pulp.campaigntracker.parser.JsonLocationDataParser;
import com.pulp.campaigntracker.utils.ConstantUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SendOfflineDataReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (HTTPConnectionWrapper.isNetworkAvailable(context)) {
			Toast.makeText(context,
					"Sending offline data", 5).show();
			DataBaseHandler dataBaseHandler = new DataBaseHandler(context);

			List<CheckIn> checkIns = dataBaseHandler.getAllCheckins();
			JsonCheckinDataParser jsonCheckinDataParser = new JsonCheckinDataParser();
			for (CheckIn checkIn : checkIns) {
				// sending post check in request to server in receiver

				jsonCheckinDataParser.postCheckinDataToURL(
						context,
						checkIn.getAuth_token(), checkIn.getRole(),
						checkIn.getEncodedImage(), checkIn.getUrl(),
						checkIn.getId(), checkIn.getTime(),
						checkIn.getCampaign_id(), checkIn.getStore_id(), true);
				// deleting this record from database
				dataBaseHandler.deleteCheckin(checkIn);

				List<MyLocation> locs = dataBaseHandler.getAllLocations();
				JsonLocationDataParser jsonLocationDataParser = new JsonLocationDataParser();
				for (MyLocation loc : locs) {
					// sending post check in request to server in receiver

					jsonLocationDataParser.postLocationDataToURL(context
							, LoginData.getInstance()
							.getAuthToken(), loc.getCellId(), loc.getLacId(),
							loc.getType(), loc.getTimeStamp(), loc
									.getLatitude(), loc.getLongitude(), true);
					// deleting this record from database
					dataBaseHandler.deletelocation(loc);

				}
			}
		}
	}
}
