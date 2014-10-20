package com.pulp.campaigntracker.listeners;

import com.pulp.campaigntracker.utils.ConstantUtils.LocationSyncType;

public class MyLocation {

	String timeStamp;
	String cid;
	String lac;
	double latitude;
	double longitude;
	String auth_token;
	long id;
	LocationSyncType type;

	public MyLocation() {

	}

	public MyLocation(String auth_token, String cid, String lac,
			LocationSyncType type, double latitude, double longitude,
			String timeStamp) {
		super();
		this.auth_token = auth_token;
		this.cid = cid;
		this.lac = lac;
		this.latitude = latitude;
		this.timeStamp = timeStamp;
		this.id = id;
		this.type = type;

	}


	public LocationSyncType getType() {
		return type;
	}

	public void setType(LocationSyncType type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	public String getCellId() {
		return cid;
	}

	public void setCellId(String cid) {
		this.cid = cid;
	}

	public String getLacId() {
		return lac;
	}

	public void setLacId(String lac) {
		this.lac = lac;
	}

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

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}