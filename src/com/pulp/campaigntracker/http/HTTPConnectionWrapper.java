package com.pulp.campaigntracker.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;

import com.pulp.campaigntracker.utils.TLog;




public class HTTPConnectionWrapper {

	
	public static final String PRODUCTION_HOST = "";

	public static final String STAGING_HOST = "";

	public static final int PRODUCTION_PORT = 80;

	public static final int PRODUCTION_PORT_SSL = 443;

	public static final int STAGING_PORT = 8080;

	public static final int STAGING_PORT_SSL = 443;
	
	public static boolean ssl = false;

	public static final String NETWORK_PREFS_NAME = "NetworkPrefs";

	public static HttpClient mClient = null;

	public static String mToken = null;

	public static String mUid = null;

	private static String appVersion = null;
	
	public static String host = PRODUCTION_HOST;

	public static int port = PRODUCTION_PORT;

	public static void setToken(String token)
	{
		mToken = token;
	}

	public static void setUID(String uid)
	{
		mUid = uid;
	}

	public static void setAppVersion(String version)
	{
		appVersion = version;
	}

	
	/**
	 * This is to check if the internet connection is available.
	 * 
	 * @param context
	 * @return {@link Boolean}
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkAvailable = false;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// For 3G check
		boolean is3g = false;
		if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
			is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.isConnectedOrConnecting();
		// For WiFi Check
		boolean isWifi = false;
		if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null)
			isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.isConnectedOrConnecting();

		if (is3g || isWifi) {
			isNetworkAvailable = true;
		}

		return isNetworkAvailable;
	}
	/**
	 * Retry http connection
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse runHttpRequest(HttpUriRequest uriRequest) 
		    throws IOException
		{
		    IOException last = null;
		    HttpResponse response =null;
		    HttpUriRequest request=uriRequest;
		    
		    HttpClient mHttpClient = getClient();
		    for (int attempt = 0; attempt < 3; attempt++) {
		        try {
		        	
		            TLog.d("HTTP", "Performing HTTP Request " + request.getRequestLine());
					TLog.d("HTTP", "to host" + request);
				
		            response = mHttpClient.execute(request);
		        
					
					TLog.d("HTTP", "finished request");
					if (response.getStatusLine().getStatusCode() == 200)
					{
						TLog.w("HTTP", "Request success: " + response.getStatusLine());
						return response;
						
					}
					
		            
		        } catch (IOException e) {
		           
		           
		            last = e;
		        }
		        try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }

		    request.abort();
		    throw last;
		}



	/**
	 * Get HTTP request/response and return Jsonresponse in string format
	 * 
	 * @param String URL
	 * @return {@String}
	 */
	public static String GetHttpResponse(String url) {
		// TODO Auto-generated method stub
		
		String jsonResponse=null;
		HttpUriRequest request = new HttpGet(url);
		request.addHeader("Accept-Encoding", "gzip");
	
		HttpClient httpClient = getClient();
		
	
		HttpResponse response = null;
		try {
			
				
				response = httpClient.execute(request);
				String as =response.toString();
				TLog.d("HTTP", "finished request");
				if (response.getStatusLine().getStatusCode() != 200)
				{
					TLog.w("HTTP", "Request Failed: " + response.getStatusLine());
					return null;
				}

				HttpEntity entity = response.getEntity();
				InputStream is= entity.getContent();
				return getResponse(is);
			}
			catch (ClientProtocolException e)
			{
				TLog.e("HTTP", "Invalid Response",e);
				e.printStackTrace();
			}
			catch (IOException e)
			{
				TLog.e("HTTP", "Unable to perform request",e);
				e.printStackTrace();
				try {
					response = runHttpRequest(request);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					TLog.e("HTTP", "Unable to perform request even after retry",e1);
					e1.printStackTrace();
					return null;
				} 
			}


		
		return jsonResponse;
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	
	public static String getResponse(InputStream is) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder builder = new StringBuilder();
		CharBuffer target = CharBuffer.allocate(10000);
		int read = reader.read(target);
		while (read >= 0)
		{
			builder.append(target.array(), 0, read);
			target.clear();
			read = reader.read(target);
		}
		TLog.d("HTTP", "request finished");
		
		return  builder.toString();
		
	}
	
	/**
	 * 
	 * @return
	 */
		
	public static synchronized HttpClient getClient()
	{
		if (mClient != null)
		{
			return mClient;
		}
		TLog.d("HTTPConnectionWrapper", "Initialising the HTTP CLIENT");

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		/*
		 * set the connection timeout to 6 seconds, and the waiting for data timeout to 30 seconds
		 */
		HttpConnectionParams.setConnectionTimeout(params, 30*1000);
		HttpConnectionParams.setSoTimeout(params, 30 * 1000);

		SchemeRegistry schemeRegistry = new SchemeRegistry();

		if (ssl)
		{
				//To be done
		}
		else
		{
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), port));
		}

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		mClient = new DefaultHttpClient(cm, params);
		mClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android-" + appVersion);
		return mClient;
	}

	public static void addUserAgent(URLConnection urlConnection)
	{
		urlConnection.addRequestProperty("User-Agent", "android-" + appVersion);
	}

	public static void addUserAgent(HttpRequestBase request)
	{
		request.addHeader("User-Agent", "android-" + appVersion);
	}

	/**
	 * Takes url and NameValuePair as params and return the String response for the
	 * same.
	 * 
	 * @param url
	 * @param params
	 * @return {@link String}
	 */
	public static String postHTTPRequest(String url,
			List<NameValuePair> params) {


		String jsonResponse=null;
		HttpResponse httpResponse=null;	
		HttpPost httpPostRequest = new HttpPost(url);
		try {
			httpPostRequest.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//httpPostRequest.addHeader("Accept-Encoding", "gzip");
		
		HttpClient httpClient = getClient();
	    for (int attempt = 0; attempt < 3; attempt++)
	    {
				try {
					
					httpResponse = httpClient.execute(httpPostRequest);
					HttpEntity httpEntity = httpResponse.getEntity();
					return getResponse(httpEntity.getContent());
				
				}catch (ClientProtocolException e)
					{
						TLog.e("HTTP", "Invalid Response",e);
						e.printStackTrace();
					}
	public static String postHTTPRequest(String url,
			JSONObject params) {


		String jsonResponse=null;
		HttpResponse httpResponse=null;	
		HttpPost httpPostRequest = new HttpPost(url);
		try {
			 StringEntity se = new StringEntity( params.toString());  
			  se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			  httpPostRequest.setHeader("Accept", "application/json");
			  httpPostRequest.setHeader("Content-type", "application/json");
			  httpPostRequest.setEntity(se);
			//httpPostRequest.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		httpPostRequest.addHeader("Accept-Encoding", "gzip");
		
		HttpClient httpClient = getClient();
	    for (int attempt = 0; attempt < 3; attempt++)
	    {
				try {
					
					httpResponse = httpClient.execute(httpPostRequest);
					HttpEntity httpEntity = httpResponse.getEntity();
					return getResponse(httpEntity.getContent());
				
				}catch (ClientProtocolException e)
					{
						TLog.e("HTTP", "Invalid Response",e);
						e.printStackTrace();
					}
					catch (IOException e)
					{
						TLog.e("HTTP", "Unable to perform request",e);
						e.printStackTrace();
	
					}
	    	}

	    httpPostRequest.abort();
		return jsonResponse;
		
	}	
	
}
