package com.caimatech.riskcontrol.procotol.base;

public class ReturnCode {

    public static final String SUCCESS = "000000";
    
    //Common Error ReturnCode
    public static final String WRONG_SIGN = "000001";
    public static final String WRONG_AUTH = "000002";
    public static final String WRONG_REQUEST_SOURCE = "000003";
    public static final String WRONG_SECRET = "000004";
    
    public static final String EMPTY_REQUEST = "000005";
    public static final String JSON_PARSE_FAIL = "000006";
    
    public static final String ACCT_MAY_NOT_EXIST = "000007";
    public static final String WRONG_TOKEN = "000008";
    public static final String WRONG_CALL = "000009";
    public static final String SYSTEM_BUSY = "000010";
    public static final String OPERATION_FAIL = "000011";
    
    //Key Return
    public static final String KEY_EXIST = "001000";
    public static final String INVALID_KEY_EXIST = "001001";
    
    //SMS Return
    public static final String SEND_MOBILE_INVALID = "002000";
    public static final String SEND_CONTENT_INVALID = "002001";
    public static final String SEND_IP_INVALID = "002002";
    public static final String SEND_IP_RULE = "002003";
    public static final String SEND_MOBILE_RULE = "002004";
    public static final String SEND_DEVICE_RULE = "002005";
    public static final String SEND_FAIL = "002006";
    public static final String NO_GET_DYNAMICPWD = "002007";
    public static final String DEPRECATED_DYNAMICPWD = "002008";
    public static final String WRONG_DUNAMICPWD = "002009";
    
    
    // Return
    public static final String ACCOUNT_HAS_REGISTER = "005000";
    public static final String UNKNOW_LOGIN_TYPE = "005001";
    public static final String ACCOUNT_NO_REGISTER = "005002";
    public static final String WRONG_PASSWORD = "005003";
    public static final String INFO_RECORD = "005004";
    public static final String IDNO_USED = "005005";
    public static final String CHECK_ID_QUALITY_FAIL = "005006";
    public static final String PLS_CHECK_ID_QUALITY_FIRST = "005007";
    public static final String FILE_FAIL = "005008";
    public static final String FILE_REQUEST_ATLAST_ONE = "005009";
    public static final String PLS_COMPLETE_INFO = "005010";
    public static final String PLS_ADD_CREDITCARD_FIRST = "005011";
    public static final String PLS_CALL_CUSTOM_SERVICE = "005012";
}
