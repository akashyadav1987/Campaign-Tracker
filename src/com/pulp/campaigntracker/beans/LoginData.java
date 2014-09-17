package com.pulp.campaigntracker.beans;

import com.pulp.campaigntracker.ui.PromotorMotherActivity;

public class LoginData {
//Single instance for Login
public static LoginData instance;

public static LoginData getInstance() {
        if (instance == null)
              instance = new LoginData();
        return instance;
}

	
	String username;
	String password;
	String email;
	String createAt;
	String id;
	String phoneNo;
	String authToken;
	String role;
	PromotorMotherActivity mPromoActivity=null;

	
	public void setMotherActivity(PromotorMotherActivity activity)
	{
		 mPromoActivity=activity;
		
	}
	
	public PromotorMotherActivity getMotherActivity()
	{
		
		return mPromoActivity; 
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	
}
