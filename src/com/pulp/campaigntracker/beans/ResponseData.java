package com.pulp.campaigntracker.beans;

public class ResponseData {
boolean success;
public boolean getSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public boolean getError() {
	return error;
}
public void setError(boolean error) {
	this.error = error;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
boolean error;
String errorMessage;
}
