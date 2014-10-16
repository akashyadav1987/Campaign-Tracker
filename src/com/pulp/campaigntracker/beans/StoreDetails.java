package com.pulp.campaigntracker.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class StoreDetails implements Parcelable{

	String id;
	String name;
	String address;
	String city;
	String pincode;
	String state;
	String ownerName;
	String contactNo;
	String storeCategory;
	String storeCode;
	String region;
	int agent;
	double latitude;
	double longitude;
	private Bitmap storeImage;

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public Bitmap getStoreImage() {
		return storeImage;
	}
	public void setStoreImage(Bitmap image) {
		this.storeImage = image;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getAgent() {
		return agent;
	}
	public void setAgent(int agent) {
		this.agent = agent;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getStoreCategory() {
		return storeCategory;
	}
	public void setStoreCategory(String storeCategory) {
		this.storeCategory = storeCategory;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(city);
		dest.writeString(state);
		dest.writeString(ownerName);
		dest.writeString(contactNo);
		dest.writeString(storeCategory);
		dest.writeString(storeCode);
		dest.writeString(region);
		dest.writeInt(agent);

	}
	public StoreDetails()
	{
		
	}
	public StoreDetails(Parcel in)
	{
		super();
		id = in.readString();
		name = in.readString();
		address = in.readString();
		city = in.readString();
		state = in.readString();
		ownerName = in.readString();
		contactNo = in.readString();
		storeCategory = in.readString();
		storeCode = in.readString();
		region = in.readString();
		agent = in.readInt();

	}
	
	public static final Parcelable.Creator<StoreDetails> CREATER = new Parcelable.Creator<StoreDetails>() {

		@Override
		public StoreDetails createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new StoreDetails(source);
		}

		@Override
		public StoreDetails[] newArray(int size) {
			// TODO Auto-generated method stub
			return new StoreDetails[size];
		}
	};
	

	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

}
