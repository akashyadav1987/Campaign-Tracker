package com.pulp.campaigntracker.beans;

import java.util.ArrayList;

public class SinglePromotorData {
	int total;
	ArrayList<UserProfile> personalDetails;

	public ArrayList<UserProfile> getPersonalDetails() {
		return personalDetails;
	}

	public void setPersonalDetails(ArrayList<UserProfile> personalDetails) {
		this.personalDetails = personalDetails;
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

	int start_count;
	int last_count;
}
