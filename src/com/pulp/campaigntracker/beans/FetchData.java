package com.pulp.campaigntracker.beans;

public class FetchData {

	public static FetchData instance;
	
	int total;

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
