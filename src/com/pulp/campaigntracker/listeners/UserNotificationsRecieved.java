package com.pulp.campaigntracker.listeners;

import java.util.List;

import com.pulp.campaigntracker.beans.UserNotification;


public interface UserNotificationsRecieved {

	public void onRecievedNotifcationList(List<UserNotification> notificationList);
}
