/**
 * 
 */
package com.dmi.constant;

/**
 * @author Mukul Bansal
 *
 */
public class Constants
{

	/*
	 * public static final String APPLICATION_PROJECT_URL =
	 * "http://localhost:7001/DaaSDeviceReg"; public static final String
	 * UI_PROJECT_URL = "http://localhost:3000/#/root/resetPassword";
	 * 
	 * public static final String EMANUAL_DIR = "E:\\EManuals\\";
	 */
	public static final String EMANUAL_DIR = "/home/opc/webapp-configs/IOT-E-Manuals/";
	
	public static final String REDIS_IP = "129.144.51.197";
	// public static final String REDIS_URL = "localhost";
	public static final String REDIS_AUTH = "chanje";
	public static final String REDIS_IOT_DMI_CUSTOMERID="iot_dmi_customerId";
	public static final String REDIS_IOT_DMI_CUSTOMERSERVSUBSERV="iot_dmi_customerServSubserv";
	public static final String REDIS_IOT_DMI_GEOFENCE="iot_dmi_geofence";
	public static final String REDIS_IOT_DMI_MESSAGEMODEL="iot_dmi_messagemodel";
	public static final String REDIS_IOT_DMI_RULES="iot_dmi_rules";

	public static final String APPLICATION_PROJECT_URL = "http://129.191.21.218:8080/IOTWebService/";
	public static final String TOPIC_URL = "http://129.144.51.160:1883/";
	
	public static final String UI_PROJECT_URL = "http://129.191.21.218:8080/dmi-iot/#";

	public static final String HASH_ENCRYPTION_ALGO = "PBKDF2WithHmacSHA1";

	public static final String JWT_KEY = "9C7F2CB481FF410EABFEEFC5D45B9EC7";
	public static final Integer JWT_Timeout = 1000 * 60 * 60 * 24 * 2;

	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final Long USER_ROLE_CODE_NORMAL = 2L;
	public static final Long ADMINUSER_ROLE_CODE= 1L;

	public static final Long USER_STATUS_CODE_ACTIVE = 1L;
	public static final Long USER_STATUS_CODE_INACTIVE = 2L;

	public static final String DEVICE_STATUS_ALIAS_REGISTERED = "Registered";
	public static final String DEVICE_STATUS_ALIAS_UNDER_ACTIVATION = "Under Activation";
	public static final String DEVICE_STATUS_ALIAS_ACTIVATED = "Activated";

	public static final String REGISTRATION_EMAIL_TOPIC = "Confirmation Email - DMI IoT Portal";
	public static final String FROM_EMAIL_ADDRESS = "vhr.myvw@gmail.com";
	public static final Integer EMAIL_JWT_TIMEOUT = 1000 * 60 * 60 * 24;
	public static final String JWT_SUBJECT_PRIMARY_EMAIL_CONFIRMATION = "primaryEmailConfirmation";
	public static final String JWT_SUBJECT_FORGOT_PASSWORD = "forgotPassword";
	public static final String JWT_SUBJECT_VHR_ON_DEMAND = "Vehicle Health Report";
	
	public static final String PASSWORD_RESET_EMAIL_TOPIC = "Password Reset Request - DMI IoT Portal";

	public static final String DATEFORMAT_ORACLE_DB = "DD-MMM-YYYY";

	public static final String UOM_SI = "SI";

	public static final String USER_ROLE_CODE_ADMIN = "Admin";
	public static final String USER_ROLE_CODE_USER = "User";
	public static final String USER_ROLE_CODE_SUPERADMIN = "Superadmin";

	public static final String GEOGRAPHY_TYPE_POLYGON = "Polygon";
	public static final String GEOGRAPHY_TYPE_CIRCLE = "Circle";

	public static final String LOCATION_SERVICE_NAME = "Location Based Services";
	public static final String GEOFENCE_SUBSERVICE_NAME = "Geolocation Alert";
	public static final String CURFEW_SUBSERVICE_NAME = "Curfew ALert";
	public static final String SPEEDLIMIT_SUBSERVICE_NAME = "Speed Limit Alert";
	public static final String DEFAULT_SERVICE_NAME = "Default Service";
	public static final String DEFAULT_SUBSERVICE_NAME = "Default Subservice";
	

	public static final boolean CHECK_LOCATION_SERVICE_SUBSCRIPTION = false;

	public static final Float DETROIT_LAT_MIN = 42.0f;
	public static final Float DETROIT_LAT_MAX = 43.0f;
	public static final Float DETROIT_LNG_MIN = -83.0f;
	public static final Float DETROIT_LNG_MAX = -84.0f;

	public static final String NOTIFICATION_CHANNEL_EMAIL = "Email";
	public static final String NOTIFICATION_CHANNEL_SMS = "SMS";
	public static final String NOTIFICATION_CHANNEL_PUSHNOTFN = "Push Notification";

	public static final String MONDAY = "monday";
	public static final String TUESDAY = "tuesday";
	public static final String WEDNESDAY = "wednesday";
	public static final String THURSDAY = "thursday";
	public static final String FRIDAY = "friday";
	public static final String SATURDAY = "saturday";
	public static final String SUNDAY = "sunday";
	
	public static final String RESPONSE = "response";
	public static final String SUCCESS = "success";
	
	public static final String SIMULATOR_START_FILEPATH = "/home/opc/simulator/scripts/simulator/";
	public static final String SIMULATOR_START_FILENAME = "SimulatorStartScript.sh";
	
	public static final String SIMULATOR_STOP_FILEPATH = "/home/opc/simulator/scripts/simulator/";
	public static final String SIMULATOR_STOP_FILENAME = "SimulatorStopScript.sh";
	
	public static final String SIMULATOR_PID_FILEPATH = "/home/opc/simulator/pid/";
	public static final String SIMULATOR_PID_FILENAME = "simulator.pid";
	
	

}
