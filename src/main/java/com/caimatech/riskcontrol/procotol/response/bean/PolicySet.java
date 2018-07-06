package com.caimatech.riskcontrol.procotol.response.bean;

import java.util.ArrayList;

public class PolicySet  {

    public ArrayList<HitRules> getPolicySet() {
        return policySet;
    }

    public void setPolicySet(ArrayList<HitRules> policySet) {
        this.policySet = policySet;
    }

    private ArrayList<HitRules> policySet;
    
}
