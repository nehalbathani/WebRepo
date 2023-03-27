package com.aflac.aims.tph.web.utils;


public class constants {
	public static final String salt = "123#@!456^%$";
	public static final String ERROR_FILE_DIR="/home/itmdev-d-user/data/SITE/NYAFLACD/ftpout/";
	public static final String[] SOURCE_LIST = {"GS","BR"};
	public static final String[] SOURCE_OUT_LIST = {"CAMRAJP","CAMRAUS"};
	
	public static final String SOURCE_GSAM = "GS";
	
	public static final String SOURCE_BR = "BR";
	
	
	public static final String TRADE_STATUS2_NEW ="NEW";
	
	public static final String SEC_GROUP_FX ="FX";
	public static final String SEC_GROUP_BOND ="BND";
	
	public static final String SOURCE_OUT_CAMRA_JP="CAMRAJP";
	public static final String SOURCE_OUT_CAMRA_US="CAMRAUS";
	public static final String SOURCE_OUT_IVT="IVT";
	
	public static final String TRADE_STATUS1_P = "P";
	public static final String TRADE_STATUS1_F = "F";
	public static final String TRADE_STATUS1_N="N";
	public static final String TRADE_STATUS1_D="D";
	public static final String TRADE_STATUS1_M="M";
	
	public static final String TRADE_OUT_STATUS_P="COMPLETE";
	public static final String TRADE_OUT_STATUS_F="FAIL";
	public static final String TRADE_OUT_STATUS_PND = "READY";
	public static final String TRADE_OUT_STATUS_M="MANUAL";
	
	public static final String TRADE_OUT_DISPLAY_STATUS_P="Processed";
	public static final String TRADE_OUT_DISPLAY_STATUS_F="Failed";
	public static final String TRADE_OUT_DISPLAY_STATUS_PND = "Pending";
	public static final String TRADE_OUT_DISPLAY_STATUS_M="Manual";
	public static final String TRADE_OUT_DISPLAY_STATUS_D="Deleted";
	
	public static final String MSG_TRADE_REPROCESS_FAIL="Trade has been FAILED to go for processing";
	public static final String MSG_TRADE_REPROCESS_SUCCESS="Trade has been placed in queue for processing";
	public static final String MSG_TRADE_CANCEL_FAIL="FAILED to Remove Trade from processing";
	public static final String MSG_TRADE_CANCEL_SUCCESS="Trade has been removed from processing";
	public static final String MSG_CAMRA_TRADE_RESEND_SUCCESS="Trade has been placed in queue to resend";
	public static final String MSG_CAMRA_TRADE_RESEND_FAIL="Trade has been FAILED to resend";
	public static final String MSG_CAMRA_TRADE_DISCARD_SUCCESS="Trade has been discarded successfully";
	public static final String MSG_CAMRA_TRADE_DISCARD_FAIL="Trade has been FAILED to discard";
	
	public static final String TRADE_STATUS_FULL_P = "Processed";
	public static final String TRADE_STATUS_FULL_F = "Failed";
	public static final String TRADE_STATUS_FULL_N = "Pending";
	public static final String TRADE_STATUS_FULL_D = "Deleted";
	public static final String TRADE_STATUS_FULL_M = "Manual";
	public static final String CodeUSD = "USD";
	
	public static final String MSG_SECURITY_REPROCESS_FAIL="Security has been FAILED to go for processing";
	public static final String MSG_SECURITY_REPROCESS_SUCCESS="Security has been placed in queue for processing";
	public static final String MSG_SECURITY_CANCEL_FAIL="FAILED to Remove Security from processing";
	public static final String MSG_SECURITY_CANCEL_SUCCESS="Security has been removed from processing";
	public static final String MSG_CAMRA_SECURITY_RESEND_SUCCESS="Security has been placed in queue to resend";
	public static final String MSG_CAMRA_SECURITY_RESEND_FAIL="Security has been FAILED to resend";
	public static final String MSG_CAMRA_SECURITY_DISCARD_SUCCESS="Security has been discarded successfully";
	public static final String MSG_CAMRA_SECURITY_DISCARD_FAIL="Security has been FAILED to discard";
	
	public static final String FIELDMAP_DATATYPE_TRADE = "Trade";
	public static final String FIELDMAP_DATATYPE_SECURITY = "Security";
	
	public static final String SYSTEM_ERROR_CODE = "-100";
	public static final String SYSTEM_ERROR_MSG_TO_DISPLAY = "System error, please contact the system administrator";
	public static final String TICKETERROR_CODE = "101";
	
	public static final String NODATA_ERROR_CODE = "100";
	public static final String NODATA_ERROR_MSG_TO_DISPLAY = "No data available, please contact the system administrator";
	
	public static final String DATAEXISTS_ERROR_CODE = "102";
	public static final String DATAEXISTS_ERROR_MSG_TO_DISPLAY = "Data already exists, please insert new data";
	
	public static final Integer HEARTBEAT_CODE_ALIVE=2;
	public static final Integer HEARTBEAT_CODE_STANDBY=1;
	public static final Integer HEARTBEAT_CODE_DEAD=0;
	
	public static final long HEARTBEAT_INTERVAL_STANDBY=3*60*1000; 
	public static final long HEARTBEAT_INTERVAL_DEAD=5*60*1000; 
	
	public static final String SUCCESS_CODE = "0";
	public static final String SUCCESS_MSG = "Success";
	
	public static final String INVALID_FIELDNAME_ERROR_CODE = "-2";
	public static final String INVALID_FIELDNAME_ERROR_MSG_TO_DISPLAY = "Invalid field name";
	
	public static final String RECORDEXISTS_ERROR_CODE = "-3";
	public static final String RECORDEXISTS_ERROR_MSG_TO_DISPLAY = "Record already exists";
	
	public static final String NORECORDEXISTS_ERROR_CODE = "-4";
	public static final String NORECORDEXISTS_ERROR_MSG_TO_DISPLAY = "No record exists";
	public static final Integer INDICATOR_RED = 0;
	public static final Integer INDICATOR_ORANGE = 1;
	public static final Integer INDICATOR_BLUE = 2;
	public static final Integer INDICATOR_GREEN = 3;
}
