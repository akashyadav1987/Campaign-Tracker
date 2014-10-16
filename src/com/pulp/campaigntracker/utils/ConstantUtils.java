package com.pulp.campaigntracker.utils;

public class ConstantUtils {

	// Production and staging server for testing and release.
	public static String SERVER = "http://www.promotadka.in";
	public static String PROD_SERVER = "http://www.promotadka.in/task_manager/api3/";
	//public static String STAGING_SERVER = "http://182.74.47.179/";
	
	//public static String LOGIN_URL = "http://23.229.182.83/task_manager/api3/index.php/login";
	public static String LOGIN_URL = PROD_SERVER + "index.php/login";
	public static String CAMPAIGN_DETAILS_URL = PROD_SERVER+ "campaign.php/campaignlist";
	public static final String USER_DETAILS_URL = PROD_SERVER +"promoters.php/promoterlist";
	public static final String CHECK_IN_URL = PROD_SERVER +"checkin.php/checkin";


	public static String SUBMIT_FORM_URL = SERVER + "LFRTrack/api/SubmitForm/";

	
	public static final String POST_LOCATION_URL = SERVER + "";
	public static final String POST_LOGIN_STATUS_URL = SERVER + "";
	public static final String POST_FORM_DATA_URL = SERVER + "";
	public static final String INIT_URL = SERVER + "";
	public static final String IMAGE_UPLOAD_URL = SERVER;
	public static final String USER_NOTIFICATION_URL = SERVER + "";


	public static String NO_PICTURE_ERROR_MESSAGE = "Picture not found please click again";

	public static final String CAMPAIGNTRACKER_PREF = "campiagn_tracker_pref";
	
	public static final String STORE_DETAILS = "store_details";
	public static final String CAMPAIGN_DETAILS = "campaign_details";
	public static final String CAMPAIGN_LIST = "campaign_list";
	public static final String USER_DETAILS = "user_details";
	public static final String USER_LIST = "user_list";

	public static final CharSequence CAMPAIGN_DISPLAY_NAME = "Campaign Information";
	public static final long LOCATION_UPDATE_INTERVAL = 1000 * 60;
	public static int SYNC_INTERVAL = 0;
	
	//Preferences Setting Variables
	public static final String INIT = "INIT";
	public static final String LOGIN_ID = "loginid";
	public static final String LOGIN_NAME = "loginame";
	public static final String DEVICEID = "deviceid";
	public static final String LAST_SYNC_TIME = "sync_time";
	public static final String AUTH_TOKEN = "auth_token";
	public static final String CHECKIN_STATUS = "checkin_status";
	public static final String BATTERY_STATUS = "battery_status";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_ROLE = "user_role";
	public static final String USER_NUMBER = "user_number";
	public static final String LOCATION_INTERVAL = "location_interval";
	public static final String LOCATION_START_TIME = "location_start_time";
	public static final String LOCATION_END_TIME = "location_end_time";
	public static final String SYNC_INTERVAL_TIME = "Sync_interval";
	public static final String CACHED_DATA = "cached_data";
	public static final String NOTIFICATION = "notification";
	public static final String GCM_REG_ID = "gcm_registration_id";
	public static final String APP_VERSION = "appVersion";
	public static final String STORE_CHECKIN = "store_checkin";
	public static final String ASSIGN_CAMAIGN = "assign_campaign";
	public static final String ASSIGN_STORE = "assign_store";
	
	
	
	public static final int ACTION_PICTURE = 1234;
	public static final int SELECT_PICTURE_REQUEST_CODE = 100;
	public static final int MAX_USER_RESPONSE_COUNT = 3;
	
	//Keys used in the System
	public static final String USER_FORM_LIST = "user_form_list";
	public static final String CAMPAIGN_NAME = "campaign_name";
	public static final String ASSIGN_CAMPAIGN_NAME = "assign_campaign_name";
	public static final String ASSIGN_STORE_NAME = "assign_store_name";
	public static final String STORE_LIST = "Store_list";

	// Server Error Codes
	
	public static final int SERVER_OK = 200;
	public static final int SERVER_AUTH_FAIL = 0;
	public static final int SERVER_MISSING_PARAM = 400;
	public static final int SERVER_ERROR = -1;




}
