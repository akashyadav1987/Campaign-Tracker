package com.pulp.campaigntracker.beans;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class UserFormDetails implements Parcelable{
	
	private String fieldName;
	private String fieldType;
	private String fieldLength;
	private String fieldValue;
	private Date feildDate;
	public Date getFeildDate() {
		return feildDate;
	}
	public void setFeildDate(Date feildDate) {
		this.feildDate = feildDate;
	}

	private byte[] image;

	
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fieldName);
		dest.writeString(fieldType);
		dest.writeString(fieldLength);
		dest.writeString(fieldValue);
		
		
		
	}
	public UserFormDetails() {

	}

	public UserFormDetails(Parcel in) {
		super();
		fieldName = in.readString();
		fieldType = in.readString();
		fieldLength = in.readString();
		
	

	}

	public static final Parcelable.Creator<UserFormDetails> CREATOR = new Creator<UserFormDetails>() {

		@Override
		public UserFormDetails[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UserFormDetails[size];
		}

		@Override
		public UserFormDetails createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new UserFormDetails(source);
		}
	};

}
