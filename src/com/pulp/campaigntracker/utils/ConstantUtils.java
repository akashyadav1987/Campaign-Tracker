package com.pulp.campaigntracker.utils;

public class ConstantUtils {

	// Production and staging server for testing and release.
	public static String SERVER = "http://182.74.47.179/";
	public static String PROD_SERVER = "http://182.74.47.179/";
	public static String STAGING_SERVER = "http://182.74.47.179/";

	// URL for the api's
	public static String LOGIN_URL = SERVER + "LFRTrack/api/Login/";

	// public static String LOGIN_URL =
	// "http://www.promotadka.in/task_manager/api1/index.php/login";

	// public static String LOGIN_URL =
	// "http://www.drikpanchang.com/dp-api/xml/panchangam-data.xml";
	public static String SUBMIT_FORM_URL = SERVER + "LFRTrack/api/SubmitForm/";

	// public static String LOGIN_URL =
	// "http://182.74.47.179/LFRTrack/api/login/?_name=Shailendra&_email=Shailendra@pulpstrategy.com&_gcm_token=dfsdf&_password=ert&_device_id=12345";

	public static String CAMPAIGN_DETAILS_URL = SERVER
			+ "LFRTrack/api/campaign/?";
	public static final String POST_LOCATION_URL = SERVER + "";
	public static final String POST_LOGIN_STATUS_URL = SERVER + "";
	public static final String POST_FORM_DATA_URL = SERVER + "";
	public static final String INIT_URL = SERVER + "";
	public static final String IMAGE_UPLOAD_URL = SERVER;
	public static final String USER_NOTIFICATION_URL = SERVER + "";
	public static final String USER_DETAILS_URL = SERVER + "";

	public static String PROMOTOR_LIST = "promotor_list";
	public static String NO_PICTURE_ERROR_MESSAGE = "Picture not found please click again";
	public static String LOGIN = "login";
	public static final int HOME = -1;
	public static final int FRAGMENT2FRAGMENT_REQUEST = -2;
	public static final int TAB_FRAGMENT_REQUEST = -3;
	public static final int POP_RELAUNCH_FRAGMENT = -4;
	public static final String STORE_DETAILS = "store_details";
	public static final String CAMPAIGN_DETAILS = "campaign_details";
	public static final String CAMPAIGN_LIST = "campaign_list";
	public static final String USER_DETAILS = "user_details";

	public static final CharSequence CAMPAIGN_DISPLAY_NAME = "Campaign's";
	public static final int ACTION_PICTURE = 0;
	public static final int SELECT_PICTURE_REQUEST_CODE = 100;
	public static final String LOGIN_ID = "loginid";
	public static final String LOGIN_NAME = "loginame";
	public static final String DEVICEID = "deviceid";
	public static final String SYNC_TO_SERVER = "SYNC_TO_SERVER";
	public static final String LAST_SYNC_TIME = "sync_time";
	public static final String AUTH_TOKEN = "auth_token";

	public static final String CHECKIN_STATUS = "CHECKIN_STATUS";
	public static final String STATUS = "STATUS";

	public static final String GCM = "GCM";
	public static final String INIT = "INIT";
	public static final String BATTERY_STATUS = "battery_status";
	public static int SYNC_INTERVAL = 0;
	public static final String LOCATION_INTERVAL = "location_interval";
	public static final String LOCATION_START_TIME = "location_start_time";
	public static final String LOCATION_END_TIME = "location_end_time";
	public static final long INTERVAL_UPDATE = 1000 * 60;
	public static final String CAMPAIGN_DETAILS_CACHE = "campaign_cache";
	public static final String IS_CACHED = "is_cached";
	public static final String NOTIFICATION = "notification";
	public static final String CACHED_DATA = "cached_data";
	public static final String CACHED_TIME = "cached_time";

	public static final String PROPERTY_GCM_REG_ID = "gcm_registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String USER_EMAIL = "email";
	public static final String USER_ROLE = "role";
	public static final String USER_NUMBER = "number";
	public static boolean CheckinStatus;
	public static boolean afterCheckinButtonClicked;
	public static boolean ReferList = false;

	public static final String START_COUNT = "0";
	public static final String NUMBER = "20";
	public static final String USER_FORM_LIST = "user_form_list";
	public static final String PROMOTER_DETAILS_CACHE = "PROMOTER_DETAILS_CACHE";
	public static final String NOTIFICATION_CACHE = "NOTIFICATION_CACHE";
	public static final String CHECKIN_DETAILS = "Checkin";
	public static final String STORE_CHECKIN = "STORE_CHECKIN";
	public static final int NOTIFI_ID = 1;
	public static final String CAMPAIGN_NAME = "CAMPAIGN_NAME";
	public static final String ASSIGN_CAMPAIGN_NAME = "ASSIGN_CAMPAIGN_NAME";
	public static final String ASSIGN_STORE_NAME = "ASSIGN_STORE_NAME";
	public static final String STORE_LIST = null;
	public static final String ASSIGN_PREF = "ASSIGN_PREF";
	public static final String ASSIGN_CAMAIGN_PREF = "ASSIGN_CAMAIGN_PREF";
	public static final String ASSIGN_STORE_PREF = "ASSIGN_STORE_PREF";

	public static enum LoginType {
		promotor, supervisor
	};

	public static boolean formButtonClicked = false;

}
