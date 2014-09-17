package com.pulp.campaigntracker.beans;

import java.util.ArrayList;

public class AllPromoterData {
	public static ArrayList<UserProfile> previousSet;
	public  ArrayList<UserProfile> getPreviousSet() {
		return previousSet;
	}
	public  void setPreviousSet(ArrayList<UserProfile> previousSet) {
		AllPromoterData.previousSet = previousSet;
	}
	public  ArrayList<UserProfile> getCurrentSet() {
		return currentSet;
	}
	public void setCurrentSet(ArrayList<UserProfile> currentSet) {
		AllPromoterData.currentSet = currentSet;
	}
	public  ArrayList<UserProfile> getNextSet() {
		return nextSet;
	}
	public  void setNextSet(ArrayList<UserProfile> nextSet) {
		AllPromoterData.nextSet = nextSet;
	}
	public static  ArrayList<UserProfile> currentSet;
	public static  ArrayList<UserProfile> nextSet;
}
