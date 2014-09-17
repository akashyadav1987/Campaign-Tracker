package com.pulp.campaigntracker.listeners;

import java.util.List;

import com.pulp.campaigntracker.beans.UserFormDetails;

public interface UserFormFieldRecieved {

	public void onFormDataRecieved(List<UserFormDetails> userFormDetails);
}
