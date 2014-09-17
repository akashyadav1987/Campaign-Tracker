package com.pulp.campaigntracker.beans;

public class CheckinDetails {

	double latitude;
	double longitude;
	String address;
	String time;
	int checkinStatus;
	int sentStatus;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getCheckinStatus() {
		return checkinStatus;
	}
	public void setCheckinStatus(int checkinStatus) {
		this.checkinStatus = checkinStatus;
	}
	public int getSentStatus() {
		return sentStatus;
	}
	public void setSentStatus(int sentStatus) {
		this.sentStatus = sentStatus;
	}
	
}
