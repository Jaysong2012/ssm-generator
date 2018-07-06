package com.caimatech.riskcontrol.procotol.base;

public class BaseRequest {

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    //请求方UA
    private String ua;
    
    //调用接口
    private String call;
    
    //请求发起时间戳
    private long timestamp;
    
    //请求参数
    private Object args;
    
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private String sign;
}
