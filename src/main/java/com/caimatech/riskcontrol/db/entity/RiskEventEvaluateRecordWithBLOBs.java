package com.caimatech.riskcontrol.db.entity;

public class RiskEventEvaluateRecordWithBLOBs extends RiskEventEvaluateRecord {
    private String argsInfo;

    private String resultDetail;

    public String getArgsInfo() {
        return argsInfo;
    }

    public void setArgsInfo(String argsInfo) {
        this.argsInfo = argsInfo == null ? null : argsInfo.trim();
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail == null ? null : resultDetail.trim();
    }
}