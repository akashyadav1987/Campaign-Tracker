package com.pulp.campaigntracker.beans;

import org.json.JSONObject;

public class FormData {
private long id;
private JSONObject formDataObject;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public JSONObject getFormDataObject() {
	return formDataObject;
}
public void setFormDataObject(JSONObject formDataObject) {
	this.formDataObject = formDataObject;
}
@Override
public String toString() {
	return "FormData [id=" + id + ", formDataObject=" + formDataObject + "]";
}

}
