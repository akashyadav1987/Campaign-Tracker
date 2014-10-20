package com.pulp.campaigntracker.controllers;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.pulp.campaigntracker.http.HTTPConnectionWrapper;
import com.pulp.campaigntracker.utils.TLog;

public class JsonResponseAdapter {

	private static final String TAG = null;

	/**
	 * Takes a url as an input makes and HTTP call and returns the JSONOnject
	 * for the same
	 * 
	 * @param url
	 * @return {@link JSONObject}
	 */
	public static JSONObject campaignJsonResponse(String url, Context mContext) {
		String jsonString = "";
		JSONObject jsonObj = null;

		try {
			jsonString = HTTPConnectionWrapper.GetHttpResponse(url
					.toLowerCase());
			

			Object jTObject = new JSONTokener(jsonString).nextValue();
			if (jTObject instanceof JSONObject) {
				jsonObj = new JSONObject(jsonString);
			}

			return jsonObj;
		} catch (JSONException e) {
			TLog.e("HTTP", "Invalid JSON Response", e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("json", e.toString());
			TLog.v(TAG, "Exception" + e.toString());
		}

		return jsonObj;
	}

	public static JSONObject postJSONToUrl(String url,
			List<NameValuePair> params) {

		JSONObject jObj = null;
		String jsonString = null;

		// Making HTTP post request

		try {
			System.out.println(params.toString());
			jsonString = HTTPConnectionWrapper.postHTTPRequest(
					url.toLowerCase(), params);
			System.out.println("jsonString:::::::::::::" + jsonString);
			jObj = new JSONObject(jsonString);

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return jObj;

	}
	public static JSONObject postJSONToUrl(String url,
			JSONObject params) {

		JSONObject jObj = null;
		String jsonString = null;

		// Making HTTP post request

		try {
			System.out.println(params.toString());
			jsonString = HTTPConnectionWrapper.postHTTPRequest(
					url.toLowerCase(), params);
			System.out.println("jsonString:::::::::::::" + jsonString);
			jObj = new JSONObject(jsonString);

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return jObj;

	}

	/**
	 * Takes url and NameValuePair as params and return the JSONObject for the
	 * same.
	 * 
	 * @param url
	 * @param params
	 * @return {@link JSONObject}
	 */
	public static JSONObject getJSONFromUrl(String url,
			List<NameValuePair> params) {

		JSONObject jObj = null;
		String jsonString = null;

		// Making HTTP post request
		try {

			jsonString = HTTPConnectionWrapper.postHTTPRequest(
					url.toLowerCase(), params);

			
			jObj = new JSONObject(jsonString);
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return jObj;

	}

	public static JSONObject submitForm(String url,
			List<NameValuePair> formSubmitValues) {

		JSONObject jObj = null;
		String jsonString = null;

		// Making HTTP post request
		try {

			jsonString = HTTPConnectionWrapper.postHTTPRequest(
					url.toLowerCase(), formSubmitValues);

			jObj = new JSONObject(jsonString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jObj;
	}

}
