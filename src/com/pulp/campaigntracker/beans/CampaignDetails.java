package com.pulp.campaigntracker.beans;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CampaignDetails implements Parcelable {

	private String id;
	private String code;
	private String name;
	private int store_id;
	private String alias_name;
	private String address;
	private String company;
	private String contactNumber;
	private String registered;
	private String imageUrl;
	private String promoterCount;

	public String getPromoterCount() {
		return promoterCount;
	}

	public void setPromoterCount(String promoterCount) {
		this.promoterCount = promoterCount;
	}

	private List<StoreDetails> storeList;
	private UserProfile immediateManager;
	private String campaignDisplayName;
	private List<UserFormDetails> userFormDetailsList;
	private List<UserProfile> userList;

	public List<UserFormDetails> getUserFormDetailsList() {
		return userFormDetailsList;
	}

	public void setUserFormDetailsList(List<UserFormDetails> userFormDetailsList) {
		this.userFormDetailsList = userFormDetailsList;
	}

	public String getCampaignDisplayName() {
		return campaignDisplayName;
	}

	public void setCampaignDisplayName(String campaignDisplayName) {
		this.campaignDisplayName = campaignDisplayName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public UserProfile getImmediateManager() {
		return immediateManager;
	}

	public void setImmediateManager(UserProfile immediateManager) {
		this.immediateManager = immediateManager;
	}

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<UserProfile> getUserList() {
		return userList;
	}

	public void setUserList(List<UserProfile> userList) {
		this.userList = userList;
	}

	public List<StoreDetails> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<StoreDetails> storeList) {
		this.storeList = storeList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getAlias_name() {
		return alias_name;
	}

	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public CampaignDetails(Parcel in) {
		super();
		id = in.readString();
		code = in.readString();
		name = in.readString();
		store_id = in.readInt();
		alias_name = in.readString();
		address = in.readString();
		company = in.readString();
		contactNumber = in.readString();
		registered = in.readString();
		imageUrl = in.readString();
		promoterCount = in.readString();
		userFormDetailsList = new ArrayList<UserFormDetails>();
		in.readList(userFormDetailsList, UserFormDetails.class.getClassLoader());

		userList = new ArrayList<UserProfile>();
		in.readList(userList, UserProfile.class.getClassLoader());
		
		storeList = new ArrayList<StoreDetails>();
		in.readList(userList, StoreDetails.class.getClassLoader());
		
		this.immediateManager = in.readParcelable(UserProfile.class
				.getClassLoader());

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(code);
		dest.writeString(name);
		dest.writeInt(store_id);
		dest.writeString(alias_name);
		dest.writeString(address);
		dest.writeString(company);
		dest.writeString(promoterCount);
		dest.writeString(contactNumber);
		dest.writeString(registered);
		dest.writeString(imageUrl);
		dest.writeList(this.userList);
		dest.writeList(this.storeList);
		dest.writeList(this.userFormDetailsList);
		dest.writeParcelable(this.immediateManager, flags);

	}

	public CampaignDetails() {

	}

	public static final Parcelable.Creator<CampaignDetails> CREATOR = new Parcelable.Creator<CampaignDetails>() {
		public CampaignDetails createFromParcel(Parcel in) {
			return new CampaignDetails(in);
		}

		public CampaignDetails[] newArray(int size) {
			return new CampaignDetails[size];
		}
	};

}
