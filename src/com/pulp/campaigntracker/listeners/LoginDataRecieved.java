package com.pulp.campaigntracker.listeners;

import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.beans.LoginErrorData;

public interface LoginDataRecieved {

	public void onLoginDataRecieved(LoginData ld);
	public void onLoginErrorDataRecieved(LoginErrorData ld);

}
