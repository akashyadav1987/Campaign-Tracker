/**
 * @author sunil.kumar
 */

package com.pulp.campaigntracker.beans;

/**
 * Error response object from Sign IN/Sign Up/
 */

public class LoginErrorData {

	private String type;
	private String value;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LoginErrorData [type=" + type + ", value=" + value + "]";
	}
}
