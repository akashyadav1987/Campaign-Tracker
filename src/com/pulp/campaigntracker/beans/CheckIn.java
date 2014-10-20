package com.pulp.campaigntracker.beans;

public class CheckIn {
	
private String auth_token;
private String role;
private String encodedImage;
private String url;
private String id;
private String store_id;
private String campaign_id;
private String time;


public CheckIn(String auth_token, String role, String encodedImage, String url,
		String id, String store_id, String campaign_id, String time) {
	super();
	this.auth_token = auth_token;
	this.role = role;
	this.encodedImage = encodedImage;
	this.url = url;
	this.id = id;
	this.store_id = store_id;
	this.campaign_id = campaign_id;
	this.time = time;
}
public String getAuth_token() {
	return auth_token;
}
public void setAuth_token(String auth_token) {
	this.auth_token = auth_token;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getEncodedImage() {
	return encodedImage;
}
public void setEncodedImage(String encodedImage) {
	this.encodedImage = encodedImage;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getStore_id() {
	return store_id;
}
public void setStore_id(String store_id) {
	this.store_id = store_id;
}
public String getCampaign_id() {
	return campaign_id;
}
public void setCampaign_id(String campaign_id) {
	this.campaign_id = campaign_id;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
@Override
public String toString() {
	return "CheckIn [auth_token=" + auth_token + ", role=" + role
			 + ", url=" + url + ", id=" + id
			+ ", store_id=" + store_id + ", campaign_id=" + campaign_id
			+ ", time=" + time + "]";
}
}
