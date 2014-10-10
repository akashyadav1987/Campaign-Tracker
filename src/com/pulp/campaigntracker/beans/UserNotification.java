package com.pulp.campaigntracker.beans;

import java.io.Serializable;

public class UserNotification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -628114057342131945L;
	String title;
	String message;
	
	long notifyTime;

	public long getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(long notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
