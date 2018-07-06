package com.caimatech.riskcontrol.procotol.base;

public class ReturnMsg {

    public static final String SUCCESS = "成功";
    
    //Common Error ReturnMsg
    public static final String WRONG_SIGN = "非法请求源";
    public static final String WRONG_AUTH = "非法请求源";
    public static final String WRONG_REQUEST_SOURCE = "非法请求源";
    public static final String WRONG_SECRET = "错误加密信息";
    
    public static final String EMPTY_REQUEST = "空请求";
    public static final String JSON_PARSE_FAIL = "JSON格式化错误";
    public static final String WRONG_TOKEN = "错误的Token信息";
    
    public static final String ACCT_MAY_NOT_EXIST = "账户可能不存在,请联系客服";
    public static final String WRONG_CALL = "未知Call或者暂未开放";
    public static final String SYSTEM_BUSY = "系统繁忙,请稍后重试";
    public static final String OPERATION_FAIL = "操作失败";
    
    //Key
    public static final String KEY_EXIST = "要创建的Key已经存在";
    public static final String INVALID_KEY_EXIST = "不合法的Key存在，请先删除";
    
    
    //SMS
    public static final String SEND_MOBILE_INVALID = "发送手机号检查不通过";
    public static final String SEND_CONTENT_INVALID = "发送内容检查不通过";
    public static final String SEND_IP_INVALID = "发送IP检查不通过";
    public static final String SEND_IP_RULE = "发送IP规则检查不通过";
    public static final String SEND_MOBILE_RULE = "发送手机号规则检查不通过";
    public static final String SEND_DEVICE_RULE = "发送设备规则检查不通过";
    public static final String SEND_FAIL = "短信发送失败,请联系客服";
    public static final String NO_GET_DYNAMICPWD = "未获取验证码";
    public static final String DEPRECATED_DYNAMICPWD = "验证码已过期";
    public static final String WRONG_DUNAMICPWD = "验证码错误";
    
    //Return
    public static final String ACCOUNT_HAS_REGISTER = "账户已注册";
    public static final String UNKNOW_LOGIN_TYPE = "未知登录类型";
    public static final String ACCOUNT_NO_REGISTER = "账户未注册";
    public static final String WRONG_PASSWORD = "用户名或者密码错误";
    public static final String IDNO_USED = "身份证已注册";
    public static final String CHECK_ID_QUALITY_FAIL = "申请资格认证失败,请稍后重试";
    public static final String PLS_CHECK_ID_QUALITY_FIRST = "请先认证申请资格";
    public static final String FILE_FAIL = "文件提交失败";
    public static final String FILE_REQUEST_ATLAST_ONE = "请至少提交一个文件";
    public static final String PLS_COMPLETE_INFO = "请先完善信息";
    public static final String PLS_ADD_CREDITCARD_FIRST = "请先绑定借记卡";
    public static final String PLS_CALL_CUSTOM_SERVICE = "账户错误，请联系客服";
}
