package com.pulp.campaigntracker.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.pulp.campaigntracker.listeners.GetStoreDistanceRecieved;
import com.pulp.campaigntracker.utils.UtilityMethods;

public class JsonGetStoreDistance {

	private GetStoreDistanceRecieved listener;
	private Context mContext;
	private String url;

	public JsonGetStoreDistance(String url,
			GetStoreDistanceRecieved listener, Context mContext){

		this.url = url;
		this.listener = listener;
		this.mContext = mContext;

		DownloadTask downloadTask = new DownloadTask();

		if (UtilityMethods.isHoneycombOrHigher())
			downloadTask.executeForHoneyComb(url);
		else
			downloadTask.execute(url);

	}

	private class DownloadTask extends AsyncTask<String, Void, String>{

		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void executeForHoneyComb(String url)
		{
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
			
		}
		
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try{
				// Fetching the data from web service
				data = UtilityMethods.downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try{
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			}catch(Exception e){
				e.printStackTrace();
			}
			return routes;
		}


		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			listener.showDistance(result);
		}
	}


	public class DirectionsJSONParser {

		/** Receives a JSONObject and returns a list of lists containing latitude and longitude */
		public List<List<HashMap<String,String>>> parse(JSONObject jObject){

			List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
			JSONArray jRoutes = null;
			JSONArray jLegs = null;
			JSONArray jSteps = null;
			JSONObject jDistance = null;
			JSONObject jDuration = null;

			try {

				jRoutes = jObject.getJSONArray("routes");

				/** Traversing all routes */
				for(int i=0;i<jRoutes.length();i++){
					jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");

					List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

					/** Traversing all legs */
					for(int j=0;j<jLegs.length();j++){

						/** Getting distance from the json data */
						jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
						HashMap<String, String> hmDistance = new HashMap<String, String>();
						hmDistance.put("distance", jDistance.getString("text"));

						/** Getting duration from the json data */
						jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
						HashMap<String, String> hmDuration = new HashMap<String, String>();
						hmDuration.put("duration", jDuration.getString("text"));

						/** Adding distance object to the path */
						path.add(hmDistance);

						/** Adding duration object to the path */
						path.add(hmDuration);

						jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

						/** Traversing all steps */
						for(int k=0;k<jSteps.length();k++){
							String polyline = "";
							polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
							List<LatLng> list = decodePoly(polyline);

							/** Traversing all points */
							for(int l=0;l<list.size();l++){
								HashMap<String, String> hm = new HashMap<String, String>();
								hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
								hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
								path.add(hm);
							}
						}
					}
					routes.add(path);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e){
			}
			return routes;
		}

		/**
		 * Method to decode polyline points
		 * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
		 * */
		private List<LatLng> decodePoly(String encoded) {

			List<LatLng> poly = new ArrayList<LatLng>();
			int index = 0, len = encoded.length();
			int lat = 0, lng = 0;

			while (index < len) {
				int b, shift = 0, result = 0;
				do {
					b = encoded.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lat += dlat;

				shift = 0;
				result = 0;
				do {
					b = encoded.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lng += dlng;

				LatLng p = new LatLng((((double) lat / 1E5)),
						(((double) lng / 1E5)));
				poly.add(p);
			}
			return poly;
		}
	}
}
