package com.pulp.campaigntracker.beans;

public class InitData {

	//Single instance for Login
	public static InitData instance;

	public static InitData getInstance() {
	        if (instance == null)
	              instance = new InitData();
	        return instance;
	}

	int locationPeriodicInterval;
	String locationStartInterval;
	String locationEndInterval;
	int locationBatteryStatus;
	int syncUnsentDataInterval;
	String device_id;
	
	public String getDeviceID() {
		return device_id;
	}
	public void setDeviceID(String device_id) {
		this.device_id = device_id;
	}
	
	public int getLocationPeriodicInterval() {
		return locationPeriodicInterval;
	}
	public void setLocationPeriodicInterval(int locationPeriodicInterval) {
		this.locationPeriodicInterval = locationPeriodicInterval;
	}
	public String getLocationStartInterval() {
		return locationStartInterval;
	}
	public void setLocationStartInterval(String locationStartInterval) {
		this.locationStartInterval = locationStartInterval;
	}
	public String getLocationEndInterval() {
		return locationEndInterval;
	}
	public void setLocationEndInterval(String locationEndInterval) {
		this.locationEndInterval = locationEndInterval;
	}
	public int getLocationBatteryStatus() {
		return locationBatteryStatus;
	}
	public void setLocationBatteryStatus(int locationBatteryStatus) {
		this.locationBatteryStatus = locationBatteryStatus;
	}
	public int getSyncUnsentDataInterval() {
		return syncUnsentDataInterval;
	}
	public void setSyncUnsentDataInterval(int syncUnsentDataInterval) {
		this.syncUnsentDataInterval = syncUnsentDataInterval;
	}
	@Override
	public String toString() {
		return "InitData [locationPeriodicInterval=" + locationPeriodicInterval
				+ ", locationStartInterval=" + locationStartInterval
				+ ", locationEndInterval=" + locationEndInterval
				+ ", locationBatteryStatus=" + locationBatteryStatus
				+ ", syncUnsentDataInterval=" + syncUnsentDataInterval + "]";
	}
	
	

}
