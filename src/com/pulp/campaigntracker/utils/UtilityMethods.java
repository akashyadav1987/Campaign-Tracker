package com.pulp.campaigntracker.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.R.integer;
import com.pulp.campaigntracker.R.string;
import com.pulp.campaigntracker.beans.LoginData;
import com.pulp.campaigntracker.dao.UserLoginStatusDatabase;
import com.pulp.campaigntracker.listeners.PromoterActivityFinish;
import com.pulp.campaigntracker.ui.LoginActivity;
import com.pulp.campaigntracker.ui.PromotorMotherActivity;
import com.pulp.campaigntracker.ui.SplashScreen;
import com.pulp.campaigntracker.ui.SupervisorMotherActivity;

public class UtilityMethods {

	private static final String MY_PICS_IMAGES = "MyPics/Images";
	public final static String TAG = UtilityMethods.class.getSimpleName();
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	public static final String CONTENT_TYPE_KEY = "Content-Type";
	public static final String CONTENT_TYPE_JSON = "application/json";
	private static final String TAG_STATUS = "status";
	public static String SECURITY_SALT = "a%pULp0StRaTeGy$!";
	private static LoginActivity mLoginActivity;
	private static PromotorMotherActivity mPromotorMotherActivity;
	private static SupervisorMotherActivity mSupervisorMotherActivity;
	private static SplashScreen mSplashScreen;
	private static PromoterActivityFinish promoterFinishListner;

	public static SplashScreen getmSplashScreen() {
		return mSplashScreen;
	}

	public static void setmSplashScreen(SplashScreen mSplashScreen) {
		UtilityMethods.mSplashScreen = mSplashScreen;
	}

	public static void setLoginActivity(LoginActivity loginActivity) {

		mLoginActivity = loginActivity;

	}

	public static LoginActivity getLoginActivity() {

		return mLoginActivity;

	}

	public static void setSupervisorMotherActivity(
			SupervisorMotherActivity supervisorMotherActivity) {

		mSupervisorMotherActivity = supervisorMotherActivity;

	}

	public static SupervisorMotherActivity getSupervisorMotherActivity() {

		return mSupervisorMotherActivity;

	}

	public static void setPromotorMotherActivity(
			PromotorMotherActivity promotorMotherActivity) {

		mPromotorMotherActivity = promotorMotherActivity;

	}

	public static PromotorMotherActivity getPromotorMotherActivity() {

		return mPromotorMotherActivity;

	}

	/**
	 * Extract the JSonObject from the files in asset folder.
	 * 
	 * @param filename
	 * @param context
	 * @return {@link JSONObject}
	 * @throws IOException
	 */
	public static JSONObject AssetJSONFile(String filename, Context context)
			throws IOException {
		JSONObject jsonObj = null;
		String jsonString = null;
		AssetManager manager = context.getAssets();
		InputStream file = manager.open(filename);
		byte[] formArray = new byte[file.available()];
		file.read(formArray);
		file.close();
		jsonString = new String(formArray, "UTF-8");

		try {
			Object jTObject = new JSONTokener(jsonString).nextValue();
			if (jTObject instanceof JSONObject) {
				jsonObj = new JSONObject(jsonString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			TLog.v(TAG, "Exception" + e.toString());
		}
		return jsonObj;
	}

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
			LoginData mLoginData = LoginData.getInstance();
			//
			// url +="?auth_token="+mLoginData.getAuthToken();
			//
			// url +="&user_id=test"+mLoginData.getId();

			url = url.toLowerCase();

			HttpUriRequest request = new HttpGet(url);
			request.addHeader("Accept-Encoding", "gzip");
//			request.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff()));
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			InputStream instream = response.getEntity().getContent();
			org.apache.http.Header contentEncoding = response
					.getFirstHeader("Content-Encoding");

			if (contentEncoding != null
					&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(instream)));
				jsonString = readAll(rd);
				rd = null;
			} else {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						instream));
				jsonString = readAll(rd);
				rd = null;
			}
			instream.close();
			instream = null;
			contentEncoding = null;
			request = null;
			response = null;
			httpClient = null;
			// } catch (Exception e) {
			// TLog.v(TAG, e.toString());
			// }
			// // try parse the string to a JSON object
			//
			// try {
			Object jTObject = new JSONTokener(jsonString).nextValue();
			if (jTObject instanceof JSONObject) {
				jsonObj = new JSONObject(jsonString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			TLog.v(TAG, "Exception" + e.toString());
//			promoterFinishListner.promoterFinishActivity();
			//UtilityMethods.ShowAlertDialog(mContext);
			
		}

		return jsonObj;

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

		InputStream is = null;
		JSONObject jObj = null;
		String json = null;

		// String loginParamsString = (URLEncodedUtils.format(params, "utf-8"))
		// .toLowerCase();
		// url += loginParamsString;

		// Making HTTP request

		// defaultHttpClient

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			// HttpGet httpGet = new HttpGet(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			// reading the json Object received in Http Response

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			jObj = new JSONObject(json);
		} catch (Exception ex) {
			UtilityMethods.ShowAlertDialog(getLoginActivity());
		}
		return jObj;

	}

	public static JSONObject submitForm(String url,
			List<NameValuePair> formSubmitValues) {

		InputStream is = null;
		JSONObject jObj = null;
		String json = null;

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(formSubmitValues));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			// // reading the json Object received in Http Response
			// try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			jObj = new JSONObject(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jObj;
	}

	/**
	 * This is to read the file line by line to avoid outofmemory. And make use
	 * of StringBuilder for optimization.
	 * 
	 * @param rd
	 * @return {@link String}
	 * @throws IOException
	 */
	public static String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		String inputLine;

		while ((inputLine = rd.readLine()) != null) {
			sb.append(inputLine);
		}

		return sb.toString();
	}

	/**
	 * This takes the context and return the device id as string
	 * 
	 * @param context
	 * @return {@link string}
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String trans = telephonyManager.getDeviceId();

		if (trans == null) {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wInfo = wifiManager.getConnectionInfo();
			trans = wInfo.getMacAddress();
		}

		TLog.i(TAG, "device id = " + trans);
		return trans;
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * @param context
	 *            The application's environment.
	 * @param action
	 *            The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
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
	 * 
	 * @param mContext
	 * @return {@link File} File created for the device on the directory.
	 */
	public static File getAlbumDir(Context mContext) {

		File storageDir = null;
		AlbumStorageDirFactory mAlbumStorageDirFactory = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName(mContext));

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(mContext.getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	/**
	 * This is to be used to get the album name as a string
	 * 
	 * @param mContext
	 * @return {@link String}
	 */
	public static String getAlbumName(Context mContext) {
		return mContext.getString(R.string.album_name);
	}

	/**
	 * Creates a File for the Image.
	 * 
	 * @param mContext
	 * @return {@link File}
	 * @throws IOException
	 */
	public static File createImageFile(Context mContext) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir(mContext);
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	/**
	 * Setup a File for the image.
	 * 
	 * @param mContext
	 * @return {@link File}
	 * @throws IOException
	 */
	public static File setUpPhotoFile(Context mContext) throws IOException {

		File f = createImageFile(mContext);
		return f;
	}

	/**
	 * This method first compress the Image saves it on to a file. Then Upload
	 * the image on the provided URL.
	 * 
	 * @param imageUri
	 * @param serverUploadURL
	 * @param mContext
	 * @return {@link String}
	 */
	
	public static String compressAndUploadImage(String imageUri,
			final String serverUploadURL, final Context mContext) {

		String filePath = imageUri;
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		options.inSampleSize = calculateInSampleSize(options, actualWidth,
				actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
				middleY - bmp.getHeight() / 2, new Paint(
						Paint.FILTER_BITMAP_FLAG));

		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream out = null;
		final String filename = getFilename();
		try {
			out = new FileOutputStream(filename);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			new Thread(new Runnable() {
				public void run() {

					uploadFile(filename, serverUploadURL, mContext);

				}
			}).start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return filename;

	}

	public static String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), MY_PICS_IMAGES);
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"
				+ System.currentTimeMillis() + ".jpg");
		return uriSting;

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;

		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}

	public static int uploadFile(String sourceFileUri, String upLoadServerUri,
			final Context mContext) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			Log.e("uploadFile", "Source File not exist :" + sourceFileUri);

			return 0;

		} else {
			saveBitmapToDB(mContext, sourceFile);

			int serverResponseCode = 0;
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				TLog.v(TAG, "bytesRead" + bytesRead);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				TLog.i(TAG, "HTTP Response is : " + serverResponseMessage
						+ ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					((Activity) mContext).runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(mContext, "File Upload Complete.",
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					// saveBitmapToDB(mContext,sourceFile);
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (Exception e) {
				// saveBitmapToDB(mContext,sourceFile);

				TLog.d(TAG, "Exception : " + e.getMessage());
			}
			return serverResponseCode;

		} // End else block
	}

	/**
	 * This saves the image to the database if the upload fails.
	 * 
	 * @param mContext
	 * @param sourceFile
	 */
	private static void saveBitmapToDB(Context mContext, File sourceFile) {

		byte[] array = null;
		try {

			array = FileUtils.readFileToByteArray(sourceFile);
			UserLoginStatusDatabase userLoginStatusDatabase = new UserLoginStatusDatabase(
					mContext);
			userLoginStatusDatabase.open();
			TLog.v(TAG,
					"database save +"
							+ userLoginStatusDatabase.insertInfo(
									getCurrentTimeStampInHours(), array, 1));
			userLoginStatusDatabase.close();

			array = null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the current time in the format : HHddMMyyyy
	 * 
	 * @return {@link String}
	 */
	public static String getCurrentTimeStampInHours() {
		SimpleDateFormat s = new SimpleDateFormat("HHddMMyyyy");
		String format = s.format(new Date());
		return format;
	}

	/**
	 * Gets the current time in the format : ddMMyyyy
	 * 
	 * @return {@link String}
	 */
	public static String getCurrentTimeStampInDays() {
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy");
		String format = s.format(new Date());
		return format;
	}

	/**
	 * Construct the header for the POST call.
	 * 
	 * @return {@link String}
	 */
	public static String contsructRequestHeader() {
		JSONObject outer = new JSONObject();

		try {
			outer.put(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
			TLog.i(TAG, "user JSON" + outer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outer.toString();
	}

	/**
	 * This is to calculate the hash on the params for POST calls It first sorts
	 * the params(with the comparator for the post value) and then append a HASH
	 * SALT defined as a static value to be shared between the client & server.
	 * Then apply a hash Salt.
	 * 
	 * @param nameValuePairs
	 * @return {@link String}
	 */
	public static String calculateSyncHash(List<NameValuePair> nameValuePairs) {
		StringBuilder sb = new StringBuilder();
		Collections.sort(nameValuePairs, new KeyComparator());

		TLog.i(TAG, "NVP " + nameValuePairs);

		for (NameValuePair nvp : nameValuePairs) {
			sb.append(nvp.getName());
			sb.append(nvp.getValue());
		}

		sb.append(SECURITY_SALT);

		return applyHashSalt(sb.toString());
	}

	public static class KeyComparator implements Comparator<NameValuePair> {

		@Override
		public int compare(NameValuePair lhs, NameValuePair rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}

	}

	/**
	 * This is an algo to apply hash salt. It takes the query from the
	 * calculateHashSync and provides an ecncryted string for the same. Need to
	 * have a same algo for both Client/Server.
	 * 
	 * @param query
	 * @return {@link String}
	 */
	public static String applyHashSalt(String query) {
		TLog.i(TAG, "query " + query);
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		md.update(query.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuilder hash = new StringBuilder();
		for (int i = 0; i < byteData.length; i++) {
			hash.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return hash.toString();
	}

	/**
	 * This is to post the JSONObject to server to the url provided. It reads
	 * the response from the server and fetchs the status of the api call.
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return {@link integer} Status response code from server.
	 */
	public static int postJsonToServer(String url,
			List<NameValuePair> nameValuePairs) {
		int status = 0;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			String responseString = (EntityUtils.toString(resEntity))
					.toString();
			TLog.i(TAG, "postJsonToServer Response : " + responseString);
			if (responseString != null) {
				Object jTObject = new JSONTokener(responseString).nextValue();

				if (jTObject instanceof JSONObject) {
					JSONObject jFullObject = new JSONObject(responseString);

					if (!jFullObject.isNull(TAG_STATUS)) {
						status = jFullObject.getInt(TAG_STATUS);
					}
				}
			}
			response = null;
			resEntity = null;
			httpclient = null;
			httppost = null;

		} catch (Exception e) {
			TLog.v(TAG, "Exception postJsonToServer : " + e.toString());
		}
		return status;

	}

	public static String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		// System.out.println("URL : "+url);
		return url;
	}

	public static String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/**
	 * Gets the date from the string in format : HHddMMyyyy
	 * 
	 * @param dateString
	 * @return {@link Date}
	 */
	public static Date getDateFromString(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("HHddMMyyyy");
		try {
			Date date = format.parse(dateString);
			System.out.println(date);
		} catch (Exception e) {
			TLog.v(TAG, "getDateFromString : " + e.toString());
		}
		return null;

	}

	/**
	 * Return the init shared preference for the app.
	 * 
	 * @param context
	 * @return {@link SharedPreferences}
	 */
	public static SharedPreferences getInitPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(ConstantUtils.INIT,
				Context.MODE_PRIVATE);
	}

	public final static boolean isValidEmail(String target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	public static void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(ConstantUtils.PROPERTY_GCM_REG_ID, regId);
		editor.putInt(ConstantUtils.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	public static String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(
				ConstantUtils.PROPERTY_GCM_REG_ID, "");
		if (registrationId != null && registrationId.equals("")) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(
				ConstantUtils.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	public static SharedPreferences getLoginPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(ConstantUtils.LOGIN,
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	public static void setLoginPreferences(Context context, String key,
			String value) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		final SharedPreferences prefs = getLoginPreferences(context);

		Log.i(TAG, "Saving login info on app version ");
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();

	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private static SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(ConstantUtils.GCM,
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private void setGcmPreferences(Context context, String key, String value) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.

		SharedPreferences appSharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEditor = appSharedPrefs.edit();
		prefsEditor.putString(key, value);
		prefsEditor.commit();

	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your application. Not needed for this
	 * demo since the device sends upstream messages to a server that echoes
	 * back the message using the 'from' address in the message.
	 * 
	 * @throws JSONException
	 */
	private void sendRegistrationIdToBackend(Context context, String regId)
			throws JSONException {
		TLog.v(TAG, "Registeration ID = " + regId);

		storeRegistrationId(context, regId);

	}

	private JSONObject buildRegisterationJSON(String regId)
			throws JSONException {
		JSONObject jCompleteObject = new JSONObject();
		JSONObject jInnerObject = new JSONObject();
		jInnerObject.put("name", "udit");
		jInnerObject.put("email", "uditgupta.mail@gmail.com");
		jInnerObject.put("gcm_token", regId);
		return jCompleteObject.put("user", jInnerObject);

	}

	public static void ShowAlertDialog(Context context) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		// set title
		alertDialogBuilder.setTitle("Something Went Wrong");
		// set dialog message
		alertDialogBuilder
				.setMessage("Check Your Network Connection")
				.setCancelable(false)
				.setNeutralButton("Exit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

}
