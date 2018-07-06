package com.caimatech.riskcontrol.procotol.response.bean;

import java.util.ArrayList;

public class HitRules {

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyScore() {
        return policyScore;
    }

    public void setPolicyScore(String policyScore) {
        this.policyScore = policyScore;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getPolicyDesc() {
        return policyDesc;
    }

    public void setPolicyDesc(String policyDesc) {
        this.policyDesc = policyDesc;
    }

    public String getPolicyResult() {
        return policyResult;
    }

    public void setPolicyResult(String policyResult) {
        this.policyResult = policyResult;
    }

    public ArrayList<HitRule> getHitRules() {
        return hitRules;
    }

    public void setHitRules(ArrayList<HitRule> hitRules) {
        this.hitRules = hitRules;
    }

    private String policyId;
    
    private String policyName;
    
    private String policyScore;
    
    private String policyType;
    
    private String policyDesc;
    
    private String policyResult;
    
    ArrayList<HitRule> hitRules;
    
}
