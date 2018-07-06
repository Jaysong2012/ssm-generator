package com.caimatech.riskcontrol.procotol.base;

import com.alibaba.fastjson.JSONObject;

public class ObjectResponse {

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    private Object response;
    
    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    private long cost;
    
    private String returnCode;
    
    private String returnMsg;
    
    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
    
}
