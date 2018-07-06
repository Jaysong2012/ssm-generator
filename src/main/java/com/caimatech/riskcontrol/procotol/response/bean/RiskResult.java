package com.caimatech.riskcontrol.procotol.response.bean;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;

public class RiskResult {

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getFieldMatchScore() {
        return fieldMatchScore;
    }

    public void setFieldMatchScore(String fieldMatchScore) {
        this.fieldMatchScore = fieldMatchScore;
    }

    public String getCrossValidateResult() {
        return crossValidateResult;
    }

    public void setCrossValidateResult(String crossValidateResult) {
        this.crossValidateResult = crossValidateResult;
    }

    private String resultCode;
    
    private String resultDesc;
    
    private String resultDetail;
    
    private String finalScore;
    
    private String reviewType;
    
    private String fieldMatchScore;
    
    private String crossValidateResult;
    
    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
    
}
