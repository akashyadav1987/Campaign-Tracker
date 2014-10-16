package com.pulp.campaigntracker.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {

	private String uid;
	private String name;
	private String email;
	private String DOB;
	private String contactNumber;
	private String landlinePhone;
	private String gender;
	private String address;
	private String role;
	private String storeId;
	private String campaginId;
	private String CurrentCampagin;
	private String CurrentStore;
	private String status;

	public String getCampaign_start_date() {
		return campaign_start_date;
	}

	public void setCampaign_start_date(String campaign_start_date) {
		this.campaign_start_date = campaign_start_date;
	}

	public String getStore_start_date() {
		return store_start_date;
	}

	public void setStore_start_date(String store_start_date) {
		this.store_start_date = store_start_date;
	}

	public String getStore_end_date() {
		return store_end_date;
	}

	public void setStore_end_date(String store_end_date) {
		this.store_end_date = store_end_date;
	}

	public String getCampaign_end_date() {
		return campaign_end_date;
	}

	public void setCampaign_end_date(String campaign_end_date) {
		this.campaign_end_date = campaign_end_date;
	}

	private String campaign_start_date;
	private String store_start_date;
	private String store_end_date;
	private String campaign_end_date;
	int total;
	int start_count;
	int last_count;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentCampagin() {
		return CurrentCampagin;
	}

	public void setCurrentCampagin(String currentCampagin) {
		CurrentCampagin = currentCampagin;
	}

	public String getCurrentStore() {
		return CurrentStore;
	}

	public void setCurrentStore(String currentStore) {
		CurrentStore = currentStore;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStart_count() {
		return start_count;
	}

	public void setStart_count(int start_count) {
		this.start_count = start_count;
	}

	public int getLast_count() {
		return last_count;
	}

	public void setLast_count(int last_count) {
		this.last_count = last_count;
	}

	public String getCampaginId() {
		return campaginId;
	}

	public void setCampaginId(String campaginId) {
		this.campaginId = campaginId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getLandlinePhone() {
		return landlinePhone;
	}

	public void setLandlinePhone(String landlinePhone) {
		this.landlinePhone = landlinePhone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(uid);
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(DOB);
		dest.writeString(contactNumber);
		dest.writeString(landlinePhone);
		dest.writeString(gender);
		dest.writeString(address);
		dest.writeString(role);
		dest.writeString(CurrentCampagin);
		dest.writeString(CurrentStore);
		dest.writeString(status);

		dest.writeString(campaign_start_date);
		dest.writeString(store_start_date);
		dest.writeString(store_end_date);
		dest.writeString(campaign_end_date);

	}

	public UserProfile() {

	}

	public UserProfile(Parcel in) {
		super();
		uid = in.readString();
		name = in.readString();
		email = in.readString();
		DOB = in.readString();
		contactNumber = in.readString();
		landlinePhone = in.readString();
		gender = in.readString();
		address = in.readString();
		role = in.readString();
		CurrentCampagin = in.readString();
		CurrentStore = in.readString();
		status = in.readString();

		campaign_start_date = in.readString();
		store_start_date = in.readString();
		store_end_date = in.readString();
		campaign_end_date = in.readString();
	}

	public static final Parcelable.Creator<UserProfile> CREATOR = new Creator<UserProfile>() {

		@Override
		public UserProfile[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UserProfile[size];
		}

		@Override
		public UserProfile createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new UserProfile(source);
		}
	};


}
